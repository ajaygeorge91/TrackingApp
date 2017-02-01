package online.westbay.trackingapp.trackingstuff;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;


import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import online.westbay.trackingapp.R;


public class TrackActivity extends Activity {
	private static final String TAG = "TripTracker/Main";
	static final int LOGIN_REQUEST = 1;

	private CheckBox enabler;

	Messenger mService = null;
	boolean mIsBound;
	final Messenger mMessenger = new Messenger(new IncomingHandler());

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_track);

		/* add minute values to update frequency drop down */
		final Spinner updatefreq = (Spinner)findViewById(R.id.main_updatefreq);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
			this, R.array.main_updatefreq_entries,
			android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(
			android.R.layout.simple_spinner_dropdown_item);
		updatefreq.setAdapter(adapter);

		/* load saved frequency */
		String savedfreq = Prefs.getUpdateFreq(this);
		if (savedfreq != null)
			updatefreq.setSelection(adapter.getPosition(savedfreq));

		/* store frequency when we change it */
		updatefreq.setOnItemSelectedListener(
		new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
									   int pos, long id) {
				Prefs.putUpdateFreq(TrackActivity.this,
					updatefreq.getSelectedItem().toString());

				/* if the service is already running, restart it */
				if (TrackerService.isRunning()) {
					doUnbindService();
					stopService(new Intent(TrackActivity.this,
						TrackerService.class));
					startService(new Intent(TrackActivity.this,
						TrackerService.class));
					doBindService();
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		enabler = (CheckBox)findViewById(R.id.main_enabler);

		enabler.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/* divert focus away from the endpoint text field so it can
				 * validate and hide the keyboard */
				findViewById(R.id.main_layout).requestFocus();

				if (enabler.isChecked()) {

					startService(new Intent(TrackActivity.this,
						TrackerService.class));
						doBindService();
				} else {
					stopService(new Intent(TrackActivity.this,
						TrackerService.class));
					doUnbindService();
				}

				Prefs.putEnabled(TrackActivity.this, enabler.isChecked());
			}
		});




		/* if the service is already running, bind and we'll get back its
		 * recent log ring buffer */
		if (TrackerService.isRunning()) {
			enabler.setChecked(true);
			doBindService();
		}
		else if (Prefs.getEnabled(this) && Prefs.getUserId(this) != null) {
			enabler.performClick();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);
	}

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_exit:
			stopService(new Intent(TrackActivity.this, TrackerService.class));
			doUnbindService();
			finish();
			return true;

		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		try {
			doUnbindService();
		}
		catch (Throwable e) {
			Log.e(TAG, e.getMessage());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == LOGIN_REQUEST) {
			if(resultCode == RESULT_OK){
				Prefs.putUserId(TrackActivity.this, data.getStringExtra("uid"));
				Prefs.putUserEmail(TrackActivity.this, data.getStringExtra("email"));
				Prefs.putUserPassword(TrackActivity.this, data.getStringExtra("password"));
				enabler.performClick();
			} else if (resultCode == RESULT_CANCELED) {
				if(!TrackerService.isRunning()) {
					enabler.setChecked(false);
				}
			}
		}
	}

	public void logText(String text) {
		logText(text, new Date());
	}

	public void logText(String text, Date date) {
		/* DateFormat.SHORT doesn't honor 24 hour time :( */
		String now = (new SimpleDateFormat("HH:mm:ss", Locale.US)).format(date);

		TextView log = (TextView)findViewById(R.id.main_log);
		log.append("[" + now + "] " + text + "\n");

		/* we have to scroll asynchronously because the view isn't updated from
		 * our append() yet, so scrolling to its bottom will not actually reach
		 * the bottom */
		final ScrollView scroller = (ScrollView)findViewById(
			R.id.main_log_scroller);
		scroller.post(new Runnable() {
			@Override
			public void run() {
				scroller.fullScroll(View.FOCUS_DOWN);
			}
		});
	}

	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TrackerService.MSG_LOG:
				logText(msg.getData().getString("log"));
				break;

			case TrackerService.MSG_LOG_RING:
				TextView log = (TextView)findViewById(R.id.main_log);
				log.setText("");

				ArrayList<LogMessage> logs = (ArrayList)msg.obj;

				for (int i = 0; i < logs.size(); i++) {
					LogMessage l = logs.get(i);
					logText(l.message, l.date);
				}

				break;

			default:
				super.handleMessage(msg);
			}
		}
	}

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className,
		IBinder service) {
			mService = new Messenger(service);
			try {
				Message msg = Message.obtain(null,
					TrackerService.MSG_REGISTER_CLIENT);
				msg.replyTo = mMessenger;
				mService.send(msg);
			}
			catch (RemoteException e) {
				logText("Error connecting to service: " + e.getMessage());
			}
		}

		public void onServiceDisconnected(ComponentName className) {
			mService = null;
			logText("Disconnected from service");
		}
	};

	private void doBindService() {
		bindService(new Intent(this, TrackerService.class), mConnection,
			Context.BIND_AUTO_CREATE);
		mIsBound = true;
	}

	private void doUnbindService() {
		if (!mIsBound) {
			return;
		}
		if (mService != null) {
			try {
				Message msg = Message.obtain(null,
					TrackerService.MSG_UNREGISTER_CLIENT);
				msg.replyTo = mMessenger;
				mService.send(msg);
			}
			catch (RemoteException e) {
				Log.e(TAG, e.getMessage());
			}
		}
		unbindService(mConnection);
		mIsBound = false;
		logText("Service stopped");
	}
}

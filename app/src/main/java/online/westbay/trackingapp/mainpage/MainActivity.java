package online.westbay.trackingapp.mainpage;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import online.westbay.trackingapp.R;
import online.westbay.trackingapp.models.MqttMessageDTO;
import online.westbay.trackingapp.models.MqttStatusDTO;
import online.westbay.trackingapp.models.OrganizationUserDTO;
import online.westbay.trackingapp.models.ResponseDTO;
import online.westbay.trackingapp.models.UserDTO;
import online.westbay.trackingapp.models.VehicleDTO;
import online.westbay.trackingapp.models.VehicleStatusDTO;
import online.westbay.trackingapp.mqtt.MqttServiceDelegate;
import online.westbay.trackingapp.mqtt.service.MqttService;
import online.westbay.trackingapp.services.ServiceGenerator;
import online.westbay.trackingapp.services.UserService;
import online.westbay.trackingapp.utils.RxBus;
import online.westbay.trackingapp.utils.SharedPrefs;
import online.westbay.trackingapp.widgets.NonSwipeableViewPager;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements MqttServiceDelegate.MessageHandler, MqttServiceDelegate.StatusHandler {


    private MqttServiceDelegate.MessageReceiver msgReceiver;
    private MqttServiceDelegate.StatusReceiver statusReceiver;

    private HomeFragmentViewPagerAdapter homeFragmentViewPagerAdapter;
    UserService userService;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeFragmentViewPagerAdapter = new HomeFragmentViewPagerAdapter(getSupportFragmentManager());
        userService = ServiceGenerator.createService(UserService.class, SharedPrefs.getToken(this));
        gson = new Gson();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NonSwipeableViewPager viewPager = (NonSwipeableViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(homeFragmentViewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(tabId -> {
            if (tabId == R.id.tab_dashboard) {
                viewPager.setCurrentItem(0, false);
            } else if (tabId == R.id.tab_map) {
                viewPager.setCurrentItem(1, false);
            } else if (tabId == R.id.tab_my_vehicles) {
                viewPager.setCurrentItem(2, false);
            } else if (tabId == R.id.tab_profile) {
                viewPager.setCurrentItem(3, false);
            }
        });

        userService.userDetails()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(error -> {
                    Log.d("app", error.getMessage());
                    return new ResponseDTO<>(error.getMessage());
                })
                .subscribe(s -> {
                    if (s.success) {

                        //Listen to all vehicles
                        ArrayList<String> topics = new ArrayList<>();
                        for (OrganizationUserDTO o : s.data.getOrgList()) {
                            for (VehicleDTO v : o.getVehicles()) {
                                topics.add(v.id);
                            }
                        }
                        topics.add("test");
                        MqttServiceDelegate.startService(this, topics);

                        RxBus.send(s.data);
                    } else {
                        Toast.makeText(this, s.message, Toast.LENGTH_SHORT).show();
                    }
                });

        //Init Receivers
        bindStatusReceiver();
        bindMessageReceiver();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profiles) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        Log.d("app", "onDestroy");

        MqttServiceDelegate.stopService(this);

        unbindMessageReceiver();
        unbindStatusReceiver();

        super.onDestroy();
    }

    private void bindMessageReceiver() {
        msgReceiver = new MqttServiceDelegate.MessageReceiver();
        msgReceiver.registerHandler(this);
        registerReceiver(msgReceiver,
                new IntentFilter(MqttService.MQTT_MSG_RECEIVED_INTENT));
    }

    private void unbindMessageReceiver() {
        if (msgReceiver != null) {
            msgReceiver.unregisterHandler(this);
            unregisterReceiver(msgReceiver);
            msgReceiver = null;
        }
    }

    private void bindStatusReceiver() {
        statusReceiver = new MqttServiceDelegate.StatusReceiver();
        statusReceiver.registerHandler(this);
        registerReceiver(statusReceiver,
                new IntentFilter(MqttService.MQTT_STATUS_INTENT));
    }

    private void unbindStatusReceiver() {
        if (statusReceiver != null) {
            statusReceiver.unregisterHandler(this);
            unregisterReceiver(statusReceiver);
            statusReceiver = null;
        }
    }

    private String getCurrentTimestamp() {
        return new Timestamp(new Date().getTime()).toString();
    }

    @Override
    public void handleMessage(String topic, byte[] payload) {
        String message = new String(payload);

        Log.d("app", "handleMessage: topic=" + topic + ", message=" + message);

        RxBus.send(gson.fromJson(message, VehicleStatusDTO.class));

    }

    @Override
    public void handleStatus(MqttService.ConnectionStatus status, String reason) {
        Log.d("app", "handleStatus: status=" + status + ", reason=" + reason);
        RxBus.send(new MqttStatusDTO(status, reason));
    }


}

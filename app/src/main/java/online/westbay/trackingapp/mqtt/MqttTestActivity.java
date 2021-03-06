package online.westbay.trackingapp.mqtt;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import online.westbay.trackingapp.R;
import online.westbay.trackingapp.mqtt.MqttServiceDelegate.MessageHandler;
import online.westbay.trackingapp.mqtt.MqttServiceDelegate.MessageReceiver;
import online.westbay.trackingapp.mqtt.MqttServiceDelegate.StatusHandler;
import online.westbay.trackingapp.mqtt.MqttServiceDelegate.StatusReceiver;
import online.westbay.trackingapp.mqtt.service.MqttService;
import online.westbay.trackingapp.mqtt.service.MqttService.ConnectionStatus;

public class MqttTestActivity extends Activity implements MessageHandler, StatusHandler {
    private static final Logger LOG = Logger.getLogger(MqttTestActivity.class);

    private MessageReceiver msgReceiver;
    private StatusReceiver statusReceiver;

    private TextView timestampView, topicView, messageView, statusView;

    private EditText publishEditView;
    private Button publishButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LOG.debug("onCreate");

        super.onCreate(savedInstanceState);

        //Init UI
        setContentView(R.layout.main_test);

        timestampView = (TextView) findViewById(R.id.timestampView);
        topicView = (TextView) findViewById(R.id.topicView);
        messageView = (TextView) findViewById(R.id.messageView);
        statusView = (TextView) findViewById(R.id.statusView);

        publishEditView = (EditText) findViewById(R.id.publishEditView);
        publishButton = (Button) findViewById(R.id.publishButton);

        publishButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MqttServiceDelegate.publish(
                        MqttTestActivity.this,
                        "test3",
                        publishEditView.getText().toString().getBytes());
            }
        });

        //Init Receivers
        bindStatusReceiver();
        bindMessageReceiver();

        //Start service if not started
        ArrayList<String> topics = new ArrayList<>();
        topics.add("test1");
        topics.add("test2");
        topics.add("test3");
        MqttServiceDelegate.startService(this, topics);

    }

    @Override
    protected void onDestroy() {
        LOG.debug("onDestroy");

        MqttServiceDelegate.stopService(this);

        unbindMessageReceiver();
        unbindStatusReceiver();

        super.onDestroy();
    }

    private void bindMessageReceiver() {
        msgReceiver = new MessageReceiver();
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
        statusReceiver = new StatusReceiver();
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

        LOG.debug("handleMessage: topic=" + topic + ", message=" + message);

        if (timestampView != null) timestampView.setText("When: " + getCurrentTimestamp());
        if (topicView != null) topicView.setText("Topic: " + topic);
        if (messageView != null) messageView.setText("Message: " + message);
    }

    @Override
    public void handleStatus(ConnectionStatus status, String reason) {
        LOG.debug("handleStatus: status=" + status + ", reason=" + reason);
        if (statusView != null)
            statusView.setText("Status: " + status.toString() + " (" + reason + ")");
    }
}

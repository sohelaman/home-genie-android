package dev.sohel.homegenie.classes;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttHelper {

    final String mqttServerUri = "tcp://157.230.30.178:1883";
    // final String mqttServerUri = "tcp://mqtt.eclipse.org:1883";
    final String clientId = "ftflteam3homeauto";
    final String topic = "topic/ftflteam3/general";

    private Context context;
    private Activity activity;
    private MqttClient client;
    private ActionView actionView;

    private final String TAG = "HomeGenieMQTT";

    public MqttHelper(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public MqttHelper setActionView(ActionView actionView) {
        this.actionView = actionView;
        return this;
    }

    public void setupMqtt() {
        if (this.actionView == null) {
            Toast.makeText(this.context, "MQTT action view not set up.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (this.client != null) {
            return;
        }

        try {
            MqttClient client = new MqttClient(this.mqttServerUri, this.clientId, new MemoryPersistence());
            client.setCallback(this.createMqttCallback());
            client.connect();
            this.client = client;
            client.subscribe(this.topic);
            Toast.makeText(this.context, "MQTT connected", Toast.LENGTH_SHORT).show();

            this.actionView.toggleButton.setChecked(true);

            this.sendMessage("init 0");
        } catch (MqttException e) {
            e.printStackTrace();
            this.client = null;
            this.actionView.toggleButton.setChecked(false);
            Toast.makeText(this.context, "MQTT exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void dismantleMqtt() {
        try {
            if (this.client == null) {
                return;
            }

            this.client.disconnect();
            this.client = null;

            Toast.makeText(this.context, "MQTT disconnected", Toast.LENGTH_SHORT).show();
            this.actionView.toggleButton.setChecked(false);
        } catch (MqttException e) {
            e.printStackTrace();
            this.client = null;
            this.actionView.toggleButton.setChecked(false);
            Toast.makeText(this.context, "MQTT exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private MqttCallback createMqttCallback() {
        return new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.d(TAG, "connectionLost");
                actionView.toggleButton.setChecked(false);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                String payload = new String(message.getPayload());
                Log.d(TAG, "messageArrived:" + payload);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d(TAG, "deliveryComplete");
            }
        };
    }

    public void sendMessage(String str) {
        if (this.client == null) {
            return;
        }

        MqttMessage message = new MqttMessage(str.getBytes());
        message.setQos(2);
        message.setRetained(false);
        try {
            this.client.publish(this.topic, message);
            this.actionView.messageBox.setText(str);
        } catch (MqttException e) {
            e.printStackTrace();
            this.client = null;
            this.actionView.toggleButton.setChecked(false);
            Toast.makeText(this.context, "MQTT exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}

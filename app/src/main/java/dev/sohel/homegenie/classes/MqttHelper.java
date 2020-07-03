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

import java.util.Arrays;

public class MqttHelper {

    final String mqttServerUri = "tcp://157.230.30.178:1883";
    // final String mqttServerUri = "tcp://mqtt.eclipse.org:1883";
    final String topic1 = "topic/ftflteam3/commands";
    final String topic2 = "topic/ftflteam3/activities";
    final String topic3 = "topic/ftflteam3/heartbeats";
    final String topic4 = "topic/ftflteam3/temperature";
    String clientId;

    private Context context;
    private Activity activity;
    private MqttClient client;
    private ActionView actionView;

    private final String TAG = "HomeGenieMQTT";

    public MqttHelper(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
        this.clientId = "android-ftflteam3-" + Math.random() * (999999 - 100000 + 1) + 100000;
        Log.d(TAG, "clientId: " + this.clientId);

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
            client.subscribe(this.topic2);
            client.subscribe(this.topic3);
            client.subscribe(this.topic4);
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
                Log.d(TAG, "messageArrived at topic " + topic + " -- " + payload);
                processIncomingMessage(topic, payload);
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

        if ((this.actionView.switch6.isChecked() && str.startsWith("switch4-"))
                || (this.actionView.switch7.isChecked() && str.startsWith("switch5-"))) {
            Toast.makeText(this.context, "Command will not work on auto mode!", Toast.LENGTH_SHORT).show();
            return;
        }

        MqttMessage message = new MqttMessage(str.getBytes());
        message.setQos(2);
        message.setRetained(false);
        try {
            this.client.publish(this.topic1, message);
            this.actionView.messageBox.setText(str);
        } catch (MqttException e) {
            e.printStackTrace();
            this.client = null;
            this.actionView.toggleButton.setChecked(false);
            Toast.makeText(this.context, "MQTT exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void processIncomingMessage(final String topic, final String str) {
        if (this.client == null) {
            return;
        }

        if (topic.equals(topic1)) {
            this.actionView.messageBox.setText(str);
        } else if (topic.equals(topic2)) {
            try {
                String[] commandParts = str.split("-");
                if (Arrays.asList(this.actionView.switches).contains(commandParts[0])) {
                    this.actionView.updateButtonState(commandParts[0], commandParts[1].equals("on"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (topic.equals(topic3)) {
            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    actionView.textViewHeartbeat.setText(str);
                }
            });
        } else if (topic.equals(topic4)) {
            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // String t = str.split(".")[0] + " C";
                    actionView.textViewCelsius.setText("T: " + str + " ℃"); // ℃
                }
            });
        }
    }

}

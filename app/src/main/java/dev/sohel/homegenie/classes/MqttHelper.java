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

    final String mqttServerUri = "tcp://mqtt.eclipse.org:1883";
    final String clientId = "ftflteam3homeauto";

    private Context context;
    private Activity activity;
    private MqttClient client;

    private final String TAG = "HomeGenieMQTT";

    public MqttHelper(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public void setupMqtt() {
        try {
            MqttClient client = new MqttClient(this.mqttServerUri, this.clientId, new MemoryPersistence());
            client.setCallback(this.createMqttCallback());
            client.connect();
            this.client = client;

            String topic = "topic/ftflteam3/general";
            client.subscribe(topic);
            Toast.makeText(this.context, "MQTT connected", Toast.LENGTH_SHORT).show();

            MqttMessage message = new MqttMessage("init 0".getBytes());
            message.setQos(2);
            message.setRetained(false);
            client.publish(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
            Toast.makeText(this.context, "MQTT exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private MqttCallback createMqttCallback() {
        return new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.d(TAG, "connectionLost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String payload = new String(message.getPayload());
                Log.d(TAG, "messageArrived:" + payload);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d(TAG, "deliveryComplete");
            }
        };
    }

}

package dev.sohel.homegenie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final int PERM_REC_AUDIO = 1;
    private final String TAG = "HomeGenieMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setupSpeechRec();

        try {
            MqttClient client = new MqttClient("tcp://mqtt.eclipse.org:1883", "ftflteam3homeauto", new MemoryPersistence());
            client.setCallback(this.createMqttCallback());
            client.connect();
            String topic = "topic/ftflteam3/general";
            client.subscribe(topic);
            Toast.makeText(this, "MQTT connected", Toast.LENGTH_LONG).show();

            MqttMessage message = new MqttMessage("init 0".getBytes());
            message.setQos(2);
            message.setRetained(false);
            client.publish(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
            Toast.makeText(this, "MQTT exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void checkRecordingPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                // Toast.makeText(this, "Please grant permissions to record audio", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERM_REC_AUDIO);

                // Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                // startActivity(intent);
            }
        }
    }

    private void setupSpeechRec() {
        final EditText editText = findViewById(R.id.editText);
        final SpeechRecognizer speechRec = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent speechRecIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        editText.setEnabled(false);
        speechRecIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRec.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null) editText.setText(matches.get(0));
            }

            @Override
            public void onReadyForSpeech(Bundle bundle) { }

            @Override
            public void onBeginningOfSpeech() { }

            @Override
            public void onRmsChanged(float v) { }

            @Override
            public void onBufferReceived(byte[] bytes) { }

            @Override
            public void onEndOfSpeech() { }

            @Override
            public void onError(int i) { }

            @Override
            public void onPartialResults(Bundle bundle) { }

            @Override
            public void onEvent(int i, Bundle bundle) { }
        });

        findViewById(R.id.button).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                checkRecordingPermission();

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speechRec.stopListening();
                        editText.setHint("Voice commands");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        speechRec.startListening(speechRecIntent);
                        editText.setText("");
                        editText.setHint("Listening...");
                        break;
                }
                return false;
            }
        });
    } // end of setupSpeechRec()

    private void setupMqtt() {
        // TODO
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
                Log.d(TAG, payload);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d(TAG, "deliveryComplete");
            }
       };
    }

}

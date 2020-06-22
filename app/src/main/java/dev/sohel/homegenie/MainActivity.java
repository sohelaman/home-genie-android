package dev.sohel.homegenie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import dev.sohel.homegenie.classes.MqttHelper;
import dev.sohel.homegenie.classes.SpeechHelper;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "HomeGenieMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();

        final EditText commandEditText = findViewById(R.id.commandEditText);
        final ImageButton recordButton = findViewById(R.id.recordButton);

        SpeechHelper speechHelper = new SpeechHelper(MainActivity.this, context);
        speechHelper.setupSpeechRecognition(commandEditText, recordButton);

        MqttHelper mqttHelper = new MqttHelper(MainActivity.this, context);
        mqttHelper.setupMqtt();
    } // onCreate()

}

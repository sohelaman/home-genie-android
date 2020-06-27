package dev.sohel.homegenie.classes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import dev.sohel.homegenie.R;

public class SpeechHelper {

    final int PERM_CODE_REC_AUDIO = 1;

    private Context context;
    private Activity activity;
    private MqttHelper mqttHelper;
    private ActionView actionView;

    public SpeechHelper(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public SpeechHelper setMqttHelper(MqttHelper mqttHelper) {
        this.mqttHelper = mqttHelper;
        return this;
    }

    public SpeechHelper setActionView(ActionView actionView) {
        this.actionView = actionView;
        return this;
    }

    public void checkRecordingPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this.context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.RECORD_AUDIO}, PERM_CODE_REC_AUDIO);
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setupSpeechRecognition(final EditText commandEditText, final ImageButton recordButton) {
        commandEditText.setEnabled(false);

        final SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this.context);
        final Intent speechRecIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        speechRecIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null) {
                    commandEditText.setText(matches.get(0));
                    parseTextToCommand(matches.get(0));
                }
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

        recordButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                checkRecordingPermission();

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        speechRecognizer.stopListening();
                        commandEditText.setHint(context.getString(R.string.command_hint));
                        break;

                    case MotionEvent.ACTION_DOWN:
                        speechRecognizer.startListening(speechRecIntent);
                        commandEditText.setText("");
                        commandEditText.setHint("Listening...");
                        break;
                }
                return false;
            }
        });
    } // end of setupSpeechRecognition()

    public void parseTextToCommand(String text) {
        String[] sw1OnLang = new String[]{"turn on switch 1", "turn on switch one", "turn on blue led", "turn on blue light"};
        String[] sw1OffLang = new String[]{"turn off switch 1", "turn off switch one", "turn off blue led", "turn off blue light"};
        String[] sw2OnLang = new String[]{"turn on switch 2", "turn on switch two", "turn on green led", "turn on green light"};
        String[] sw2OffLang = new String[]{"turn off switch 2", "turn off switch two", "turn off green led", "turn off green light"};
        String[] sw3OnLang = new String[]{"turn on switch 3", "turn on switch three", "turn on red led", "turn on red light"};
        String[] sw3OffLang = new String[]{"turn off switch 3", "turn off switch three", "turn off red led", "turn off red light"};
        String[] sw4OnLang = new String[]{"turn on switch 4", "turn on switch four"};
        String[] sw4OffLang = new String[]{"turn off switch 4", "turn off switch four"};
        String[] ledOnLang = new String[]{"turn on led", "led on", "light on", "turn on the light"};
        String[] ledOffLang = new String[]{"turn off led", "led off", "light off", "turn off the light"};
        String[] motorOnLang = new String[]{"turn on motor", "motor on", "start motor", "start the motor"};
        String[] motorOffLang = new String[]{"turn off motor", "motor off", "stop motor", "stop the motor"};

        text = text.trim().toLowerCase();
        String command = null;
        if (Arrays.asList(sw1OnLang).contains(text)) {
            command = "switch1-on";
        } else if (Arrays.asList(sw1OffLang).contains(text)) {
            command = "switch1-off";
        } else if (Arrays.asList(sw2OnLang).contains(text)) {
            command = "switch2-on";
        } else if (Arrays.asList(sw2OffLang).contains(text)) {
            command = "switch2-off";
        } else if (Arrays.asList(sw3OnLang).contains(text)) {
            command = "switch3-on";
        } else if (Arrays.asList(sw3OffLang).contains(text)) {
            command = "switch3-off";
        } else if (Arrays.asList(sw4OnLang).contains(text)) {
            command = "switch4-on";
        } else if (Arrays.asList(sw4OffLang).contains(text)) {
            command = "switch4-off";
        } else if (Arrays.asList(ledOnLang).contains(text)) {
            command = "led-on";
        } else if (Arrays.asList(ledOffLang).contains(text)) {
            command = "led-off";
        } else if (Arrays.asList(motorOnLang).contains(text)) {
            command = "motor-on";
        } else if (Arrays.asList(motorOffLang).contains(text)) {
            command = "motor-off";
        }

        if (command != null) {
            this.mqttHelper.sendMessage(command);
        }
    } // end of parseTextToCommand()

}

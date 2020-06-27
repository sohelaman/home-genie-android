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
            public void onReadyForSpeech(Bundle bundle) {
            }

            @Override
            public void onBeginningOfSpeech() {
            }

            @Override
            public void onRmsChanged(float v) {
            }

            @Override
            public void onBufferReceived(byte[] bytes) {
            }

            @Override
            public void onEndOfSpeech() {
            }

            @Override
            public void onError(int i) {
            }

            @Override
            public void onPartialResults(Bundle bundle) {
            }

            @Override
            public void onEvent(int i, Bundle bundle) {
            }
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
        text = text.trim().toLowerCase();
        Sentences sentences = new Sentences();
        String command = sentences.map.get(text);

        if (command != null) {
            this.mqttHelper.sendMessage(command);
        }
    } // end of parseTextToCommand()

}

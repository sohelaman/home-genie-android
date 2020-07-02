package dev.sohel.homegenie.classes;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Arrays;

import dev.sohel.homegenie.R;

public class ActionView {

    private final String TAG = "HomeGenieActionView";

    private Activity activity;
    private Context context;
    private MqttHelper mqttHelper;

    final String[] switches;

    final ToggleButton toggleButton;
    final TextView messageBox;
    final Switch switch1;
    final Switch switch2;
    final Switch switch3;
    final Switch switch4;

    public ActionView(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;

        this.toggleButton = this.activity.findViewById(R.id.toggleButton1);
        this.messageBox = this.activity.findViewById(R.id.messageBox);
        this.switch1 = this.activity.findViewById(R.id.switch1);
        this.switch2 = this.activity.findViewById(R.id.switch2);
        this.switch3 = this.activity.findViewById(R.id.switch3);
        this.switch4 = this.activity.findViewById(R.id.switch4);

        this.switches = new String[]{"switch1", "switch2", "switch3", "switch4"};
    } // constructor

    public ActionView setMqttHelper(MqttHelper mqttHelper) {
        this.mqttHelper = mqttHelper;
        return this;
    }

    public void registerListeners() {
        this.toggleButton.setOnCheckedChangeListener(this.getButtonStateChangeListener("toggleButton"));
        this.switch1.setOnCheckedChangeListener(this.getButtonStateChangeListener("switch1"));
        this.switch2.setOnCheckedChangeListener(this.getButtonStateChangeListener("switch2"));
        this.switch3.setOnCheckedChangeListener(this.getButtonStateChangeListener("switch3"));
        this.switch4.setOnCheckedChangeListener(this.getButtonStateChangeListener("switch4"));
    } // end of registerListeners()


    public CompoundButton.OnCheckedChangeListener getButtonStateChangeListener(String buttonName) {
        if (Arrays.asList(this.switches).contains(buttonName)) {
            return new MyOnCheckedChangeListener(buttonName, this.mqttHelper);
        } else if (buttonName.equals("toggleButton")) {
            return new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        if (isChecked) {
                            mqttHelper.dismantleMqtt();
                            mqttHelper.setupMqtt();
                        } else {
                            mqttHelper.dismantleMqtt();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        }

        return null;
    } // end of getButtonStateChangeListener()


    public void updateButtonState(final String buttonName, final boolean isChecked) {
        final Switch targetSwitch;

        switch (buttonName) {
            case "switch1":
                targetSwitch = this.switch1;
                break;
            case "switch2":
                targetSwitch = this.switch2;
                break;
            case "switch3":
                targetSwitch = this.switch3;
                break;
            case "switch4":
                targetSwitch = this.switch4;
                break;
            default:
                targetSwitch = null;
                break;
        }

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (targetSwitch != null) {
                    targetSwitch.setOnCheckedChangeListener(null);
                    targetSwitch.setChecked(isChecked);
                    targetSwitch.setOnCheckedChangeListener(getButtonStateChangeListener(buttonName));
                }
            }
        }, 100);
    } // end of updateButtonState()

}

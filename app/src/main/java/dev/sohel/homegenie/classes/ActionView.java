package dev.sohel.homegenie.classes;

import android.app.Activity;
import android.content.Context;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import dev.sohel.homegenie.R;

public class ActionView {

    private Activity activity;
    private Context context;
    private MqttHelper mqttHelper;

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
    } // constructor

    public ActionView setMqttHelper(MqttHelper mqttHelper) {
        this.mqttHelper = mqttHelper;
        return this;
    }

    public void registerListeners() {
        this.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mqttHelper.setupMqtt();
                } else {
                    mqttHelper.dismantleMqtt();
                }
            }
        });
        this.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String command = isChecked ? "switch1-on" : "switch1-off";
                mqttHelper.sendMessage(command);
            }
        });
        this.switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String command = isChecked ? "switch2-on" : "switch2-off";
                mqttHelper.sendMessage(command);
            }
        });
        this.switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String command = isChecked ? "switch3-on" : "switch3-off";
                mqttHelper.sendMessage(command);
            }
        });
        this.switch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String command = isChecked ? "switch4-on" : "switch4-off";
                mqttHelper.sendMessage(command);
            }
        });
    } // end of registerListeners()

}

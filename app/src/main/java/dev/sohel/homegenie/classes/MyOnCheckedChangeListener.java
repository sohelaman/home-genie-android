package dev.sohel.homegenie.classes;

import android.util.Log;
import android.widget.CompoundButton;

public class MyOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

    String TAG = "[MyOnCheckedChangeListener]";
    String buttonName;
    MqttHelper mqttHelper;

    public MyOnCheckedChangeListener(String buttonName, MqttHelper mqttHelper) {
        this.buttonName = buttonName;
        this.mqttHelper = mqttHelper;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String command = isChecked ? this.buttonName + "-on" : this.buttonName + "-off";
        Log.d(TAG, this.buttonName + " value changed to " + (isChecked ? "'true'" : "'false'"));
        this.mqttHelper.sendMessage(command);
    }

}

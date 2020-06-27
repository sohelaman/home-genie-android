package dev.sohel.homegenie.classes;

import java.util.HashMap;

public class Sentences {

    public final HashMap<String, String> map;

    public Sentences() {
        this.map = new HashMap<String, String>();
        this.enumerate();
    }

    public void enumerate() {
        String sw1On = "switch1-on";
        map.put("turn on switch 1", sw1On);
        map.put("turn on switch one", sw1On);
        map.put("turn on blue led", sw1On);
        map.put("turn on blue light", sw1On);

        String sw1Off = "switch1-off";
        map.put("turn off switch 1", sw1Off);
        map.put("turn off switch one", sw1Off);
        map.put("turn off blue led", sw1Off);
        map.put("turn off blue light", sw1Off);

        String sw2On = "switch2-on";
        map.put("turn on switch 2", sw2On);
        map.put("turn on switch two", sw2On);
        map.put("turn on green led", sw2On);
        map.put("turn on green light", sw2On);

        String sw2Off = "switch2-off";
        map.put("turn off switch 2", sw2Off);
        map.put("turn off switch two", sw2Off);
        map.put("turn off green led", sw2Off);
        map.put("turn off green light", sw2Off);

        String sw3On = "switch3-on";
        map.put("turn on switch 3", sw3On);
        map.put("turn on switch three", sw3On);
        map.put("turn on red led", sw3On);
        map.put("turn on red light", sw3On);

        String sw3Off = "switch3-off";
        map.put("turn off switch 3", sw3Off);
        map.put("turn off switch three", sw3Off);
        map.put("turn off red led", sw3Off);
        map.put("turn off red light", sw3Off);

        String sw4On = "switch4-on";
        map.put("turn on switch 4", sw4On);
        map.put("turn on switch four", sw4On);

        String sw4Off = "switch4-off";
        map.put("turn off switch 4", sw4Off);
        map.put("turn off switch four", sw4Off);

        String ledOn = "led-on";
        map.put("turn on led", ledOn);
        map.put("led on", ledOn);
        map.put("light on", ledOn);
        map.put("turn on the light", ledOn);

        String ledOff = "led-off";
        map.put("turn off led", ledOff);
        map.put("led off", ledOff);
        map.put("light off", ledOff);
        map.put("turn off the light", ledOff);

        String motorOn = "motor-on";
        map.put("turn on motor", motorOn);
        map.put("motor on", motorOn);
        map.put("start motor", motorOn);
        map.put("start the motor", motorOn);

        String motorOff = "motor-off";
        map.put("turn off motor", motorOff);
        map.put("motor off", motorOff);
        map.put("stop motor", motorOff);
        map.put("stop the motor", motorOff);

    } // end of enumerate()

} // end of class

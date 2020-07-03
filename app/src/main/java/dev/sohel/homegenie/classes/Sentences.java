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

        String sw5On = "switch5-on";
        map.put("turn on switch 5", sw5On);
        map.put("turn on switch five", sw5On);
        map.put("turn on led", sw5On);
        map.put("led on", sw5On);
        map.put("light on", sw5On);
        map.put("turn on the light", sw5On);

        String sw5Off = "switch5-off";
        map.put("turn off switch 5", sw5Off);
        map.put("turn off switch five", sw5Off);
        map.put("turn off led", sw5Off);
        map.put("led off", sw5Off);
        map.put("light off", sw5Off);
        map.put("turn off the light", sw5Off);

        String sw6On = "switch6-on";
        map.put("turn on switch 6", sw6On);
        map.put("turn on switch six", sw6On);
        map.put("turn on motor", sw6On);
        map.put("motor on", sw6On);
        map.put("start motor", sw6On);
        map.put("start the motor", sw6On);

        String sw6Off = "switch6-off";
        map.put("turn off switch 6", sw6Off);
        map.put("turn off switch six", sw6Off);
        map.put("turn off motor", sw6Off);
        map.put("motor off", sw6Off);
        map.put("stop motor", sw6Off);
        map.put("stop the motor", sw6Off);

        String sw7On = "switch7-on";
        map.put("turn on switch 7", sw7On);
        map.put("turn on switch seven", sw7On);
        map.put("light auto", sw7On);
        map.put("auto light", sw7On);

        String sw7Off = "switch7-off";
        map.put("turn off switch 7", sw7Off);
        map.put("turn off switch seven", sw7Off);
        map.put("fan auto", sw7Off);
        map.put("auto fan", sw7Off);

        // String ledOn = "led-on";
        // map.put("turn on led", ledOn);
        // map.put("led on", ledOn);
        // map.put("light on", ledOn);
        // map.put("turn on the light", ledOn);

        // String ledOff = "led-off";
        // map.put("turn off led", ledOff);
        // map.put("led off", ledOff);
        // map.put("light off", ledOff);
        // map.put("turn off the light", ledOff);

        // String motorOn = "motor-on";
        // map.put("turn on motor", motorOn);
        // map.put("motor on", motorOn);
        // map.put("start motor", motorOn);
        // map.put("start the motor", motorOn);

        // String motorOff = "motor-off";
        // map.put("turn off motor", motorOff);
        // map.put("motor off", motorOff);
        // map.put("stop motor", motorOff);
        // map.put("stop the motor", motorOff);

    } // end of enumerate()

} // end of class

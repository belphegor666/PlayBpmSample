package org.tmr.raw;

import org.tmr.TemperatureEvent;
import org.tmr.WaterState;

import java.util.UUID;

public class H2OStateTemplate {

    private WaterState state;
    private UUID id;
    private int temperature;
    private final int freezingPoint = 0;
    private final int boilingPoint = 100;
    private final int startingTemperature = 25;

    private void init() {
        id = UUID.randomUUID();
        temperature = startingTemperature;
        calculateState();
    }

    private H2OStateTemplate() { init(); }

    public static H2OStateTemplate newInstance() { return new H2OStateTemplate(); }

    public int getTemperature() { return temperature; }

    public WaterState getState () { return state; }

    public UUID getId() { return id; }

    public void processEvent ( TemperatureEvent e ) {
        WaterState fromState = getState();
        if ( e == TemperatureEvent.COOL ) {
            temperature -= 25;
        } else if ( e == TemperatureEvent.HEAT ) {
            temperature += 25;
        } else {
            //something has gone wrong
            //TODO
        }

        calculateState();
        WaterState toState = getState();
        debug(fromState,toState);
    }

    private void calculateState() {
        if ( temperature <= freezingPoint) {
            state = WaterState.ICE;
        } else if (temperature >= boilingPoint) {
            state = WaterState.STEAM;
        } else {
            state = WaterState.WATER;
        }
    }

    private void debug(WaterState from, WaterState to) {
        System.out.println("Transitioning from "+from+" to "+to);
    }

}

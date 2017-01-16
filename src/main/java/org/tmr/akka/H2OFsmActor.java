package org.tmr.akka;

import akka.actor.UntypedActor;
import org.tmr.TemperatureEvent;
import org.tmr.WaterState;

public class H2OFsmActor extends UntypedActor {

    private WaterState state;
    private int temperature;
    private final int freezingPoint = 0;
    private final int boilingPoint = 100;

    public WaterState getState() { return state; }
    public int getTemperature() { return temperature; }
    private void heat() { temperature += 25; }
    private void cool() { temperature -= 25; }

    @Override
    public void preStart() {
        temperature = 25;
        calculateState();
    }

    @Override
    public void onReceive(Object o) {
        WaterState fromState = getState();
        if(o instanceof TemperatureEvent) {
            TemperatureEvent e = (TemperatureEvent) o;
            if (e == TemperatureEvent.HEAT) {
                heat();
            } else if (e == TemperatureEvent.COOL) {
                cool();
            } else {
                //Unknown event type
                //TODO
            }

            calculateState();

        } else {
            //Uknown message type
            //TODO
        }
        WaterState toState = getState();
        debug(fromState,toState);
        //reply
        getSender().tell(getState(),self());
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

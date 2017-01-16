package org.tmr.akkalambda;

import akka.actor.AbstractFSM;
import org.tmr.WaterState;

import static org.tmr.TemperatureEvent.COOL;
import static org.tmr.TemperatureEvent.HEAT;

public class H2OFsmLambda extends AbstractFSM<WaterState, Integer> {

    private final int freezingPoint = 0;
    private final int boilingPoint = 100;

    {
        startWith(WaterState.WATER, 25);

        when(WaterState.ICE,
            matchEventEquals(COOL, Integer.class,
                (event, temperature) -> {
                    temperature -= 25;
                    WaterState newState = calculateState(temperature);
                    return goTo(newState)
                            .using(temperature)
                            .replying(newState);
                }
            )
        );

        when(WaterState.ICE,
            matchEventEquals(HEAT, Integer.class,
                (event, temperature) -> {
                    temperature += 25;
                    WaterState newState = calculateState(temperature);
                    return goTo(newState)
                            .using(temperature)
                            .replying(newState);
                }
            )
        );

        when(WaterState.WATER,
            matchEventEquals(HEAT, Integer.class,
                (event, temperature) -> {
                    temperature += 25;
                    WaterState newState = calculateState(temperature);
                    return goTo(newState)
                            .using(temperature)
                            .replying(newState);
                }
            )
        );

        when(WaterState.WATER,
            matchEventEquals(COOL, Integer.class,
                (event, temperature) -> {
                    temperature -= 25;
                    WaterState newState = calculateState(temperature);
                    return goTo(newState)
                            .using(temperature)
                            .replying(newState);
                }
            )
        );

        when(WaterState.STEAM,
            matchEventEquals(COOL, Integer.class,
                (event, temperature) -> {
                    temperature -= 25;
                    WaterState newState = calculateState(temperature);
                    return goTo(newState)
                            .using(temperature)
                            .replying(newState);
                }
            )
        );

        when(WaterState.STEAM,
            matchEventEquals(HEAT, Integer.class,
                (event, temperature) -> {
                    temperature += 25;
                    WaterState newState = calculateState(temperature);
                    return goTo(newState)
                            .using(temperature)
                            .replying(newState);
                }
            )
        );

        whenUnhandled(
            matchAnyEvent((event, data) -> {
                log().warning("Received unknown event: " + event);
                return stay().using(stateData());
            })
        );

        initialize();

        onTransition(this::handler);
    }

    private WaterState calculateState(int temperature) {
        if ( temperature <= freezingPoint) {
            return WaterState.ICE;
        } else if (temperature >= boilingPoint) {
            return WaterState.STEAM;
        } else {
            return WaterState.WATER;
        }
    }

    public void handler(WaterState from, WaterState to) {
        System.out.println("Transitioning from "+from+" to "+to);
    }

}

package org.tmr.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tmr.TemperatureEvent;
import org.tmr.WaterState;
import org.tmr.akkalambda.H2OFsmLambda;

public class TestH2OFsmActor {
    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("FsmActorTest");
    }

    @AfterClass
    public static void tearDown() {
        JavaTestKit.shutdownActorSystem(system);
        system = null;
    }


    @Test
    public void testLoggingFSM()
    {
        new JavaTestKit(system) {{
            final ActorRef tester = system.actorOf(Props.create(H2OFsmActor.class));

            tester.tell(TemperatureEvent.COOL,getRef());
            expectMsgEquals(WaterState.ICE);

            tester.tell(TemperatureEvent.HEAT,getRef());
            expectMsgEquals(WaterState.WATER);

            tester.tell(TemperatureEvent.HEAT,getRef());
            expectMsgEquals(WaterState.WATER);

            tester.tell(TemperatureEvent.HEAT,getRef());
            expectMsgEquals(WaterState.WATER);

            tester.tell(TemperatureEvent.HEAT,getRef());
            expectMsgEquals(WaterState.STEAM);

        }};
    }

}

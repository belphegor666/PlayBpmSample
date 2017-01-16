package org.tmr.akkalambda;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import akka.testkit.TestFSMRef;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tmr.TemperatureEvent;
import org.tmr.WaterState;

public class TestH2OFsmLambda {
    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("FsmLambdaTest");
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
            final ActorRef tester = system.actorOf(Props.create(H2OFsmLambda.class));

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

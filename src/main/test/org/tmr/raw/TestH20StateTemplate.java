package org.tmr.raw;

import org.junit.Test;
import org.tmr.TemperatureEvent;
import org.tmr.WaterState;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestH20StateTemplate {

    @Test
    public void TestInitialState() {
        H2OStateTemplate h2o = H2OStateTemplate.newInstance();
        assertTrue(h2o.getState() == WaterState.WATER);
    }

    @Test
    public void TestHeatEvent() {
        H2OStateTemplate h2o = H2OStateTemplate.newInstance();
        h2o.processEvent(TemperatureEvent.HEAT);
        assertEquals(h2o.getTemperature(),50);
    }

    @Test
    public void TestCoolEvent() {
        H2OStateTemplate h2o = H2OStateTemplate.newInstance();
        h2o.processEvent(TemperatureEvent.COOL);
        assertEquals(h2o.getTemperature(),0);
        assertTrue(h2o.getState() == WaterState.ICE);
    }

    @Test
    public void TestBoilingPoint_1() {
        H2OStateTemplate h2o = H2OStateTemplate.newInstance();
        h2o.processEvent(TemperatureEvent.HEAT);
        h2o.processEvent(TemperatureEvent.HEAT);
        assertEquals(h2o.getTemperature(),75);
        assertTrue(h2o.getState() == WaterState.WATER);
        h2o.processEvent(TemperatureEvent.HEAT);
        assertEquals(h2o.getTemperature(),100);
        assertTrue(h2o.getState() == WaterState.STEAM);
    }

    @Test
    public void TestBoilingPoint_2() {
        H2OStateTemplate h2o = H2OStateTemplate.newInstance();
        h2o.processEvent(TemperatureEvent.HEAT);
        h2o.processEvent(TemperatureEvent.HEAT);
        h2o.processEvent(TemperatureEvent.HEAT);
        h2o.processEvent(TemperatureEvent.HEAT);
        assertEquals(h2o.getTemperature(),125);
        assertTrue(h2o.getState() == WaterState.STEAM);
        h2o.processEvent(TemperatureEvent.COOL);
        assertEquals(h2o.getTemperature(),100);
        assertTrue(h2o.getState() == WaterState.STEAM);
        h2o.processEvent(TemperatureEvent.COOL);
        assertEquals(h2o.getTemperature(),75);
        assertTrue(h2o.getState() == WaterState.WATER);
    }

}

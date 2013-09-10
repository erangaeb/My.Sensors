package com.wasn.Sensors.pojo;


/**
 * POJO class to hold sensor data attributes
 */
public class Sensor {

    String sensorName;
    String sensorvalue;

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getSensorvalue() {
        return sensorvalue;
    }

    public void setSensorvalue(String sensorvalue) {
        this.sensorvalue = sensorvalue;
    }

    public Sensor(String sensorName, String sensorvalue) {
        this.sensorName = sensorName;
        this.sensorvalue = sensorvalue;
    }
}

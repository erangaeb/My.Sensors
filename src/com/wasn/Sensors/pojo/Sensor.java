package com.wasn.Sensors.pojo;


/**
 * POJO class to hold sensor data attributes
 */
public class Sensor {

    String sensorName;
    String sensorValue;
    boolean isAvailable;

    public Sensor(String sensorName, String sensorValue, boolean available) {
        this.sensorName = sensorName;
        this.sensorValue = sensorValue;
        isAvailable = available;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getSensorValue() {
        return sensorValue;
    }

    public void setSensorValue(String sensorValue) {
        this.sensorValue = sensorValue;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}

package com.wasn.Sensors.pojo;


/**
 * POJO class to hold sensor data attributes
 */
public class Sensor {

    String sensorName;
    String sensorValue;
    boolean isAvailable;
    boolean isMySensor;

    public Sensor(String sensorName, String sensorValue, boolean available, boolean isMySensor) {
        this.sensorName = sensorName;
        this.sensorValue = sensorValue;
        this.isAvailable = available;
        this.isMySensor = isMySensor;
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

    public boolean isMySensor() {
        return isMySensor;
    }

    public void setMySensor(boolean mySensor) {
        isMySensor = mySensor;
    }
}

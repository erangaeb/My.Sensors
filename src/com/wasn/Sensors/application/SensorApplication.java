package com.wasn.Sensors.application;

import android.app.Application;


/**
 * Application class to hold shared attributes
 *
 * @author erangaeb@gmail.com (eranga herath)
 */
public class SensorApplication extends Application {

    private boolean locationUpdateRequested;

    private String location;

    // determine sensor type
    //  1. my sensors
    //  2. friends sensors
    public final static String MY_SENSORS = "MY_SENSORS";
    public final static String FRIENDS_SENSORS = "FRIENDS_SENSORS";
    public final static String SENSOR_TYPE = "SENSOR_TYPE";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isLocationUpdateRequested() {
        return locationUpdateRequested;
    }

    public void setLocationUpdateRequested(boolean locationUpdateRequested) {
        this.locationUpdateRequested = locationUpdateRequested;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {
        super.onCreate();

        this.location = "NOT AVAILABLE";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTerminate() {
        super.onTerminate();

        this.locationUpdateRequested = false;
    }

}

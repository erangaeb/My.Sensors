package com.wasn.Sensors.service;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import com.wasn.Sensors.application.SensorApplication;

/**
 * Created with IntelliJ IDEA.
 * User: eranga
 * Date: 10/3/13
 * Time: 8:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class LocationTask extends AsyncTask implements LocationListener {
    private static final String LOCATION_PROVIDER = LocationManager.NETWORK_PROVIDER;
    SensorApplication application;
    private Location location;
    LocationManager locationManager;
    Location lastKnowLocation;

    public LocationTask(SensorApplication application) {
        System.out.println("init location");

        this.application = application;
        locationManager = (LocationManager) application.getSystemService(Context.LOCATION_SERVICE);
        lastKnowLocation = locationManager.getLastKnownLocation(LOCATION_PROVIDER);
    }

    @Override
    protected Object doInBackground(Object... arg0) {
        Looper.prepare();
        // Request GPS updates. The third param is the looper to use, which defaults the the one for
        // the current thread.
        System.out.println("getting location");
        //locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
        Looper.loop(); // start waiting...when this is done, we'll have the location in this.location
        // location available
        System.out.println("location got");

        return null;
    }

    @Override
    protected void onPostExecute(Object result) {
        // notify someone we are done...
    }

    @Override
    public void onLocationChanged(Location location) {
        // Store the location, then get the current thread's looper and tell it to
        // quit looping so it can continue on doing work with the new location.
        this.location = location;
        Looper.myLooper().quit();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onProviderEnabled(String provider) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onProviderDisabled(String provider) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}

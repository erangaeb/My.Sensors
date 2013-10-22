package com.wasn.Sensors.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.wasn.Sensors.application.SensorApplication;
import com.wasn.Sensors.pojo.Query;
import com.wasn.Sensors.utils.QueryParser;

import java.util.HashMap;

public class GpsReadingService extends Service implements GooglePlayServicesClient.ConnectionCallbacks,
                                                                GooglePlayServicesClient.OnConnectionFailedListener {

    SensorApplication application;
    private LocationClient locationClient;

    /*
     Called before service  onStart method is called.All Initialization part goes here
    */
    @Override
    public void onCreate() {
        application = (SensorApplication) getApplication();
        locationClient = new LocationClient(getApplicationContext(), this,this);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("###################################");
        System.out.println("//////LocationService Started//////");
        System.out.println("###################################");

        if (isServicesConnected())
            locationClient.connect();

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.out.println("###################################");
        System.out.println("//////LocationConnection Fail//////");
        System.out.println("###################################");
    }

    @Override
    public void onConnected(Bundle arg0) {
        System.out.println("###################################");
        System.out.println("//////////LocationConnected////////");
        System.out.println("###################################");

        Location location = locationClient.getLastLocation();

        if(location!=null) {
            System.out.println("###################################");
            System.out.println("***********************************");
            System.out.println("//////// " + location.getLatitude());
            System.out.println("//////// " + location.getLongitude());
            System.out.println("###################################");
            if(application.isServiceRequest()) {
                // construct message to send
                String command = "DATA";
                String user = application.getQuery().getUser();
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("lat", Double.toString(location.getLatitude()));
                params.put("lon", Double.toString(location.getLongitude()));
                String message = QueryParser.getMessage(new Query(command, user, params));

                // send data to server
                if(application.getWebSocketConnection().isConnected()) {
                    application.getWebSocketConnection().sendTextMessage(message);
                }
            } else {

            }

        }

        stopSelf();
    }

    @Override
    public void onDisconnected() {
        System.out.println("###################################");
        System.out.println("////////LocationDisconnected///////");
        System.out.println("###################################");
    }

    /**
     * Verify that Google Play services is available before making a request.
     * @return true if Google Play services is available, otherwise false
     */
    private boolean isServicesConnected() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(GpsReadingService.this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        } else {
            return false;
        }
    }

    /*
     Called when Service running in background is stopped.
     Remove location update to stop receiving gps reading
    */
    @Override
    public void onDestroy() {
        System.out.println("###################################");
        System.out.println("/////LocationService Destroyed/////");
        System.out.println("###################################");

        super.onDestroy();
    }
}

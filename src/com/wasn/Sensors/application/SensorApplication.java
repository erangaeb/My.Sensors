package com.wasn.Sensors.application;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import de.tavendo.autobahn.WebSocket;
import de.tavendo.autobahn.WebSocketConnection;


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

    public final static String WEB_SOCKET_URI = "ws://10.2.4.14:9000";

    // web socket connection share in application
    public final WebSocket webSocketConnection = new WebSocketConnection();

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

    public WebSocket getWebSocketConnection() {
        return webSocketConnection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTerminate() {
        super.onTerminate();

        this.locationUpdateRequested = false;
    }

    Handler handler = new Handler() {
        public void handleMessage(Message message) {
            if (realCallback!=null) {
                realCallback.handleMessage(message);
            }
        }
    };

    Handler.Callback realCallback=null;

    public Handler getHandler() {
        return handler;
    }

    public void setCallback(Handler.Callback c) {
        realCallback = c;
    }
}

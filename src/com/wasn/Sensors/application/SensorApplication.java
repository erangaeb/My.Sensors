package com.wasn.Sensors.application;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import com.wasn.Sensors.pojo.Sensor;
import com.wasn.Sensors.pojo.User;
import de.tavendo.autobahn.WebSocket;
import de.tavendo.autobahn.WebSocketConnection;


/**
 * Application class to hold shared attributes
 *
 * @author erangaeb@gmail.com (eranga herath)
 */
public class SensorApplication extends Application {

    // current sensor
    Sensor currentSensor;

    // current user
    User user;

    // determine sensor type
    //  1. my sensors
    //  2. friends sensors
    public final static String MY_SENSORS = "MY_SENSORS";
    public final static String FRIENDS_SENSORS = "FRIENDS_SENSORS";
    public final static String SENSOR_TYPE = "SENSOR_TYPE";

    public final static String WEB_SOCKET_URI = "ws://10.2.4.14:9000";

    // web socket connection share in application
    public final WebSocket webSocketConnection = new WebSocketConnection();

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public Sensor getCurrentSensor() {
        return currentSensor;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCurrentSensor(Sensor currentSensor) {
        this.currentSensor = currentSensor;
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

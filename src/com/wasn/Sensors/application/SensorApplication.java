package com.wasn.Sensors.application;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import com.wasn.Sensors.pojo.Query;
import com.wasn.Sensors.pojo.Sensor;
import com.wasn.Sensors.pojo.User;
import de.tavendo.autobahn.WebSocket;
import de.tavendo.autobahn.WebSocketConnection;

import java.util.ArrayList;
import java.util.Random;

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

    // keep shared sensors
    ArrayList<Sensor> fiendSensorList = new ArrayList<Sensor>();
    ArrayList<Sensor> mySensorList = new ArrayList<Sensor>();

    // determine sensor type
    //  1. my sensors
    //  2. friends sensors
    public final static String MY_SENSORS = "MY_SENSORS";
    public final static String FRIENDS_SENSORS = "FRIENDS_SENSORS";
    public static String SENSOR = MY_SENSORS;

    //public final static String WEB_SOCKET_URI = "ws://10.2.4.14:9000";
    public final static String WEB_SOCKET_URI = "ws://mysensors.ucsc.lk:9000";

    // web socket connection share in application
    public final WebSocket webSocketConnection = new WebSocketConnection();

    // track weather request is from from web socket service or activity
    private boolean isServiceRequest = true;

    // incoming query from server
    private Query query;

    private Query currentDataQuery;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {
        super.onCreate();

        // initially add my location to my sensor list
        mySensorList.add(new Sensor("I'm", "Location", "LocationValue", true, false));
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

    public ArrayList<Sensor> getFiendSensorList() {
        return fiendSensorList;
    }

    public void setFiendSensorList(ArrayList<Sensor> fiendSensorList) {
        this.fiendSensorList = fiendSensorList;
    }

    public ArrayList<Sensor> getMySensorList() {
        return mySensorList;
    }

    public void setMySensorList(ArrayList<Sensor> mySensorList) {
        this.mySensorList = mySensorList;
    }

    public String getRandomLocation() {
        ArrayList<String> locations = new ArrayList<String>();
        locations.add("Kirulapona");
        locations.add("Thimibirigasyaya");
        locations.add("Nugegoda");
        locations.add("Maharagam");
        Random random = new Random(); //Create random class object
        int randomNumber = random.nextInt(locations.size()); //Generate a random number (index) with the size of the list being the maximum
        return locations.get(randomNumber);
    }

    public boolean isServiceRequest() {
        return isServiceRequest;
    }

    public void setServiceRequest(boolean serviceRequest) {
        isServiceRequest = serviceRequest;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public Query getCurrentDataQuery() {
        return currentDataQuery;
    }

    public void setCurrentDataQuery(Query currentDataQuery) {
        this.currentDataQuery = currentDataQuery;
    }
}

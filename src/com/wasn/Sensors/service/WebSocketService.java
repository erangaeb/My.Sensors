package com.wasn.Sensors.service;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import com.wasn.Sensors.R;
import com.wasn.Sensors.application.SensorApplication;
import com.wasn.Sensors.exceptions.InvalidQueryException;
import com.wasn.Sensors.pojo.Query;
import com.wasn.Sensors.pojo.Sensor;
import com.wasn.Sensors.ui.HomeActivity;
import com.wasn.Sensors.utils.QueryParser;
import de.tavendo.autobahn.WebSocketConnectionHandler;
import de.tavendo.autobahn.WebSocketException;

import java.util.HashMap;

/**
 * Service for listen to a web socket
 * On login to application this service need tobe start
 *
 * @author erangaeb@gmail.com (eranga herath)
 */
public class WebSocketService extends Service {

    SensorApplication application;

    // notification Id
    private static final int NOTIFICATION_ID = 1;
    private static final int MESSAGE_NOTIFICATION_ID = 2;

    NotificationManager notificationManager;
    Notification.Builder builder;
    PendingIntent updatePendingIntent;

    @Override
    public void onCreate() {
        application = (SensorApplication) getApplication();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new Notification.Builder(this);

        System.out.println("///////////////////////////////////");
        System.out.println("//////////SERVICE_CREATED//////////");
        System.out.println("///////////////////////////////////");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        System.out.println("///////////////////////////////////");
        System.out.println("//////////SERVICE_STARTED//////////");
        System.out.println("///////////////////////////////////");

        // connect to web socket from here
        connectToWebSocket(application);
        initNotification();

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        System.out.println("///////////////////////////////////");
        System.out.println("/////////////DESTROYED/////////////");
        System.out.println("///////////////////////////////////");

        // close web socket connection here
        cancelNotification();
        if(application.getWebSocketConnection().isConnected())
            application.getWebSocketConnection().disconnect();
    }

    /**
     * connect to web socket
     * when connecting we need to send username and password of current user
     * in order to continue communication
     * @param application application object
     */
    public void connectToWebSocket(final SensorApplication application) {
        try {
            application.getWebSocketConnection().connect(SensorApplication.WEB_SOCKET_URI, new WebSocketConnectionHandler() {
                @Override
                public void onOpen() {
                    // send login query to user
                    // TODO generate login query with user data
                    String query = "LOGIN" + " " + "#username" + " " + application.getUser().getUsername() + " " + "#password" + " " + application.getUser().getPassword();
                    application.getWebSocketConnection().sendTextMessage(query);
                }

                @Override
                public void onTextMessage(String payload) {
                    // delegate to handleMessage
                    handleQuery(application, payload);
                }

                @Override
                public void onClose(int code, String reason) {
                    System.out.println("///////////////////////////////////");
                    System.out.println("//////////////STOPPED//////////////");
                    System.out.println("///////////////////////////////////");

                    // TODO start service again
                    // TODO no need to stop service instead connect to web socket again
                    stopForeground(true);
                }
            });
        } catch (WebSocketException e) {
            System.out.println(e);
        }
    }

    /**
     * Generate login query and send to server
     * @param application application object instance
     */
    private void handleLogin(SensorApplication application) {
        // generate login query with user credentials
        String command = "LOGIN";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", application.getUser().getUsername());
        params.put("password", application.getUser().getPassword());
        String message = QueryParser.getMessage(new Query(command, "TEST", params));

        application.getWebSocketConnection().sendTextMessage(message);
    }

    /**
     * Handle query message from web socket
     *
     * @param application application object
     * @param payload payload from server
     */
    private void handleQuery(SensorApplication application, String payload) {
        System.out.println("PAYLOAD: " + payload);

        try {
            // need to parse query in order to further processing
            Query query = QueryParser.parse(payload);

            if(query.getCommand().equalsIgnoreCase("STATUS")) {
                // STATUS query
                // handle LOGIN and SHARE status from handleStatus
                handleStatusQuery(application, query);
            } else if(query.getCommand().equalsIgnoreCase("SHARE")) {
                // SHARE query
                // handle SHARE query from handleShare
                handleShareQuery(application, query);
            } else if (query.getCommand().equalsIgnoreCase("GET")) {
                // GET query
                // handle via handleGet
                handleGetQuery(application, query);
            } else if(query.getCommand().equalsIgnoreCase("DATA")) {
                // DATA query
                // handle via handleData
                handleDataQuery(application, query);
            } else {
                // invalid query or not supporting query
                System.out.println("INVALID/NOT SUPPORT query");
            }
        } catch (InvalidQueryException e) {
            System.out.println(e);
        }
    }

    /**
     * Handle STATUS query from server
     * @param application application
     * @param query parsed query
     */
    private void handleStatusQuery(SensorApplication application, Query query) {
        // get status from query
        String status = "success";

        if (query.getParams().containsKey("login")) {
            // login status
            status = query.getParams().get("login");
        } else if (query.getParams().containsKey("share")){
            // share status
            status = query.getParams().get("share");
        }

        // just send status to available handler
        sendMessage(application, status, false);
    }

    /**
     * Handle SHARE query from server
     * @param application application
     * @param query parsed query
     */
    private void handleShareQuery(SensorApplication application, Query query) {
        // add new sensor to friend sensor list that shared in application
        // add sensor test as 'TAP HERE' since user can tap and get sensor data ap Heretching user
        application.getFiendSensorList().add(new Sensor(query.getUser(), "Location", "Location", false, false));

        // update notification to notify user about incoming query/ share request
        updateNotification("Location @" + query.getUser());
    }

    /**
     * Handle GET query from server
     * @param application application
     * @param query parsed query
     */
    private void handleGetQuery(SensorApplication application, Query query) {
        // get location and create query
        // send created query to server
        // currently send query with dummy location
        // TODO start location manager to mange location functions
        if(application.getWebSocketConnection().isConnected()) {
            // generate query message
            // send it to server
            // TODO get real location instead of dummy location
            String command = "DATA";
            String user = query.getUser();
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("gps", application.getRandomLocation());
            String message = QueryParser.getMessage(new Query(command, user, params));

            application.getWebSocketConnection().sendTextMessage(message);
            // new LocationTask(application).execute();
        }
    }

    /**
     * Handle DATA query from server
     * @param application application
     * @param query parsed query
     */
    private void handleDataQuery(SensorApplication application, Query query) {
        // when data receives update matching sensor(at friend sensor list) with incoming sensor value
        // we assume here incoming query contains gps value of user
        for(Sensor sensor: application.getFiendSensorList()) {
            // find updating sensor
            if(sensor.getUser().equalsIgnoreCase(query.getUser())) {
                // query user and sensor user should be match
                sensor.setSensorValue(query.getParams().get("gps"));
                sensor.setAvailable(true);
            }
        }

        // send message to available handler to notify incoming sensor value
        // TODO we assume here incoming query contains gps value of user, so need to matching parameter instade of
        // TODO adding 'gps'
        sendMessage(application, query.getParams().get("gps"), false);
    }

    /**
     * Send message to appropriate handler
     * @param application application
     * @param payload payload from server
     * @param updateNotification update notification or not
     */
    private void sendMessage(SensorApplication application, String payload, boolean updateNotification) {
        Message message = Message.obtain();
        message.obj = payload;
        if (application.getHandler()!=null) {
            application.getHandler().sendMessage(message);
            if (updateNotification)
                updateNotification(payload);
        }
    }

    /**
     * Create notification when staring web socket service
     * Need to dismiss notification when disconnect from web socket
     */
    private void initNotification() {
        Intent resultIntent = new Intent(this, HomeActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack
        stackBuilder.addParentStack(HomeActivity.class);
        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(resultIntent);

        // Gets a PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Build notification
        // Actions are just fake
        builder.setContentTitle("SenZors")
               .setContentText("Touch for launch SenZors").setSmallIcon(R.drawable.google_plus)
               .setContentIntent(resultPendingIntent).build();

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;

        startForeground(NOTIFICATION_ID, notification);
        //notificationManager.notify(NOTIFICATION_ID, notification);
    }

    /**
     * Create and update notification when query receives from server
     * No we have two notifications regarding Sensor application
     * @param message incoming query
     */
    private void updateNotification(String message) {
        SensorApplication.SENSOR = SensorApplication.FRIENDS_SENSORS;

        Intent notificationIntent = new Intent(this, HomeActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentTitle("New SenZ");
        builder.setContentText(message);
        builder.setContentIntent(contentIntent);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(MESSAGE_NOTIFICATION_ID, notification);
    }

    /**
     * Cancel notification
     * need to cancel when disconnect from web socket
     */
    private void cancelNotification() {
        String notificationService = Context.NOTIFICATION_SERVICE;
        NotificationManager notificationManager = (NotificationManager) getSystemService(notificationService);

        notificationManager.cancel(NOTIFICATION_ID);
    }

}

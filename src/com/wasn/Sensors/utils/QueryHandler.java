package com.wasn.Sensors.utils;

import android.content.Intent;
import android.os.Message;
import com.wasn.Sensors.application.SensorApplication;
import com.wasn.Sensors.exceptions.InvalidQueryException;
import com.wasn.Sensors.pojo.LatLon;
import com.wasn.Sensors.pojo.Query;
import com.wasn.Sensors.pojo.Sensor;
import com.wasn.Sensors.service.GpsReadingService;

import java.util.HashMap;

/**
 * Handler class for incoming queries
 * Handle following queries
 *  1. STATUS
 *  2. SHARE
 *  3. GET
 *  4. LOGIN
 *  5. DATA
 *
 *  @author Eranga Herath(erangaeb@gmail.com)
 */
public class QueryHandler {

    /**
     * Generate login query and send to server
     * @param application application object instance
     */
    public static void handleLogin(SensorApplication application) {
        // generate login query with user credentials
        String command = "LOGIN";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", application.getUser().getUsername());
        params.put("password", application.getUser().getPassword());
        String message = QueryParser.getMessage(new Query(command, "", params));

        application.getWebSocketConnection().sendTextMessage(message);
    }

    /**
     * Handle query message from web socket
     *
     * @param application application object
     * @param payload payload from server
     */
    public static void handleQuery(SensorApplication application, String payload) {
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
                System.out.println("INVALID/UN-SUPPORTING query");
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
    private static void handleStatusQuery(SensorApplication application, Query query) {
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
        sendMessage(application, status);
    }

    /**
     * Handle SHARE query from server
     * @param application application
     * @param query parsed query
     */
    private static void handleShareQuery(SensorApplication application, Query query) {
        // add new sensor to friend sensor list that shared in application
        application.getFiendSensorList().add(new Sensor(query.getUser(), "Location", "Location", false, false));

        // update notification to notify user about incoming query/ share request
        NotificationUtils.updateNotification(application.getApplicationContext(), "Location @" + query.getUser());
    }

    /**
     * Handle GET query from server
     * @param application application
     * @param query parsed query
     */
    private static void handleGetQuery(SensorApplication application, Query query) {
        // get location by starting location service
        if(application.getWebSocketConnection().isConnected()) {
            // current location request is from web socket service
            // start location service
            application.setServiceRequest(true);
            application.setQuery(query);
            Intent serviceIntent = new Intent(application.getApplicationContext(), GpsReadingService.class);
            application.getApplicationContext().startService(serviceIntent);
        }
    }

    /**
     * Handle DATA query from server
     * @param application application
     * @param query parsed query
     */
    private static void handleDataQuery(SensorApplication application, Query query) {
        // create LatLon object from query params
        // we assume incoming query contains lat lon values
        LatLon latLon = new LatLon(query.getParams().get("lat"), query.getParams().get("lon"));
        application.setCurrentDataQuery(query);

        // send message to available handler to notify incoming sensor value
        sendMessage(application, latLon);
    }

    /**
     * Send message to appropriate handler
     * @param application application
     * @param obj payload from server
     */
    private static void sendMessage(SensorApplication application, Object obj) {
        Message message = Message.obtain();
        message.obj = obj;
        if (application.getHandler()!=null) {
            application.getHandler().sendMessage(message);
        }
    }

}

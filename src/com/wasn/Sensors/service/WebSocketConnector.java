package com.wasn.Sensors.service;

import android.os.Message;
import com.wasn.Sensors.application.SensorApplication;
import com.wasn.Sensors.exceptions.InvalidQueryException;
import com.wasn.Sensors.pojo.Query;
import com.wasn.Sensors.pojo.Sensor;
import com.wasn.Sensors.utils.QueryParser;
import de.tavendo.autobahn.WebSocketConnectionHandler;
import de.tavendo.autobahn.WebSocketException;

/**
 * Handle web socket connections and messages
 *
 * @author erangaeb@gmail.com (eranga herath)
 */
public class WebSocketConnector {

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
                    handleMessage(application, payload);
                }

                @Override
                public void onClose(int code, String reason) {

                }
            });
        } catch (WebSocketException e) {
            System.out.println(e);
        }
    }

    /**
     * Handle message from web socket
     *
     * @param application application object
     * @param payload payload from server
     */
    private void handleMessage(SensorApplication application, String payload) {
        try {
            // parse payload and get Query
            Query query = QueryParser.parse(payload);
            System.out.println("--------------------" + payload);

            // if messages except "Goodbye" or "Welcome" (except login message) we have to handle it via another thread
            // another thread need to handle tp process query/ query response
            if(payload.equalsIgnoreCase("LOGIN_SUCCESS") || payload.equalsIgnoreCase("LOGIN_FAIL")) {
                // Login request message
                Message message = Message.obtain();
                message.obj = payload;
                if (application.getHandler()!=null)
                    application.getHandler().sendMessage(message);
            } else if(payload.startsWith("SHARE_SUCCESS") || payload.startsWith("SHARE_FAIL")) {
                // sharing status
                // ignore here
                System.out.println("SHARE SUCCESS/FAIL");
                System.out.println("Payload " + payload);
            } else if(payload.startsWith("SHARE")) {
                // share query
                // need to add new sensor to sensor list share in application
                application.getFiendSensorList().add(new Sensor(query.getUser(), "Location" , "Location", false, false));
            } else if(payload.startsWith("GET")) {
                // get query
                // get location and send to user
                // send dummy location now
                // TODO start location manager to mange location functions
                if(application.getWebSocketConnection().isConnected()) {
                    // get  user from query
                    // temporary solution
                    String user = query.getUser();
                    String response = "DATA" + " " + "#gps" + " " + application.getRandomLocation() + " " + user;
                    application.getWebSocketConnection().sendTextMessage(response);
                }
            } else if(payload.startsWith("DATA")) {
                // data response from peer
                // update fried sensor value as well
                application.getFiendSensorList().get(0).setSensorValue(query.getUser());

                // send data to available handlers
                Message message = Message.obtain();
                message.obj = payload;
                if (application.getHandler()!=null)
                    application.getHandler().sendMessage(message);
            } else {
                System.out.println("case elsessssssss");
                System.out.println("Payload " + payload);
                // start location task to get location
                // new LocationTask(application).execute("GET");
            }
        } catch (InvalidQueryException e) {
            System.out.println(e.toString());
        }
    }

}

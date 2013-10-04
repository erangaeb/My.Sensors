package com.wasn.Sensors.service;

import android.content.Intent;
import android.os.Message;
import com.wasn.Sensors.application.SensorApplication;
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
     * Handle message from websocket
     *
     * @param application application object
     * @param payload payload from server
     */
    private void handleMessage(SensorApplication application, String payload) {
        System.out.println("payload -------" + payload);
        // TODO use query parser to parser query

        // if messages except "Goodbye" or "Welcome" (except login message) we have to handle it via another thread
        // another thread need to handle tp process query/ query response
        if(payload.equalsIgnoreCase("LOGIN_SUCCESS") || payload.equalsIgnoreCase("LOGIN_FAIL")) {
            // Login request message
            Message message = Message.obtain();
            message.obj = payload;
            if (application.getHandler()!=null)
                application.getHandler().sendMessage(message);
        } else {
            // Query result or Share request
            /*Message message = Message.obtain();
            message.obj = payload;
            if (application.getHandler()!=null)
                application.getHandler().sendMessage(message);*/
            //if(!payload.equalsIgnoreCase("SHARE_SUCCESS")) {
            //Intent intent = new Intent(application.getApplicationContext(), LocationService.class);
            //application.getApplicationContext().startService(intent);
            //}
            new LocationTask(application).execute("GET");

            System.out.println("started------");
        }
    }

    private void getCurrentLocation() {

    }
}

package com.wasn.Sensors.utils;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import com.wasn.Sensors.R;
import com.wasn.Sensors.application.SensorApplication;
import com.wasn.Sensors.service.WebSocketService;
import com.wasn.Sensors.ui.HomeActivity;

/**
 * Utility class for create and update notifications
 *
 * @author erangaeb@gmail.com (eranga herath)
 */
public class NotificationUtils {

    // notification Id
    private static final int SERVICE_NOTIFICATION_ID = 1;
    private static final int MESSAGE_NOTIFICATION_ID = 2;

    // notification managers
    private static NotificationManager notificationManager;
    private static Notification.Builder builder;

    /**
     * Create notification when staring web socket service
     * Need to dismiss notification when disconnect from web socket
     */
    public static void initNotification(WebSocketService webSocketService) {
        // initialize notification manages
        if(notificationManager == null) {
            notificationManager = (NotificationManager) webSocketService.getSystemService(Context.NOTIFICATION_SERVICE);
            builder = new Notification.Builder(webSocketService);
        }

        Intent resultIntent = new Intent(webSocketService, HomeActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(webSocketService);
        // Adds the back stack
        stackBuilder.addParentStack(HomeActivity.class);
        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(resultIntent);

        // Gets a PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Build notification
        builder.setContentTitle("SenZors")
                .setContentText("Touch for launch SenZors").setSmallIcon(R.drawable.google_plus)
                .setContentIntent(resultPendingIntent).build();

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;

        webSocketService.startForeground(SERVICE_NOTIFICATION_ID, notification);
    }

    /**
     * Create and update notification when query receives from server
     * No we have two notifications regarding Sensor application
     *
     * @param message incoming query
     */
    public static void updateNotification(Context context, String message) {
        SensorApplication.SENSOR = SensorApplication.FRIENDS_SENSORS;

        Intent notificationIntent = new Intent(context, HomeActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

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
    public static void cancelNotification() {
        notificationManager.cancel(SERVICE_NOTIFICATION_ID);
        notificationManager.cancel(MESSAGE_NOTIFICATION_ID);
    }

}

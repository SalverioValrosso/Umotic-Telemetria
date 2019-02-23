package org.altervista.umotic.umotic;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class NotificationHelper extends ContextWrapper {

    private static final String CHANNEL_ID = "com.telemetria.umotic.telemetria.notificationId";
    private static final String CHANNEL_NAME = "Umotic";
    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
        createChannels();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannels() {
        //IMPORTANCE_DEFAULT = show everywhere
        //IMPORTANCE_HIGH = show every
        NotificationChannel umoticChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        umoticChannel.enableLights(true);
        umoticChannel.enableVibration(true);
        umoticChannel.setLightColor(Color.BLUE);
        umoticChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(umoticChannel);


    }

    public NotificationManager getManager() {
        if(manager == null){
            manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getUmoticNotification(String title, String body){
        return new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentText(body)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_cars)
                .setAutoCancel(true);
    }
}

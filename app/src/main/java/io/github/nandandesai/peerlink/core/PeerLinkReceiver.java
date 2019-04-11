package io.github.nandandesai.peerlink.core;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import fi.iki.elonen.NanoHTTPD;
import io.github.nandandesai.peerlink.R;

import java.io.IOException;

public class PeerLinkReceiver extends NanoHTTPD {

    private static final String TAG = "PeerLinkReceiver";

    private Context context;

    public PeerLinkReceiver(Context context, int port) throws IOException {
        super(port);
        this.context=context;
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        Log.d(TAG, "PeerLinkReceiver: Server started in port :"+port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        displayNotification("PeerLink", "New request received from Tor.");
        String msg = "{\"id\":\"abcd\", \"message\":\"1234\"}";
        Log.d(TAG, "serve: NEW REQUEST RECEIVED");
        return newFixedLengthResponse(Response.Status.OK, "application/json", msg);
    }

    public void stop(){
        super.stop();
    }

    private void displayNotification(String title, String task) {


        //properly construct this method to be compatible with all the versions of Android and clean up this method
        //and place it where it should be used.

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final String NOTIF_CHANNEL_ID="peerlink_msgs";
        final String NOTIF_CHANNEL_NAME="PeerLink Messages";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIF_CHANNEL_ID, NOTIF_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, NOTIF_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(task)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_launcher_foreground);


        notificationManager.notify(1, notification.build());
    }
}

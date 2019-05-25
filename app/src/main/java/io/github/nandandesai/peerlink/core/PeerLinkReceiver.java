package io.github.nandandesai.peerlink.core;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;

import fi.iki.elonen.NanoHTTPD;
import io.github.nandandesai.peerlink.R;
import io.github.nandandesai.peerlink.database.PeerLinkDatabase;
import io.github.nandandesai.peerlink.models.ChatMessage;
import io.github.nandandesai.peerlink.repositories.ChatMessageRepository;
import io.github.nandandesai.peerlink.utils.PeerLinkPreferences;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class PeerLinkReceiver extends NanoHTTPD {

    private static final String TAG = "PeerLinkReceiver";

    private Context context;
    private ChatMessageRepository chatMessageRepository;
    public PeerLinkReceiver(Application application, int port) throws IOException {
        super(port);
        this.context=application.getApplicationContext();
        this.chatMessageRepository=new ChatMessageRepository(application);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        Log.d(TAG, "PeerLinkReceiver: Server started in port :"+port);
    }

    @Override
    public Response serve(IHTTPSession session) {

        String msg = "{\"status\":\"success\", \"message\":\"Successfully received the message.\"}";
        try {
            HashMap<String,String> map = new HashMap<>();
            session.parseBody(map);
            Log.d(TAG, "serve: content received: "+map.toString());
            ChatMessage chatMessage=new Gson().fromJson(map.get("postData"),ChatMessage.class);

            /*
                 ChatId currently is the address of the person I'm (the "user") messaging to.
                 The first time I'm sending someone a message or receiving a message from someone (message is received from this Method),
                 I'm creating dummy rows in ChatSession and PeerLinkSession in ChatMessageRepository.
                 So, the message I send from ChatActivity or the message I receive from here, both will be sent to ChatMessageRepository.
                 There, if I'm receiving the message from a new person, then his address will be stored as ChatId.
                 If I'm sending the message to someone, then his/her address will be store as ChatId.

                 Now, as in this method I'm receiving a message from someone else, the ChatId that will be stored in chatMessage object
                 will be my own address. So, I will have to rewrite it with his address and then store it in my database.
            */
            chatMessage.setChatId(chatMessage.getMessageFrom());


            PeerLinkDatabase peerLinkDatabase=PeerLinkDatabase.getInstance(context);
            String contactName = peerLinkDatabase.contactDao().getContactName(chatMessage.getChatId());
            String chatName;
            if (contactName != null) {
                chatName = contactName;
            } else {
                chatName = chatMessage.getChatId();
            }

            //send the notification
            displayNotification(chatName, chatMessage.getMessageContent());


            //temporary code below////////
            chatMessage.setMessageId(chatMessage.getMessageId()+"RECEIVED");
            ///////////////////////////////

            chatMessage.setMessageStatus(ChatMessage.STATUS.USER_NOT_READ);
            chatMessageRepository.insert(chatMessage);
            Log.d(TAG, "serve: message successfully inserted");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ResponseException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }



        return newFixedLengthResponse(Response.Status.OK, "application/json", msg);
    }

    public void stop(){
        super.stop();
    }

    private String inputStreamToString(InputStream stream) throws IOException {
        int n = 0;
        char[] buffer = new char[1024 * 4];
        InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        StringWriter writer = new StringWriter();
        while (-1 != (n = reader.read(buffer))) writer.write(buffer, 0, n);
        return writer.toString();
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
                .setSmallIcon(R.drawable.ic_stat_notification);


        notificationManager.notify(1, notification.build());
    }
}

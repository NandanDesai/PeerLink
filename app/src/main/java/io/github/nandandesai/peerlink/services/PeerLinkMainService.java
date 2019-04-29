package io.github.nandandesai.peerlink.services;

import android.app.Service;
import android.arch.lifecycle.LifecycleService;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import androidx.work.ListenableWorker;
import io.github.nandandesai.peerlink.core.PeerLinkReceiver;
import io.github.nandandesai.peerlink.models.ChatMessage;
import io.github.nandandesai.peerlink.repositories.ChatMessageRepository;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PeerLinkMainService extends LifecycleService {
    private static final String TAG = "PeerLinkMainService";

    private ExecutorService executorService;
    private PeerLinkReceiver peerLinkReceiver;
    private ChatMessageRepository chatMessageRepository;
    private Observer<List<ChatMessage>> chatMessagesObserver;
    public PeerLinkMainService() {}

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: service started");
        super.onStartCommand(intent, flags, startId);
        executorService = Executors.newCachedThreadPool();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "run: ABOUT TO START THE SERVER");
                    peerLinkReceiver = new PeerLinkReceiver(getApplication(),9000);
                }catch (IOException io){
                    io.printStackTrace();
                }
            }
        });

        chatMessageRepository=new ChatMessageRepository(getApplication());
        chatMessagesObserver=new Observer<List<ChatMessage>>() {
            @Override
            public void onChanged(@Nullable List<ChatMessage> chatMessages) {
                if(chatMessages!=null){
                    for(ChatMessage chatMessage:chatMessages) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "run: received a new WAITING_TO_SEND message. Trying to send it now to: "+chatMessage.getMessageTo()+" and the message Id is: "+chatMessage.getMessageId());
                                int retry=0;
                                while (true){
                                    if(retry==5){
                                        Log.d(TAG, "run: Tried 5 times to send the message but failed. Giving up now :(");
                                        break;
                                    }
                                    if(!messageSender(chatMessage)){
                                        Log.d(TAG, "run: failed sending the message with retry="+retry);
                                        try {
                                            Thread.sleep(3000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }else{
                                        Log.d(TAG, "run: delivered. Changing the message status to DELIVERED_TO_RECIPIENT and breaking the loop.");
                                        chatMessageRepository.updateMessageStatusWithMessageId(chatMessage.getMessageId(), ChatMessage.STATUS.DELIVERED_TO_RECIPIENT);
                                        break;
                                    }
                                    retry++;
                                }
                            }
                        }).start();
                    }
                }
            }
        };
        chatMessageRepository.getAllUnsentMsgs().observe(this, chatMessagesObserver);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: service destroyed");
        Intent broadcastIntent = new Intent(this, PeerLinkRestartReceiver.class);
        sendBroadcast(broadcastIntent);


        //stop the logic you are running here

        peerLinkReceiver.stop();
        Log.d(TAG, "onDestroy: SERVER STOPPED");
        executorService.shutdown();

    }

    private boolean messageSender(ChatMessage chatMessage){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .proxy(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 9050)))
                .build();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, new Gson().toJson(chatMessage));
        Request request = new Request.Builder()
                .url("http://"+chatMessage.getMessageTo()+":9000")
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            Log.d(TAG, "messageSender: response :"+response.body().string());
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}

package io.github.nandandesai.peerlink.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.github.nandandesai.peerlink.core.PeerLinkReceiver;
import io.github.nandandesai.peerlink.repositories.ChatMessageRepository;

public class PeerLinkMainService extends Service {
    private static final String TAG = "PeerLinkMainService";

    private ExecutorService executorService;
    private PeerLinkReceiver peerLinkReceiver;

    public PeerLinkMainService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
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
                    peerLinkReceiver = new PeerLinkReceiver(getApplicationContext(),9000);
                }catch (IOException io){
                    io.printStackTrace();
                }
            }
        });




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


}

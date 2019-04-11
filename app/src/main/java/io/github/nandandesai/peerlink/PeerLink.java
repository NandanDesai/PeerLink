package io.github.nandandesai.peerlink;

import android.app.Application;
import android.util.Log;

import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.ios.IosEmojiProvider;

public class PeerLink extends Application {
    private static final String TAG = "PeerLink";

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: The first line of the app executed");
        super.onCreate();

        //initialize emoji keyboard thing...
        EmojiManager.install(new IosEmojiProvider());
    }




}

package io.github.nandandesai.peerlink;

import android.app.Application;

import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.ios.IosEmojiProvider;

public class PeerLink extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //initialize emoji keyboard thing...
        EmojiManager.install(new IosEmojiProvider());
    }
}

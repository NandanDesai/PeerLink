package io.github.nandandesai.peerlink.utils;

import android.content.Context;

import org.whispersystems.libsignal.InvalidKeyException;

import java.io.IOException;

public class SetupHelper {
    private Context context;
    private PeerLinkPreferences preferences;
    public SetupHelper(Context context) {
        this.context = context;
        preferences=new PeerLinkPreferences(context);
    }

    public boolean identityKeyDone(){
        boolean status=false;
        try {
            preferences.getMyIdentityKeyPair();
            status=true;
        } catch (IOException e) {
        } catch (InvalidKeyException e) {
        }
        return status;
    }

    public boolean registrationIdDone(){
        return preferences.getMyRegistrationId() != 0;
    }

    public boolean onionAddressDone(){
        return preferences.getMyOnionAddress()!=null;
    }
}

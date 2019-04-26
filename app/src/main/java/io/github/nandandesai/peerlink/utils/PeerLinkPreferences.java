package io.github.nandandesai.peerlink.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;

import java.io.IOException;
import java.security.SecureRandom;

public class PeerLinkPreferences {

    private static final String SHARED_PREF_MY_IDENTITY_KEY="my_identity_key";
    private static final String SHARED_PREF_SIGNEDPREKEYID ="signed_prekey_id";
    private static final String SHARED_PREF_NEXT_PREKEYID="prekey_id";
    private static final String SHARED_PREF_MY_REGISTRATION_ID="my_registration_id";
    private static final String SHARED_PREF_NAME="PeerLinkSharedPrefs";

    private static final String FIRST_TIME_USE ="first_time_use";

    private static final String MY_ONION_ADDRESS="my_onion_address";


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PeerLinkPreferences(Context context) {
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
    }

    public void storeMyIdentityKeyPair(IdentityKeyPair identityKeyPair){
        editor = sharedPreferences.edit();
        String identityKey= Base64.encodeBytes(identityKeyPair.serialize());
        editor.putString(SHARED_PREF_MY_IDENTITY_KEY, identityKey);
        editor.apply();
    }

    public IdentityKeyPair getMyIdentityKeyPair() throws IOException, InvalidKeyException {
        byte[] serializedIdentityKeyPair= Base64.decode(sharedPreferences.getString(SHARED_PREF_MY_IDENTITY_KEY,""));
        return new IdentityKeyPair(serializedIdentityKeyPair);
    }

    public void storeMyRegistrationId(int registrationId){
        editor=sharedPreferences.edit();
        editor.putInt(SHARED_PREF_MY_REGISTRATION_ID, registrationId);
        editor.apply();
    }

    public int getMyRegistrationId(){
        return sharedPreferences.getInt(SHARED_PREF_MY_REGISTRATION_ID, 0);
    }

    //was called getCurrentPreKeyId
    public int getCurrentPreKeyId(){
        int returningValue=sharedPreferences.getInt(SHARED_PREF_NEXT_PREKEYID, new SecureRandom().nextInt(Integer.MAX_VALUE));
        editor=sharedPreferences.edit();
        editor.putInt(SHARED_PREF_NEXT_PREKEYID, returningValue);
        editor.apply();
        return returningValue;
    }

    //access to this method needs to be synchronized between threads
    public synchronized int getNextPreKeyId(){
        int currentValue=sharedPreferences.getInt(SHARED_PREF_NEXT_PREKEYID, new SecureRandom().nextInt(Integer.MAX_VALUE));
        editor=sharedPreferences.edit();
        int returningValue=(currentValue+1)%Integer.MAX_VALUE;
        editor.putInt(SHARED_PREF_NEXT_PREKEYID, returningValue);
        editor.apply();
        return returningValue;
    }

    public int getCurrentSignedPreKeyId(){
        int returningValue=sharedPreferences.getInt(SHARED_PREF_SIGNEDPREKEYID, new SecureRandom().nextInt(Integer.MAX_VALUE));
        editor=sharedPreferences.edit();
        editor.putInt(SHARED_PREF_SIGNEDPREKEYID, returningValue);
        editor.apply();
        return returningValue;
    }

    public int getSignedNextPreKeyId(){
        int currentValue=sharedPreferences.getInt(SHARED_PREF_SIGNEDPREKEYID, new SecureRandom().nextInt(Integer.MAX_VALUE));
        editor=sharedPreferences.edit();
        int returningValue=(currentValue+1)%Integer.MAX_VALUE;
        editor.putInt(SHARED_PREF_SIGNEDPREKEYID, returningValue);
        editor.apply();
        return returningValue;
    }

    public boolean isFirstTimeUse(){
        return sharedPreferences.getBoolean(FIRST_TIME_USE, false);
    }

    public void setFirstTimeUse(boolean result){
        editor=sharedPreferences.edit();
        editor.putBoolean(FIRST_TIME_USE, result);
        editor.apply();
    }

    //returns null if not exists
    public String getMyOnionAddress(){
        return sharedPreferences.getString(MY_ONION_ADDRESS, null);
    }

    public void setMyOnionAddress(String onionAddress){
        editor=sharedPreferences.edit();
        editor.putString(MY_ONION_ADDRESS, onionAddress);
        editor.apply();
    }
}

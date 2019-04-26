package io.github.nandandesai.peerlink.core;

import android.content.Context;
import android.util.Log;

import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.ecc.Curve;
import org.whispersystems.libsignal.ecc.ECKeyPair;
import org.whispersystems.libsignal.ecc.ECPublicKey;
import org.whispersystems.libsignal.state.PreKeyBundle;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.SignalProtocolStore;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.util.KeyHelper;

import java.io.IOException;

import io.github.nandandesai.peerlink.utils.PeerLinkPreferences;
import io.github.nandandesai.peerlink.utils.SetupHelper;


public class PeerLinkKeyManager {
    private static final String TAG = "PeerLinkKeyManager";
    private SignalProtocolStore store;
    private static final int myDeviceId=1;
    private PeerLinkPreferences preferences;
    private SetupHelper setupHelper;
    public PeerLinkKeyManager(Context context, SignalProtocolStore store) {
        this.store = store;
        this.preferences=new PeerLinkPreferences(context);
        setupHelper=new SetupHelper(context);
    }

    private IdentityKeyPair generateMyIdentityKeys(){
        return KeyHelper.generateIdentityKeyPair();
    }

    private int generateMyRegistrationId(){
        return KeyHelper.generateRegistrationId(false);
    }

    private ECKeyPair generateMyOneTimePreKey(){
        return Curve.generateKeyPair();
    }

    private SignedPreKeyRecord generateMySignedPreKey(int signedPreKeyId, IdentityKeyPair identityKeyPair) throws InvalidKeyException {
        return KeyHelper.generateSignedPreKey(identityKeyPair, signedPreKeyId);
    }

    private void storeNewOneTimePreKeys(){
        int preKeyId=preferences.getNextPreKeyId();
        ECKeyPair keyPair=generateMyOneTimePreKey();
        PreKeyRecord preKeyRecord=new PreKeyRecord(preKeyId, keyPair);
        store.storePreKey(preKeyId, preKeyRecord);
    }

    public void firstTimeInitialization() throws InvalidKeyException {
        IdentityKeyPair identityKeyPair=null;
        if(!setupHelper.identityKeyDone()) {
            //generate Identity Keys and Registration Id
            identityKeyPair = generateMyIdentityKeys();
            preferences.storeMyIdentityKeyPair(identityKeyPair);
            Log.d(TAG, "firstTimeInitialization: Identity Key pair generated and stored.");
        }

        if(!setupHelper.registrationIdDone()) {
            int registrationId = generateMyRegistrationId();
            preferences.storeMyRegistrationId(registrationId);
            Log.d(TAG, "firstTimeInitialization: registration id has been generated and stored");
        }


        if(identityKeyPair!=null) {
            //One-Time PreKey
            int preKeyId = preferences.getCurrentPreKeyId();
            ECKeyPair keyPair = generateMyOneTimePreKey();
            PreKeyRecord preKeyRecord = new PreKeyRecord(preKeyId, keyPair);
            store.storePreKey(preKeyId, preKeyRecord);
            Log.d(TAG, "firstTimeInitialization: PreKey is generated and stored");

            //SignedPreKey
            int signedPreKeyId = preferences.getCurrentSignedPreKeyId();
            SignedPreKeyRecord signedPreKeyRecord = generateMySignedPreKey(signedPreKeyId, identityKeyPair);
            store.storeSignedPreKey(signedPreKeyId, signedPreKeyRecord);
            Log.d(TAG, "firstTimeInitialization: Signed Prekey is generated and stored");
        }
        /*
            As far as I understand,

            The IdentityKey table in database is for storing others' identity public keys.
            The SignedPreKey and PreKey tables in the database is for storing my keys.
            But, I don't know why, my identity keys (public and private) are stored in shared preferences rather than in the database.
            I did this way because SignalAndroid did it this way.

            The public signedPreKey and the public preKey of others ("preKeyBundle") is stored in the Session table in the database.
        */
    }

    public PreKeyBundle getMyPreKeyBundle() throws InvalidKeyIdException, IOException, InvalidKeyException {
        int registrationId=preferences.getMyRegistrationId();

        int preKeyId=preferences.getCurrentPreKeyId();
        ECPublicKey preKeyPublic=store.loadPreKey(preKeyId).getKeyPair().getPublicKey();

        //this method will generate new set of OneTimePreKeys and store it in the database and that will be used in constructing the next PreKeyBundle
        //this step is done because, as I understand, we shouldn't use the same OneTimePreKeys for all the recipient requests
        storeNewOneTimePreKeys();

        int signedPreKeyId=preferences.getCurrentSignedPreKeyId();
        ECPublicKey signedPreKeyPublic=store.loadSignedPreKey(signedPreKeyId).getKeyPair().getPublicKey();
        byte[] signature=store.loadSignedPreKey(signedPreKeyId).getSignature();

        IdentityKey identityKey=preferences.getMyIdentityKeyPair().getPublicKey();

        return new PreKeyBundle(registrationId, myDeviceId, preKeyId, preKeyPublic, signedPreKeyId, signedPreKeyPublic, signature, identityKey);
    }


}

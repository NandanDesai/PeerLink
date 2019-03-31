package io.github.nandandesai.peerlink.repositories;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.whispersystems.libsignal.*;
import org.whispersystems.libsignal.ecc.Curve;
import org.whispersystems.libsignal.ecc.ECKeyPair;
import org.whispersystems.libsignal.ecc.ECPrivateKey;
import org.whispersystems.libsignal.ecc.ECPublicKey;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.SessionRecord;
import org.whispersystems.libsignal.state.SignalProtocolStore;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.github.nandandesai.peerlinkcomm.utils.Base64;
import io.github.nandandesai.peerlink.database.IdentityKeyDao;
import io.github.nandandesai.peerlink.database.PeerLinkDatabase;
import io.github.nandandesai.peerlink.database.PreKeyDao;
import io.github.nandandesai.peerlink.database.SessionDao;
import io.github.nandandesai.peerlink.database.SignedPreKeyDao;
import io.github.nandandesai.peerlink.models.PeerLinkIdentityKey;
import io.github.nandandesai.peerlink.models.PeerLinkPreKey;
import io.github.nandandesai.peerlink.models.PeerLinkSession;
import io.github.nandandesai.peerlink.models.PeerLinkSignedPreKey;
import io.github.nandandesai.peerlinkcomm.utils.PeerLinkPreferences;

public class PeerLinkKeyRepository implements SignalProtocolStore {

    private static final String TAG = "PeerLinkKeyRepository";
    private IdentityKeyDao identityKeyStoreDao;
    private PreKeyDao preKeyStoreDao;
    private SessionDao sessionStoreDao;
    private SignedPreKeyDao signedPreKeyDao;
    private PeerLinkPreferences preferences;
    public PeerLinkKeyRepository(Application application) {
        PeerLinkDatabase peerLinkDatabase = PeerLinkDatabase.getInstance(application);
        identityKeyStoreDao = peerLinkDatabase.identityKeyStoreDao();
        preKeyStoreDao = peerLinkDatabase.preKeyStoreDao();
        sessionStoreDao = peerLinkDatabase.sessionStoreDao();
        signedPreKeyDao = peerLinkDatabase.signedPreKeyStoreDao();
        preferences=new PeerLinkPreferences(application);
    }

    @Override
    public IdentityKeyPair getIdentityKeyPair() {
        try {
            return preferences.getMyIdentityKeyPair();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getLocalRegistrationId() {
        return preferences.getMyRegistrationId();
    }

    @Override
    public boolean saveIdentity(SignalProtocolAddress address, IdentityKey identityKey) {
        String addressName = address.getName();
        String identityKeyString = Base64.encodeBytes(identityKey.serialize());

        PeerLinkIdentityKey identityKeyStore = new PeerLinkIdentityKey(addressName, identityKeyString);
        identityKeyStoreDao.insert(identityKeyStore);

        return true;
    }

    @Override
    public boolean isTrustedIdentity(SignalProtocolAddress address, IdentityKey identityKey, Direction direction) {

        /*
            Not sure what this method is for but you can refer this file in the original repo
            TextSecureIdentityKeyStore.java

            AFAIK, this method decides whether the other party who wants to communicate with us can be trusted or not.
            I think, if his/her identity is saved in our contacts, then that person is trusted. Otherwise, that person cannot be trusted.
            Atleast that is what I assume as of now.

        */
        return true;
    }

    @Override
    public IdentityKey getIdentity(SignalProtocolAddress address) {
        String addressName = address.getName();
        String serializedIdentity = identityKeyStoreDao.getIdentityKey(addressName).getIdentityKey();
        IdentityKey identityKey = null;
        try {
            identityKey = new IdentityKey(Base64.decode(serializedIdentity), 0);
        } catch (InvalidKeyException | IOException e) {
            e.printStackTrace();
        }
        return identityKey;
    }

    @Override
    public PreKeyRecord loadPreKey(int preKeyId) {
        try {
            String preKeyPublic = preKeyStoreDao.getPreKeyStore(preKeyId).getPublicKey();
            String preKeyPrivate = preKeyStoreDao.getPreKeyStore(preKeyId).getPrivateKey();

            ECPublicKey publicKey = Curve.decodePoint(Base64.decode(preKeyPublic), 0);
            ECPrivateKey privateKey = Curve.decodePrivatePoint(Base64.decode(preKeyPrivate));

            return new PreKeyRecord(preKeyId, new ECKeyPair(publicKey, privateKey));


        } catch (InvalidKeyException e) {
            Log.d(TAG, "loadPreKey: Invalid Key: " + e.getMessage());
        } catch (IOException io) {
            Log.d(TAG, "loadPreKey: IO Exception: " + io.getMessage());
        }
        return null;
    }

    @Override
    public void storePreKey(int preKeyId, PreKeyRecord record) {
        String publicKey = Base64.encodeBytes(record.getKeyPair().getPublicKey().serialize());
        String privateKey = Base64.encodeBytes(record.getKeyPair().getPrivateKey().serialize());

        PeerLinkPreKey preKeyStore = new PeerLinkPreKey(preKeyId, publicKey, privateKey);
        preKeyStoreDao.insert(preKeyStore);
    }

    @Override
    public boolean containsPreKey(int preKeyId) {
        return loadPreKey(preKeyId) != null;
    }

    @Override
    public void removePreKey(int preKeyId) {
        preKeyStoreDao.delete(preKeyId);
    }

    @Override
    public SessionRecord loadSession(SignalProtocolAddress address) {
        try {
            String addressName = address.getName();
            int deviceId = address.getDeviceId();
            byte[] serializedSession = sessionStoreDao.getSessionStore(addressName, deviceId).getSerializedRecord();
            return new SessionRecord(serializedSession);
        } catch (IOException io) {
            Log.d(TAG, "loadSession: IO Exception : " + io.getMessage());
            return null;
        }
    }

    @Override
    public List<Integer> getSubDeviceSessions(String name) {
        return sessionStoreDao.getSubDeviceSessions(name);
    }

    @Override
    public void storeSession(SignalProtocolAddress address, SessionRecord record) {
        String addressName = address.getName();
        int deviceId = address.getDeviceId();
        byte[] serializedRecord = record.serialize();

        PeerLinkSession sessionStore = new PeerLinkSession(addressName, deviceId, serializedRecord);
        sessionStoreDao.insert(sessionStore);
    }

    @Override
    public boolean containsSession(SignalProtocolAddress address) {
        return loadSession(address) != null;
    }

    @Override
    public void deleteSession(SignalProtocolAddress address) {
        String addressName = address.getName();
        int deviceId = address.getDeviceId();
        sessionStoreDao.deleteSession(addressName, deviceId);
    }

    @Override
    public void deleteAllSessions(String name) {
        sessionStoreDao.deleteAllSessions(name);
    }

    @Override
    public SignedPreKeyRecord loadSignedPreKey(int signedPreKeyId) {

        PeerLinkSignedPreKey signedPreKey = signedPreKeyDao.getSignedPreKey(signedPreKeyId);

        try {
            ECPublicKey publicKey = Curve.decodePoint(Base64.decode(signedPreKey.getPublicKey()), 0);
            ECPrivateKey privateKey = Curve.decodePrivatePoint(Base64.decode(signedPreKey.getPrivateKey()));
            byte[] signature = Base64.decode(signedPreKey.getSignature());
            long timestamp = signedPreKey.getTimestamp();
            return new SignedPreKeyRecord(signedPreKeyId, timestamp, new ECKeyPair(publicKey, privateKey), signature);
        } catch (IOException io) {
            Log.d(TAG, "loadSignedPreKey: IO Exception: " + io.getMessage());
        } catch (InvalidKeyException e) {
            Log.d(TAG, "loadSignedPreKey: InvalidKeyException: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<SignedPreKeyRecord> loadSignedPreKeys() {
        List<PeerLinkSignedPreKey> signedPreKeys = signedPreKeyDao.getAllSignedPreKey();
        Iterator it = signedPreKeys.iterator();
        ArrayList<SignedPreKeyRecord> signedPreKeyRecords = new ArrayList<>();
        PeerLinkSignedPreKey peerLinkSignedPreKey = null;
        ECPublicKey publicKey = null;
        ECPrivateKey privateKey = null;
        byte[] signature = null;
        while (it.hasNext()) {
            try {
                peerLinkSignedPreKey = (PeerLinkSignedPreKey) it.next();
                publicKey = Curve.decodePoint(Base64.decode(peerLinkSignedPreKey.getPublicKey()), 0);
                privateKey = Curve.decodePrivatePoint(Base64.decode(peerLinkSignedPreKey.getPrivateKey()));
                signature = Base64.decode(peerLinkSignedPreKey.getSignature());
                signedPreKeyRecords.add(new SignedPreKeyRecord(peerLinkSignedPreKey.getSignedPreKeyId(), peerLinkSignedPreKey.getTimestamp(), new ECKeyPair(publicKey, privateKey), signature));
            } catch (IOException io) {
                Log.d(TAG, "loadSignedPreKeys: IO Exception : " + io.getMessage());
            } catch (InvalidKeyException e) {
                Log.d(TAG, "loadSignedPreKeys: InvalidKeyException : " + e.getMessage());
            }
        }
        return signedPreKeyRecords;
    }

    @Override
    public void storeSignedPreKey(int signedPreKeyId, SignedPreKeyRecord record) {
        String publicKey = Base64.encodeBytes(record.getKeyPair().getPublicKey().serialize());
        String privateKey = Base64.encodeBytes(record.getKeyPair().getPrivateKey().serialize());
        String signature = Base64.encodeBytes(record.getSignature());
        long timestamp = record.getTimestamp();
        PeerLinkSignedPreKey signedPreKey = new PeerLinkSignedPreKey(signedPreKeyId, publicKey, privateKey, signature, timestamp);
        signedPreKeyDao.insert(signedPreKey);
    }

    @Override
    public boolean containsSignedPreKey(int signedPreKeyId) {
        return loadSignedPreKey(signedPreKeyId)!=null;
    }

    @Override
    public void removeSignedPreKey(int signedPreKeyId) {
        signedPreKeyDao.delete(signedPreKeyId);
    }
}


package io.github.nandandesai.peerlink.models;

//this class is used to store the public signed pre keys of people I'm chatting with.


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class PeerLinkSignedPreKey {

    @NonNull @PrimaryKey
    private int signedPreKeyId;
    private String publicKey;
    private String privateKey;
    private String signature;
    private long timestamp;

    public PeerLinkSignedPreKey(int signedPreKeyId, String publicKey, String privateKey, String signature, long timestamp) {
        this.signedPreKeyId = signedPreKeyId;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.signature = signature;
        this.timestamp = timestamp;
    }

    public int getSignedPreKeyId() {
        return signedPreKeyId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getSignature() {
        return signature;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

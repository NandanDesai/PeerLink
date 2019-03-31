package io.github.nandandesai.peerlink.models;

//this class is used to store the public pre keys of people I'm chatting with.

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class PeerLinkPreKey {

    @PrimaryKey @NonNull
    private int preKeyId;
    private String publicKey;
    private String privateKey;

    public PeerLinkPreKey(int preKeyId, String publicKey, String privateKey) {
        this.preKeyId = preKeyId;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public int getPreKeyId() {
        return preKeyId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }
}

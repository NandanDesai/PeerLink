package io.github.nandandesai.peerlink.models;

//this class is used to store the public identity keys of people I'm chatting with.

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class PeerLinkIdentityKeyStore {

    @NonNull @PrimaryKey
    private String address;
    private String identityKey;

    public PeerLinkIdentityKeyStore(String address, String identityKey) {
        this.address = address;
        this.identityKey = identityKey;
    }

    public String getAddress() {
        return address;
    }

    public String getIdentityKey() {
        return identityKey;
    }
}

package io.github.nandandesai.peerlink.models;

//this class will be used to store the session information. To be specific, this class will
//be used to store the serialized form of SessionRecord object obtained from Signal Protocol Library.


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class PeerLinkSessionStore {

    @NonNull @PrimaryKey
    private String address;
    private int deviceId;
    private byte[] serializedRecord;

    public PeerLinkSessionStore(String address, int deviceId, byte[] serializedRecord) {
        this.address = address;
        this.deviceId = deviceId;
        this.serializedRecord = serializedRecord;
    }

    public String getAddress() {
        return address;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public byte[] getSerializedRecord() {
        return serializedRecord;
    }
}

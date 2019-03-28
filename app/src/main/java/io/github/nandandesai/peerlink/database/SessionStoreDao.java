package io.github.nandandesai.peerlink.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.github.nandandesai.peerlink.models.PeerLinkSessionStore;

@Dao
public interface SessionStoreDao {

    @Insert
    void insert(PeerLinkSessionStore sessionStore);

    @Query("DELETE FROM PeerLinkSessionStore WHERE address=:address AND deviceId=:deviceId")
    void deleteSession(String address, int deviceId);

    @Query("DELETE FROM PeerLinkSessionStore WHERE address=:address")
    void deleteAllSessions(String address);

    @Query("SELECT deviceId FROM PeerLinkSessionStore WHERE address=:address")
    List<Integer> getSubDeviceSessions(String address);

    @Query("SELECT * FROM PeerLinkSessionStore WHERE address=:address AND deviceId=:deviceId")
    PeerLinkSessionStore getSession(String address, int deviceId);
}

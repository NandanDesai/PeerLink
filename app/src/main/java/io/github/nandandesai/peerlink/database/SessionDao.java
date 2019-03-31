package io.github.nandandesai.peerlink.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.github.nandandesai.peerlink.models.PeerLinkSession;

@Dao
public interface SessionDao {

    @Insert
    void insert(PeerLinkSession sessionStore);

    @Query("DELETE FROM PeerLinkSession WHERE address=:address AND deviceId=:deviceId")
    void deleteSession(String address, int deviceId);

    @Query("DELETE FROM PeerLinkSession WHERE address=:address")
    void deleteAllSessions(String address);

    @Query("SELECT deviceId FROM PeerLinkSession WHERE address=:address")
    List<Integer> getSubDeviceSessions(String address);

    @Query("SELECT * FROM PeerLinkSession WHERE address=:address AND deviceId=:deviceId")
    PeerLinkSession getSessionStore(String address, int deviceId);
}

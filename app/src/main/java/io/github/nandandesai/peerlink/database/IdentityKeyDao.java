package io.github.nandandesai.peerlink.database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import io.github.nandandesai.peerlink.models.PeerLinkIdentityKey;

@Dao
public interface IdentityKeyDao {
    @Insert
    void insert(PeerLinkIdentityKey identityKeyStore);

    @Query("SELECT * FROM PeerLinkIdentityKey WHERE address=:address")
    PeerLinkIdentityKey getIdentityKey(String address);

    @Query("DELETE FROM PeerLinkIdentityKey WHERE address=:address")
    void delete(String address);

    @Query("DELETE FROM PeerLinkIdentityKey")
    void deleteAll();
}

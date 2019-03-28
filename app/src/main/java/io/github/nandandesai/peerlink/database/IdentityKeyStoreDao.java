package io.github.nandandesai.peerlink.database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import io.github.nandandesai.peerlink.models.PeerLinkIdentityKeyStore;

@Dao
public interface IdentityKeyStoreDao {
    @Insert
    void insert(PeerLinkIdentityKeyStore identityKeyStore);

    @Query("SELECT * FROM PeerLinkIdentityKeyStore WHERE address=:address")
    PeerLinkIdentityKeyStore getIdentityKey(String address);

    @Query("DELETE FROM PeerLinkIdentityKeyStore WHERE address=:address")
    void delete(String address);

    @Query("DELETE FROM PeerLinkIdentityKeyStore")
    void deleteAll();
}

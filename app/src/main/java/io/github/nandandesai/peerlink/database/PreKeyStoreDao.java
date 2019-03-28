package io.github.nandandesai.peerlink.database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import io.github.nandandesai.peerlink.models.PeerLinkPreKeyStore;

@Dao
public interface PreKeyStoreDao {
    @Insert
    void insert(PeerLinkPreKeyStore preKeyStore);

    @Query("DELETE FROM PeerLinkPreKeyStore WHERE preKeyId=:preKeyId")
    void delete(int preKeyId);


    @Query("SELECT * FROM PeerLinkPreKeyStore WHERE preKeyId=:preKeyId")
    PeerLinkPreKeyStore getPreKey(int preKeyId);

}

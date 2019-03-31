package io.github.nandandesai.peerlink.database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import io.github.nandandesai.peerlink.models.PeerLinkPreKey;

@Dao
public interface PreKeyDao {
    @Insert
    void insert(PeerLinkPreKey preKeyStore);

    @Query("DELETE FROM PeerLinkPreKey WHERE preKeyId=:preKeyId")
    void delete(int preKeyId);


    @Query("SELECT * FROM PeerLinkPreKey WHERE preKeyId=:preKeyId")
    PeerLinkPreKey getPreKeyStore(int preKeyId);

}

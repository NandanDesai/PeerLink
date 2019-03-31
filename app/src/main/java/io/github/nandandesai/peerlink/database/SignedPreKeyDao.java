package io.github.nandandesai.peerlink.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.github.nandandesai.peerlink.models.PeerLinkSignedPreKey;

@Dao
public interface SignedPreKeyDao {

    @Insert
    void insert(PeerLinkSignedPreKey signedPreKeyStore);

    @Query("SELECT * FROM PeerLinkSignedPreKey WHERE signedPreKeyId=:signedPreKeyId")
    PeerLinkSignedPreKey getSignedPreKey(int signedPreKeyId);

    @Query("SELECT * FROM PeerLinkSignedPreKey")
    List<PeerLinkSignedPreKey> getAllSignedPreKey();

    @Query("DELETE FROM PeerLinkSignedPreKey WHERE signedPreKeyId=:signedPreKeyId")
    void delete(int signedPreKeyId);

}

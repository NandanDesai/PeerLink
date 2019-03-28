package io.github.nandandesai.peerlink.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.github.nandandesai.peerlink.models.PeerLinkSignedPreKeyStore;

@Dao
public interface SignedPreKeyStoreDao {

    @Insert
    void insert(PeerLinkSignedPreKeyStore signedPreKeyStore);

    @Query("SELECT * FROM PeerLinkSignedPreKeyStore WHERE signedPreKeyId=:signedPreKeyId")
    PeerLinkSignedPreKeyStore getSignedPreKey(int signedPreKeyId);

    @Query("SELECT * FROM PeerLinkSignedPreKeyStore")
    List<PeerLinkSignedPreKeyStore> getAllSignedPreKey();

    @Query("DELETE FROM PeerLinkSignedPreKeyStore WHERE signedPreKeyId=:signedPreKeyId")
    void delete(int signedPreKeyId);

}

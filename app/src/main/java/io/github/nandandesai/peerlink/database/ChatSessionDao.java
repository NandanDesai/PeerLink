package io.github.nandandesai.peerlink.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.github.nandandesai.peerlink.models.ChatSession;

@Dao
public interface ChatSessionDao {
    @Insert
    void insert(ChatSession chatSession);

    @Query("SELECT * FROM ChatSession ORDER BY lastUpdated ASC")
    LiveData<List<ChatSession>> getAllChats();

    @Query("UPDATE ChatSession SET lastUpdated=:lastUpdated, lastMessage=:lastMessage, noOfUnreadMessages=:noOfUnreadMsgs WHERE chatId=:chatId")
    void updateLastUpdated(String chatId, long lastUpdated, String lastMessage, int noOfUnreadMsgs);

    @Query("DELETE FROM ChatSession WHERE chatId=:chatId")
    void delete(String chatId);
}

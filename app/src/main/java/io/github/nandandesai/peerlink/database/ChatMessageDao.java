package io.github.nandandesai.peerlink.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.github.nandandesai.peerlink.models.ChatMessage;

@Dao
public interface ChatMessageDao {

    @Insert
    void insert(ChatMessage chatMessage);

    @Query("SELECT * FROM ChatMessage ORDER BY messageId ASC")
    LiveData<List<ChatMessage>> getAllChatMessages();

    @Query("UPDATE ChatMessage SET messageStatus=:status WHERE messageId=:messageId")
    void updateMessageStatus(int messageId, String status);
}

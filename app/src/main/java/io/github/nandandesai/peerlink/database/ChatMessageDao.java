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

    @Query("SELECT * FROM ChatMessage WHERE chatId=:chatId ORDER BY messageTime ASC")
    LiveData<List<ChatMessage>> getAllChatMessages(String chatId);

    @Query("UPDATE ChatMessage SET messageStatus=:status WHERE messageId=:messageId")
    void updateMessageStatus(int messageId, String status);
}

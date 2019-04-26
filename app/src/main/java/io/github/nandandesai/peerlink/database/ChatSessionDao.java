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

    @Query("SELECT * FROM ChatSession")
    LiveData<List<ChatSession>> getAllChats();

    @Query("SELECT * FROM ChatSession WHERE chatId=:chatId")
    LiveData<ChatSession> getChatSession(String chatId);

    @Query("DELETE FROM ChatSession WHERE chatId=:chatId")
    void delete(String chatId);

    @Query("SELECT chatId FROM ChatSession")
    LiveData<List<String>> getAllChatIds();
}

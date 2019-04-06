package io.github.nandandesai.peerlink.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.github.nandandesai.peerlink.models.ChatMessage;
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

    @Query("SELECT Count(*) FROM ChatMessage WHERE messageStatus='"+ChatMessage.STATUS.USER_NOT_READ+"' AND chatId=:chatId")
    LiveData<Integer> getNumberOfUnreadMsgs(String chatId);

    @Query("SELECT messageContent FROM ChatMessage WHERE chatId=:chatId AND messageTime=(SELECT Max(messageTime) FROM ChatMessage)")
    LiveData<String> getRecentMsg(String chatId);

    @Query("SELECT icon FROM ChatSession WHERE chatId=:chatId")
    LiveData<String> getIcon(String chatId);

    @Query("SELECT name FROM ChatSession WHERE chatId=:chatId")
    LiveData<String> getName(String chatId);

    @Query("SELECT chatId FROM ChatSession")
    LiveData<List<String>> getAllChatIds();
}

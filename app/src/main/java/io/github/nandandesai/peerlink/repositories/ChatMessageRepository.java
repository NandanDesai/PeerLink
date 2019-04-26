package io.github.nandandesai.peerlink.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import java.util.List;

import io.github.nandandesai.peerlink.database.ChatMessageDao;
import io.github.nandandesai.peerlink.database.PeerLinkDatabase;
import io.github.nandandesai.peerlink.models.ChatMessage;

public class ChatMessageRepository {
    private ChatMessageDao chatMessageDao;

    public ChatMessageRepository(Application application){
        PeerLinkDatabase peerLinkDatabase=PeerLinkDatabase.getInstance(application);
        chatMessageDao=peerLinkDatabase.chatMessageDao();
    }

    public void insert(ChatMessage chatMessage){
        new Thread(new Runnable() {
            @Override
            public void run() {
                chatMessageDao.insert(chatMessage);
            }
        }).start();
    }

    public LiveData<List<ChatMessage>> getChatMessages(String chatId){
        return chatMessageDao.getAllChatMessages(chatId);
    }

    public LiveData<List<ChatMessage>> getAllUnreadMsgs(String chatId){
        return chatMessageDao.getAllUnreadMsgs(chatId);
    }

    public LiveData<List<ChatMessage>> getAllUnsentMsgs(){
        return chatMessageDao.getAllUnsentMsgs();
    }

    public void updateMessageStatusWithChatId(String chatId, String fromStatus, String toStatus){
        new Thread(new Runnable() {
            @Override
            public void run() {
                chatMessageDao.updateMessageStatusWithChatId(chatId, fromStatus, toStatus);
            }
        }).start();
    }

    public void updateMessageStatusWithMessageId(int messageId, String status){
        new Thread(new Runnable() {
            @Override
            public void run() {
                chatMessageDao.updateMessageStatusWithMessageId(messageId, status);
            }
        }).start();
    }
}

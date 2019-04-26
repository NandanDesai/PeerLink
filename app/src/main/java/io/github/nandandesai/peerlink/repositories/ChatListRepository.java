package io.github.nandandesai.peerlink.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import java.util.List;

import io.github.nandandesai.peerlink.database.ChatMessageDao;
import io.github.nandandesai.peerlink.database.ChatSessionDao;
import io.github.nandandesai.peerlink.database.PeerLinkDatabase;
import io.github.nandandesai.peerlink.models.ChatSession;


public class ChatListRepository {
    private static final String TAG = "ChatListRepository";
    private ChatSessionDao chatSessionDao;
    private LiveData<List<ChatSession>> chats;
    private ChatMessageDao chatMessageDao;

    public ChatListRepository(Application application){
        PeerLinkDatabase peerLinkDatabase=PeerLinkDatabase.getInstance(application);
        chatSessionDao =peerLinkDatabase.chatSessionDao();
        chatMessageDao=peerLinkDatabase.chatMessageDao();
        chats= chatSessionDao.getAllChats();
    }

    public LiveData<List<ChatSession>> getChats() {
        return chats;
    }

    public LiveData<Integer> getNumberOfUnreadMsgs(String chatId){
        return chatMessageDao.getNumberOfUnreadMsgs(chatId);
    }

    public LiveData<String> getRecentMsg(String chatId){
        return chatMessageDao.getRecentMsg(chatId);
    }

    public LiveData<List<String>> getAllChatIds(){
        return chatSessionDao.getAllChatIds();
    }

    public void insertChat(ChatSession chatSession){
        new Thread(new Runnable() {
            @Override
            public void run() {
                chatSessionDao.insert(chatSession);
            }
        }).start();
    }

    public LiveData<ChatSession> getChatSession(String chatId){
        return chatSessionDao.getChatSession(chatId);
    }

    public void deleteChat(String chatId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                chatSessionDao.delete(chatId);
            }
        }).start();
    }
}

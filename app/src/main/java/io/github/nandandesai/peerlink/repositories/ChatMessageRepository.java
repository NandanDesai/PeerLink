package io.github.nandandesai.peerlink.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.List;

import io.github.nandandesai.peerlink.database.ChatMessageDao;
import io.github.nandandesai.peerlink.database.PeerLinkDatabase;
import io.github.nandandesai.peerlink.models.ChatMessage;
import io.github.nandandesai.peerlink.models.ChatSession;
import io.github.nandandesai.peerlink.models.PeerLinkSession;
import io.github.nandandesai.peerlink.utils.PeerLinkPreferences;

public class ChatMessageRepository {
    private static final String TAG = "ChatMessageRepository";
    private ChatMessageDao chatMessageDao;
    private PeerLinkDatabase peerLinkDatabase;
    private Application application;
    public ChatMessageRepository(Application application){
        this.application=application;
        peerLinkDatabase=PeerLinkDatabase.getInstance(application);
        chatMessageDao=peerLinkDatabase.chatMessageDao();
    }

    public void insert(ChatMessage chatMessage){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //if chat session exists
                if(peerLinkDatabase.chatSessionDao().chatSessionExists(chatMessage.getChatId())==1) {
                    chatMessageDao.insert(chatMessage);
                }else{
                    //THIS IS TEMPORARY
                    //if chat session doesn't exists, create a new one.
                    //in actual case, this would involve key exchanges and stuff for Signal protocol.
                    //for time being, I'm just going to create some temporary rows for it.
                    Log.d(TAG, "run: Chat session didn't exist previously. So, creating a new one before inserting the message.");

                    PeerLinkPreferences preferences=new PeerLinkPreferences(application.getApplicationContext());
                    if(chatMessage.getMessageFrom()==preferences.getMyOnionAddress()) {
                        String contactName=peerLinkDatabase.contactDao().getContactName(chatMessage.getChatId());
                        String chatName;
                        if(contactName!=null){
                            chatName=contactName;
                        }else{
                            chatName=chatMessage.getChatId();
                        }
                        peerLinkDatabase.sessionStoreDao().insert(new PeerLinkSession(chatMessage.getChatId(), 1, null));
                        peerLinkDatabase.chatSessionDao().insert(new ChatSession(chatMessage.getChatId(), chatName, ChatSession.TYPE.DIRECT, "https://images.unsplash.com/photo-1529665253569-6d01c0eaf7b6?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80"));
                    }else{
                        String contactName=peerLinkDatabase.contactDao().getContactName(chatMessage.getMessageFrom());
                        String chatName;
                        if(contactName!=null){
                            chatName=contactName;
                        }else{
                            chatName=chatMessage.getMessageFrom();
                        }
                        chatMessage.setChatId(chatMessage.getMessageFrom());
                        peerLinkDatabase.sessionStoreDao().insert(new PeerLinkSession(chatMessage.getMessageFrom(), 1, null));
                        peerLinkDatabase.chatSessionDao().insert(new ChatSession(chatMessage.getMessageFrom(), chatName, ChatSession.TYPE.DIRECT, "https://images.unsplash.com/photo-1529665253569-6d01c0eaf7b6?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80"));

                    }
                    chatMessageDao.insert(chatMessage);
                }
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

    public void updateMessageStatusWithMessageId(String messageId, String status){
        new Thread(new Runnable() {
            @Override
            public void run() {
                chatMessageDao.updateMessageStatusWithMessageId(messageId, status);
            }
        }).start();
    }
}

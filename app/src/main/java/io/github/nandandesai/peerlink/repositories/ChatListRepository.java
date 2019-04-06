package io.github.nandandesai.peerlink.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import io.github.nandandesai.peerlink.database.ChatSessionDao;
import io.github.nandandesai.peerlink.database.PeerLinkDatabase;
import io.github.nandandesai.peerlink.models.ChatSession;


public class ChatListRepository {
    private static final String TAG = "ChatListRepository";
    private ChatSessionDao chatSessionDao;
    private LiveData<List<ChatSession>> chats;

    public ChatListRepository(Application application){
        PeerLinkDatabase peerLinkDatabase=PeerLinkDatabase.getInstance(application);
        chatSessionDao =peerLinkDatabase.chatSessionDao();
        chats= chatSessionDao.getAllChats();
    }

    public LiveData<List<ChatSession>> getChats() {
        return chats;
    }

    ///////////////
    public LiveData<Integer> getNumberOfUnreadMsgs(String chatId){
        return chatSessionDao.getNumberOfUnreadMsgs(chatId);
    }

    public LiveData<String> getRecentMsg(String chatId){
        return chatSessionDao.getRecentMsg(chatId);
    }

    public LiveData<String> getIcon(String chatId){
        return chatSessionDao.getIcon(chatId);
    }

    public LiveData<String> getName(String chatId){
        return chatSessionDao.getName(chatId);
    }

    public LiveData<List<String>> getAllChatIds(){
        return chatSessionDao.getAllChatIds();
    }
///////////////////////


    public void insertChat(ChatSession chatSession){
        new InsertChat(chatSessionDao).execute(chatSession);
    }


    public LiveData<ChatSession> getChatSession(String chatId){
        return chatSessionDao.getChatSession(chatId);
    }

    public void deleteChat(String chatId){
        new DeleteChat(chatSessionDao).execute(chatId);
    }


    private static class InsertChat extends AsyncTask<ChatSession, Void, Void>{
        private ChatSessionDao chatSessionDao;

        InsertChat(ChatSessionDao chatSessionDao) {
            this.chatSessionDao = chatSessionDao;
        }

        @Override
        protected Void doInBackground(ChatSession... chatSessions) {
            chatSessionDao.insert(chatSessions[0]);
            return null;
        }
    }



    private static class DeleteChat extends AsyncTask<String, Void, Void>{

        private ChatSessionDao chatSessionDao;

        DeleteChat(ChatSessionDao chatSessionDao) {
            this.chatSessionDao = chatSessionDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            chatSessionDao.delete(strings[0]);
            return null;
        }
    }


}

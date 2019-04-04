package io.github.nandandesai.peerlink.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import io.github.nandandesai.peerlink.database.ChatSessionDao;
import io.github.nandandesai.peerlink.database.PeerLinkDatabase;
import io.github.nandandesai.peerlink.models.ChatSession;


public class ChatRepository {
    private static final String TAG = "ChatRepository";
    private ChatSessionDao chatSessionDao;
    private LiveData<List<ChatSession>> chats;

    public ChatRepository(Application application){
        PeerLinkDatabase peerLinkDatabase=PeerLinkDatabase.getInstance(application);
        chatSessionDao =peerLinkDatabase.chatSessionDao();
        chats= chatSessionDao.getAllChats();
    }

    public LiveData<List<ChatSession>> getChats() {
        return chats;
    }

    public void insertChat(ChatSession chatSession){
        new InsertChat(chatSessionDao).execute(chatSession);
    }

    public void updateChat(String chatId, long time, String lastMessage){
        new UpdateChat(chatSessionDao).execute(chatId,time,lastMessage);
    }

    public ChatSession getChatSession(String chatId){
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

    private static class UpdateChat extends AsyncTask<Object, Void, Void>{

        private ChatSessionDao chatSessionDao;

        UpdateChat(ChatSessionDao chatSessionDao) {
            this.chatSessionDao = chatSessionDao;
        }

        @Override
        protected Void doInBackground(Object... objects) {
            int currentUnread=chatSessionDao.getNoOfUnreadMsgs((String) objects[0]);
            int updatedUnread=currentUnread+1;
            Log.d(TAG, "doInBackground: Total Unread = "+updatedUnread);
            chatSessionDao.update((String) objects[0], (Long)objects[1], (String)objects[2],updatedUnread);
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

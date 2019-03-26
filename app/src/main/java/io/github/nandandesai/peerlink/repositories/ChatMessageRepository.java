package io.github.nandandesai.peerlink.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import io.github.nandandesai.peerlink.database.ChatMessageDao;
import io.github.nandandesai.peerlink.database.PeerLinkDatabase;
import io.github.nandandesai.peerlink.models.ChatMessage;

public class ChatMessageRepository {
    private ChatMessageDao chatMessageDao;
    private LiveData<List<ChatMessage>> chatMessages;

    public ChatMessageRepository(Application application){
        PeerLinkDatabase peerLinkDatabase=PeerLinkDatabase.getInstance(application);
        chatMessageDao=peerLinkDatabase.chatMessageDao();
        chatMessages=chatMessageDao.getAllChatMessages();
    }

    public void insert(ChatMessage chatMessage){
        new InsertChatMessage(chatMessageDao).execute(chatMessage);
    }

    public void update(int messageId, String status){
        new UpdateChatMessage(chatMessageDao).execute(messageId, status);
    }

    public LiveData<List<ChatMessage>> getChatMessages(){
        return chatMessages;
    }

    private static class InsertChatMessage extends AsyncTask<ChatMessage, Void, Void> {

        private ChatMessageDao chatMessageDao;

        private InsertChatMessage(ChatMessageDao chatMessageDao){
            this.chatMessageDao=chatMessageDao;
        }

        @Override
        protected Void doInBackground(ChatMessage... chatMessages) {
            chatMessageDao.insert(chatMessages[0]);
            return null;
        }
    }

    private static class UpdateChatMessage extends AsyncTask<Object, Void, Void>{

        private  ChatMessageDao chatMessageDao;

        public UpdateChatMessage(ChatMessageDao chatMessageDao) {
            this.chatMessageDao = chatMessageDao;
        }

        @Override
        protected Void doInBackground(Object... objects) {
            chatMessageDao.updateMessageStatus((Integer) objects[0], (String) objects[1]);
            return null;
        }
    }
}

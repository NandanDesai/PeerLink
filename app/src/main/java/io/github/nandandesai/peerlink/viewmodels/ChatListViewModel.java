package io.github.nandandesai.peerlink.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import io.github.nandandesai.peerlink.models.ChatMessage;
import io.github.nandandesai.peerlink.models.ChatSession;
import io.github.nandandesai.peerlink.repositories.ChatMessageRepository;
import io.github.nandandesai.peerlink.repositories.ChatRepository;

public class ChatListViewModel extends AndroidViewModel {

    private ChatRepository chatRepository;
    private LiveData<List<ChatSession>> chatSessions;

    private ChatMessageRepository TESTchatMessageRepository;

    public ChatListViewModel(@NonNull Application application) {
        super(application);
        chatRepository=new ChatRepository(application);
        //
        TESTchatMessageRepository=new ChatMessageRepository(application);
        //
        chatSessions=chatRepository.getChats();
    }

    public void insert(ChatSession chatSession){
        chatRepository.insertChat(chatSession);
    }

    public void update(String chatId, long time, String lastMessage){
        chatRepository.updateChat(chatId,time,lastMessage);
    }

    public LiveData<List<ChatSession>> getChatSessions() {
        return chatSessions;
    }

    public ChatMessageRepository getTESTchatMessageRepository() {
        return TESTchatMessageRepository;
    }
}

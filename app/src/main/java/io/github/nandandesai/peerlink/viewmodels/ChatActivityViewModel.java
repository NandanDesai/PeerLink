package io.github.nandandesai.peerlink.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import io.github.nandandesai.peerlink.models.ChatMessage;
import io.github.nandandesai.peerlink.repositories.ChatMessageRepository;

public class ChatActivityViewModel extends AndroidViewModel {

    private ChatMessageRepository chatMessageRepository;
    private LiveData<List<ChatMessage>> chatMessages;

    public ChatActivityViewModel(@NonNull Application application) {
        super(application);
        chatMessageRepository=new ChatMessageRepository(application);
        chatMessages=chatMessageRepository.getChatMessages();
    }

    public void insert(ChatMessage chatMessage){
        chatMessageRepository.insert(chatMessage);
    }

    public LiveData<List<ChatMessage>> getChatMessages() {
        return chatMessages;
    }
}

package io.github.nandandesai.peerlink.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import io.github.nandandesai.peerlink.models.ChatMessage;
import io.github.nandandesai.peerlink.models.Contact;
import io.github.nandandesai.peerlink.repositories.ChatListRepository;
import io.github.nandandesai.peerlink.repositories.ChatMessageRepository;
import io.github.nandandesai.peerlink.repositories.ContactRepository;

public class ChatActivityViewModel extends AndroidViewModel {

    private ChatMessageRepository chatMessageRepository;
    private ChatListRepository chatListRepository;
    private ContactRepository contactRepository;
    private LiveData<List<ChatMessage>> chatMessages;

    public ChatActivityViewModel(@NonNull Application application) {
        super(application);
        chatMessageRepository=new ChatMessageRepository(application);
        chatListRepository=new ChatListRepository(application);
        contactRepository=new ContactRepository(application);
    }

    public ChatListRepository getChatListRepository() {
        return chatListRepository;
    }

    public ContactRepository getContactRepository(){
        return contactRepository;
    }

    public void insert(ChatMessage chatMessage){
        chatMessageRepository.insert(chatMessage);
    }

    public LiveData<List<ChatMessage>> getChatMessages(String chatId) {
        return chatMessageRepository.getChatMessages(chatId);
    }

    public LiveData<ChatMessage> getAllUnreadMsgs(String chatId){
        return chatMessageRepository.getAllUnreadMsgs(chatId);
    }

    public void updateUnreadMessagesToRead(String chatId){
        chatMessageRepository.updateUnreadMessagesToRead(chatId);
    }
}

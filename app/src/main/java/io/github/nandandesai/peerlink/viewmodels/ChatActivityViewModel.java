package io.github.nandandesai.peerlink.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import io.github.nandandesai.peerlink.models.ChatMessage;
import io.github.nandandesai.peerlink.models.ChatSession;
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

    public LiveData<ChatSession> getChatSession(String chatId){
        return chatListRepository.getChatSession(chatId);
    }

    public LiveData<Contact> getContact(String id){
        return contactRepository.getContact(id);
    }

    public void insert(ChatMessage chatMessage){
        chatMessageRepository.insert(chatMessage);
    }

    public List<ChatMessage> getChatMessages(String chatId) {
        return chatMessageRepository.getChatMessages(chatId);
    }

    public LiveData<ChatMessage> getRecentChatMessage(String chatId){
        return chatMessageRepository.getRecentMsg(chatId);
    }

    public LiveData<List<ChatMessage>> getAllUnreadMsgs(String chatId){
        return chatMessageRepository.getAllUnreadMsgs(chatId);
    }

    public void updateMessageStatusWithChatId(String chatId, String fromStatus, String toStatus){
        chatMessageRepository.updateMessageStatusWithChatId(chatId, fromStatus, toStatus);
    }
}

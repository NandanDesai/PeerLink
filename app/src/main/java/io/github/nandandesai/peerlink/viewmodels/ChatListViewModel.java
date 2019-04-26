package io.github.nandandesai.peerlink.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import io.github.nandandesai.peerlink.models.ChatSession;
import io.github.nandandesai.peerlink.repositories.ChatListRepository;
import io.github.nandandesai.peerlink.repositories.ChatMessageRepository;

public class ChatListViewModel extends AndroidViewModel {

    private ChatListRepository chatListRepository;
    private LiveData<List<ChatSession>> chatSessions;

    private ChatMessageRepository TESTchatMessageRepository;

    public ChatListViewModel(@NonNull Application application) {
        super(application);
        chatListRepository =new ChatListRepository(application);
        //
        TESTchatMessageRepository=new ChatMessageRepository(application);
        //
        chatSessions= chatListRepository.getChats();
    }

    public void insert(ChatSession chatSession){
        chatListRepository.insertChat(chatSession);
    }

    public LiveData<Integer> getNumberOfUnreadMsgs(String chatId){
        return chatListRepository.getNumberOfUnreadMsgs(chatId);
    }

    public LiveData<String> getRecentMsg(String chatId){
        return chatListRepository.getRecentMsg(chatId);
    }

    public LiveData<ChatSession> getChatSession(String chatId){
        return chatListRepository.getChatSession(chatId);
    }

    public LiveData<List<String>> getAllChatIds(){
        return chatListRepository.getAllChatIds();
    }

    public LiveData<List<ChatSession>> getChatSessions() {
        return chatSessions;
    }

    public ChatMessageRepository getTESTchatMessageRepository() {
        return TESTchatMessageRepository;
    }
}

package io.github.nandandesai.peerlink.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class ChatSession {

    public static class TYPE{
        public static final String DIRECT="direct";
        public static final String GROUP="group";
    }

    @PrimaryKey @NonNull
    private String chatId;
    private String name;
    private String type;
    private long lastUpdated;
    private String lastMessage;
    private int noOfUnreadMessages;

    public ChatSession(String chatId, String name, String type, long lastUpdated, String lastMessage, int noOfUnreadMessages) {
        this.chatId = chatId;
        this.name = name;
        this.type = type;
        this.lastUpdated = lastUpdated;
        this.lastMessage = lastMessage;
        this.noOfUnreadMessages = noOfUnreadMessages;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public int getNoOfUnreadMessages() {
        return noOfUnreadMessages;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public String getChatId() {
        return chatId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}

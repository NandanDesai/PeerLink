package io.github.nandandesai.peerlink.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = PeerLinkSession.class,
        parentColumns = "address",
        childColumns = "chatId",
        onDelete = CASCADE))
public class ChatSession {

    public static class TYPE{
        public static final String DIRECT="direct";
        public static final String GROUP="group";
    }

    @PrimaryKey @NonNull
    private String chatId;
    private String name;
    private String type;
    private String icon;

    public ChatSession(String chatId, String name, String type, String icon) {
        this.chatId = chatId;
        this.name = name;
        this.type = type;
        this.icon = icon;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public void setIcon(String icon){
        this.icon=icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
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

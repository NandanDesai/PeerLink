package io.github.nandandesai.peerlink.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;


/*
Used to store a chat message received from either a group or a direct conversation.
*/


@Entity(foreignKeys = @ForeignKey(entity = PeerLinkSession.class,
        parentColumns = "address",
        childColumns = "chatId",
        onDelete = CASCADE))
public class ChatMessage {

    public static class STATUS{
        public static final String WAITING_TO_SEND="waiting";
        public static final String SENT_TO_SERVER="server_sent";
        public static final String DELIVERED_TO_RECIPIENT="delivered";
        public static final String RECIPIENT_READ="recipient_read";//the receiver has read the message
        public static final String USER_READ="user_read";//I have read the message
        public static final String USER_NOT_READ="user_not_read";
    }

    public static class TYPE{
        public static final String TEXT="text";
        public static final String MULTIMEDIA="multimedia";
    }

    @PrimaryKey(autoGenerate = true) private int messageId;
    private String messageContent;

    /*
        messageFrom and messageTo will be used to determine incoming and outgoing messages.
        They can also be used to determine in-group replies.

        chatId will refer to the address in the session table
    */
    private String messageFrom; //From ID
    private String messageTo;   //To ID
    private String messageStatus;
    private long messageTime;
    private String messageType;
    private String chatId;

    public ChatMessage(String messageContent, String messageFrom, String messageTo, String messageStatus, long messageTime, String messageType, String chatId) {
        this.messageContent = messageContent;
        this.messageFrom = messageFrom;
        this.messageTo = messageTo;
        this.messageStatus = messageStatus;
        this.messageTime = messageTime;
        this.messageType = messageType;
        this.chatId = chatId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getMessageId() {
        return messageId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public String getMessageFrom() {
        return messageFrom;
    }

    public String getMessageTo() {
        return messageTo;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getChatId() {
        return chatId;
    }
}

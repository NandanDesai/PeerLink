package io.github.nandandesai.peerlink;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;

import java.util.List;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import de.hdodenhof.circleimageview.CircleImageView;
import io.github.nandandesai.peerlink.adapters.ChatMessagesAdapter;
import io.github.nandandesai.peerlink.core.PeerLinkSender;
import io.github.nandandesai.peerlink.database.PeerLinkDatabase;
import io.github.nandandesai.peerlink.models.ChatMessage;
import io.github.nandandesai.peerlink.models.ChatSession;
import io.github.nandandesai.peerlink.models.Contact;
import io.github.nandandesai.peerlink.models.PeerLinkSession;
import io.github.nandandesai.peerlink.utils.PeerLinkPreferences;
import io.github.nandandesai.peerlink.viewmodels.ChatActivityViewModel;


public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private ChatActivityViewModel chatActivityViewModel;
    private ChatMessagesAdapter chatMessagesAdapter;
    private String chatId;

    private ImageView emojiButton;
    private EmojiEditText messageInput;
    private ImageButton sendButton;
    private ListView chatMessagesListView;
    private PeerLinkPreferences preferences;
    private EmojiPopup emojiPopup;

    private boolean contactExists = false;
    private boolean chatSessionExists =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        emojiButton=findViewById(R.id.emojiButton);
        messageInput=findViewById(R.id.msgInput);
        sendButton=findViewById(R.id.send);
        chatMessagesListView=findViewById(R.id.chatMessageList);

        preferences=new PeerLinkPreferences(this);

        chatId=getIntent().getStringExtra("chatId");

        chatMessagesAdapter=new ChatMessagesAdapter(this);
        chatMessagesListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        chatMessagesListView.setAdapter(chatMessagesAdapter);

        chatActivityViewModel= ViewModelProviders.of(this).get(ChatActivityViewModel.class);

        //setup toolbar info
        Toolbar toolbar = (Toolbar) findViewById(R.id.chatActivityToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView chatNameView=toolbar.findViewById(R.id.chatToolbarTitle);
        CircleImageView toolbarProfileImageView=findViewById(R.id.chatToolbarIcon);


        setupLiveDataObservers(chatNameView, toolbarProfileImageView);



        //call the below method to get a list of messages that are not read
        //and then send a Read Receipt reply to the sender
        //chatActivityViewModel.getAllUnreadMsgs()
        //after that, mark those messages as read in your own database by calling the below method
        chatActivityViewModel.updateMessageStatusWithChatId(chatId, ChatMessage.STATUS.USER_NOT_READ, ChatMessage.STATUS.USER_READ);

        emojiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emojiPopup.toggle();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String messageContent=messageInput.getText().toString();
                messageContent=messageContent.trim();
                if(!messageContent.equals("")){
                    Log.d(TAG, "onClick: "+messageInput.getText());
                    String messageFrom=preferences.getMyOnionAddress();
                    String messageTo=chatId;
                    String messageStatus=ChatMessage.STATUS.WAITING_TO_SEND;
                    long messageTime=System.currentTimeMillis();
                    String messageType=ChatMessage.TYPE.TEXT;
                    String id=chatId;
                    ChatMessage chatMessage=new ChatMessage(messageContent,messageFrom,messageTo,messageStatus,messageTime, messageType,id);
                    chatActivityViewModel.insert(chatMessage);

                    messageInput.getText().clear();

                }
            }
        });

        messageInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emojiPopup.isShowing()){
                    emojiPopup.dismiss();
                }
            }
        });


        setUpEmojiPopup();
    }

    @Override protected void onStop() {
        if (emojiPopup != null) {
            emojiPopup.dismiss();
        }
        super.onStop();
    }

    private void setUpEmojiPopup() {
        emojiPopup = EmojiPopup.Builder.fromRootView(findViewById(R.id.chatLayout)).build(messageInput);
    }

    private void setupLiveDataObservers(TextView chatNameView, CircleImageView toolbarProfileImageView){

        chatActivityViewModel.getContact(chatId).observe(this, new Observer<Contact>() {
            @Override
            public void onChanged(@Nullable Contact contact) {
                if(contact==null){
                    Log.d(TAG, "onChanged: contact doesn't exists. So, trying to get info from ChatSession table");
                    contactExists =false;
                    return;
                }
                contactExists=true;
                Log.d(TAG, "onChanged: contact exists! Setting info on toolbar from Contact table");
                chatNameView.setText(contact.getName());
                Glide.with(ChatActivity.this)
                        .asBitmap()
                        .load(contact.getProfilePic())
                        .into(toolbarProfileImageView);
            }
        });

        //the below code is temporary to some extent.
        //if the contact doesn't exists, then it is probably a group chat or it is a chat with an unsaved contact.
        //so we'll try to check if the contact exists or not. If it doesn't then we'll get the data from ChatSession
        //and then use it to set the Toolbar info.
        chatActivityViewModel.getChatSession(chatId).observe(this, new Observer<ChatSession>() {
            @Override
            public void onChanged(@Nullable ChatSession chatSession) {
                if (!contactExists) {
                    if (chatSession == null) {
                        //if contact doesn't exists and chatSession is null
                        Log.d(TAG, "onChanged: ChatSession doesn't exists on chatId: " + chatId);
                        chatNameView.setText(chatId);
                        chatSessionExists =false;
                        return;
                    }
                    chatSessionExists =true;
                    Log.d(TAG, "onChanged: ChatSession info exists. I'm using that to set the toolbar info");
                    chatNameView.setText(chatSession.getName());
                    Glide.with(ChatActivity.this)
                            .asBitmap()
                            .load(chatSession.getIcon())
                            .into(toolbarProfileImageView);
                    //You should also add an "Online" status to the ChatSession and use it here. Using LiveData here is useful to
                    //dynamically update
                }
            }
        });

        chatActivityViewModel.getChatMessages(chatId).observe(this, new Observer<List<ChatMessage>>() {
            @Override
            public void onChanged(@Nullable List<ChatMessage> chatMessages) {
                chatMessagesAdapter.setChatMessages(chatMessages);

                ///////the below code is temporary.
                /////////////////////////////////
                ///////////////////////////////////
                if (chatMessages != null) {
                    if(chatMessages.size() == 1 && (!chatSessionExists)) {
                        //if the user is posting the first message and chatSession doesn't exists, then create a session
                        PeerLinkDatabase.getInstance(ChatActivity.this).sessionStoreDao().insert(new PeerLinkSession(chatId, 1, null));
                        PeerLinkDatabase.getInstance(ChatActivity.this).chatSessionDao().insert(new ChatSession(chatId, chatNameView.getText().toString(), ChatSession.TYPE.DIRECT, "https://images.unsplash.com/photo-1529665253569-6d01c0eaf7b6?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80"));
                    }
                    for(ChatMessage message:chatMessages){
                        Log.d(TAG, "onChanged: Queueing the message :"+message.getMessageContent());
                        //enqueueMessageToSend(message);
                    }
                }
            }
        });

    }

    private void enqueueMessageToSend(ChatMessage chatMessage){

        PeerLinkPreferences preferences=new PeerLinkPreferences(this);

        Data data = new Data.Builder()
                //.putString(PeerLinkSender.SENDER_ADDR, chatId) //replace the below line with this one
                .putString(PeerLinkSender.SENDER_ADDR, preferences.getMyOnionAddress()) //this is temporary.
                .putString(PeerLinkSender.MSG_TO_SEND, new Gson().toJson(chatMessage))
                .build();

        final OneTimeWorkRequest workRequest =
                new OneTimeWorkRequest.Builder(PeerLinkSender.class)
                        .setInputData(data)
                        .build();

        WorkManager.getInstance().enqueue(workRequest);
        Log.d(TAG, "enqueueMessageToSend: Message queued");
    }


}

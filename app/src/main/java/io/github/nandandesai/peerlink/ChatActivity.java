package io.github.nandandesai.peerlink;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.nandandesai.peerlink.adapters.ChatMessagesAdapter;
import io.github.nandandesai.peerlink.models.ChatMessage;
import io.github.nandandesai.peerlink.models.ChatSession;
import io.github.nandandesai.peerlink.viewmodels.ChatActivityViewModel;


public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private ChatActivityViewModel chatActivityViewModel;

    private String chatId;

    private ImageView emojiButton;
    private EmojiEditText messageInput;
    private ImageButton sendButton;
    private ListView chatMessagesListView;

    EmojiPopup emojiPopup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        emojiButton=findViewById(R.id.emojiButton);
        messageInput=findViewById(R.id.msgInput);
        sendButton=findViewById(R.id.send);
        chatMessagesListView=findViewById(R.id.chatMessageList);


        chatId=getIntent().getStringExtra("chatId");

        final ChatMessagesAdapter chatMessagesAdapter=new ChatMessagesAdapter(this);
        chatMessagesListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        chatMessagesListView.setAdapter(chatMessagesAdapter);

        chatActivityViewModel= ViewModelProviders.of(this).get(ChatActivityViewModel.class);
        chatActivityViewModel.getChatMessages(chatId).observe(this, new Observer<List<ChatMessage>>() {
            @Override
            public void onChanged(@Nullable List<ChatMessage> chatMessages) {
                chatMessagesAdapter.setChatMessages(chatMessages);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.chatActivityToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView chatUserNameView=toolbar.findViewById(R.id.chatToolbarTitle);
        CircleImageView toolbarProfileImageView=findViewById(R.id.chatToolbarIcon);

        chatActivityViewModel.getChatListRepository().getChatSession(chatId).observe(this, new Observer<ChatSession>() {
            @Override
            public void onChanged(@Nullable ChatSession chatSession) {
                chatUserNameView.setText(chatSession.getName());
                Glide.with(ChatActivity.this)
                        .asBitmap()
                        .load(chatSession.getIcon())
                        .into(toolbarProfileImageView);
                //You should also add an "Online" status to the ChatSession and use it here. Using LiveData here is useful to
                //dynamically update
            }
        });

        //call the below method to get a list of messages that are not read
        //and then send a Read Receipt reply to the sender
        //chatActivityViewModel.getAllUnreadMsgs()
        //after that, mark those messages as read in your own database by calling the below method

        chatActivityViewModel.updateUnreadMessagesToRead(chatId);

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
                    String messageFrom="1";
                    String messageTo="abcd123";
                    String messageStatus=ChatMessage.STATUS.WAITING_TO_SEND;
                    long messageTime=System.currentTimeMillis();
                    String messageType=ChatMessage.TYPE.TEXT;
                    String chatId="abcd123";
                    ChatMessage chatMessage=new ChatMessage(messageContent,messageFrom,messageTo,messageStatus,messageTime, messageType,chatId);
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

}

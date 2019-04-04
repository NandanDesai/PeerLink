package io.github.nandandesai.peerlink;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.github.nandandesai.peerlink.adapters.ChatListAdapter;
import io.github.nandandesai.peerlink.models.ChatMessage;
import io.github.nandandesai.peerlink.models.ChatSession;
import io.github.nandandesai.peerlink.repositories.ChatMessageRepository;
import io.github.nandandesai.peerlink.viewmodels.ChatListViewModel;
import io.github.nandandesai.peerlink.viewmodels.ContactListViewModel;


public class ChatListFragment extends Fragment {

    private ChatListAdapter chatListAdapter;
    private RecyclerView recyclerView;
    private List<ChatSession> chatSessions=new ArrayList<>();

    private ChatListViewModel chatListViewModel;

    public ChatListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chat_list, container, false);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TESTupdateChatItem();
            }
        });

        initRecyclerView(view);
        chatListViewModel=ViewModelProviders.of(this).get(ChatListViewModel.class);
        chatListViewModel.getChatSessions().observe(this, new Observer<List<ChatSession>>() {
            @Override
            public void onChanged(@Nullable List<ChatSession> chatSessions) {
                chatListAdapter.setChatSessions(chatSessions);
            }
        });
        return view;
    }

    private void TESTupdateChatItem(){
        String messageFrom="abcd123";
        String messageTo="1";
        String messageStatus= ChatMessage.STATUS.WAITING;
        long messageTime=System.currentTimeMillis();
        String messageType=ChatMessage.TYPE.TEXT;
        String chatId="abcd123";
        String messageContent="Hi this is abcd123. How are you?";
        ChatMessage chatMessage=new ChatMessage(messageContent,messageFrom,messageTo,messageStatus,messageTime, messageType,chatId);
        chatListViewModel.getTESTchatMessageRepository().insert(chatMessage);
        chatListViewModel.update(chatId, System.currentTimeMillis(), messageContent);
    }

/*
    private void addNewChatItem(){
        chatTitles.add(0,"Alice");
        profilePics.add(0,"https://www.trickscity.com/wp-content/uploads/2018/02/cute-girl-profile-pics.jpg");
        recentChatMsgs.add(0,"Nandan Desai: Hi");
        noOfUnreadMsgs.add(0,1);

        chatListAdapter.notifyItemInserted(0);
        recyclerView.scrollToPosition(0);
    }
*/
    private void initRecyclerView(View view){
        Context context=getContext();
        recyclerView=view.findViewById(R.id.chatList);
        chatListAdapter=new ChatListAdapter(context,chatSessions);
        recyclerView.setAdapter(chatListAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DividerItemDecoration divider = new DividerItemDecoration(
                recyclerView.getContext(),
                DividerItemDecoration.VERTICAL
        );
        divider.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider));
        recyclerView.addItemDecoration(divider);
    }


}

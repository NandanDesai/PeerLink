package io.github.nandandesai.peerlink;

import android.content.Context;
import android.os.Bundle;
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

import io.github.nandandesai.peerlink.adapters.ChatListAdapter;


public class ChatListFragment extends Fragment {


    private ArrayList<String> chatTitles=new ArrayList<>();
    private ArrayList<String> profilePics=new ArrayList<>();
    private ArrayList<String> recentChatMsgs=new ArrayList<>();
    private ArrayList<Integer> noOfUnreadMsgs=new ArrayList<>();

    private ChatListAdapter chatListAdapter;
    private RecyclerView recyclerView;

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


            }
        });

        initRecyclerView(view);

        init();
        return view;
    }


    private void addNewChatItem(){
        chatTitles.add(0,"Alice");
        profilePics.add(0,"https://www.trickscity.com/wp-content/uploads/2018/02/cute-girl-profile-pics.jpg");
        recentChatMsgs.add(0,"Nandan Desai: Hi");
        noOfUnreadMsgs.add(0,1);

        chatListAdapter.notifyItemInserted(0);
        recyclerView.scrollToPosition(0);
    }

    private void initRecyclerView(View view){
        Context context=getContext();
        recyclerView=view.findViewById(R.id.chatList);
        chatListAdapter=new ChatListAdapter(context,chatTitles,profilePics,recentChatMsgs,noOfUnreadMsgs);
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

    private void init(){
        chatTitles.add("Alice");
        profilePics.add("https://www.trickscity.com/wp-content/uploads/2018/02/cute-girl-profile-pics.jpg");
        recentChatMsgs.add("Nandan Desai: Hi");
        noOfUnreadMsgs.add(3);


        chatTitles.add("Bob");
        profilePics.add("https://images.unsplash.com/photo-1529665253569-6d01c0eaf7b6?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80");
        recentChatMsgs.add("Nandan Desai: You coming?");
        noOfUnreadMsgs.add(13);

        chatTitles.add("Alice's Group");
        profilePics.add("https://www.trickscity.com/wp-content/uploads/2018/02/cute-girl-profile-pics.jpg");
        recentChatMsgs.add("Nandan Desai: Hi");
        noOfUnreadMsgs.add(0);


        chatTitles.add("Bob's Group");
        profilePics.add("https://images.unsplash.com/photo-1529665253569-6d01c0eaf7b6?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80");
        recentChatMsgs.add("Nandan Desai: You coming?");
        noOfUnreadMsgs.add(13);

        chatTitles.add("Alice");
        profilePics.add("https://www.trickscity.com/wp-content/uploads/2018/02/cute-girl-profile-pics.jpg");
        recentChatMsgs.add("Nandan Desai: Hi");
        noOfUnreadMsgs.add(3);


        chatTitles.add("Bob");
        profilePics.add("https://images.unsplash.com/photo-1529665253569-6d01c0eaf7b6?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80");
        recentChatMsgs.add("Nandan Desai: You coming?");
        noOfUnreadMsgs.add(0);

        chatTitles.add("Alice");
        profilePics.add("https://www.trickscity.com/wp-content/uploads/2018/02/cute-girl-profile-pics.jpg");
        recentChatMsgs.add("Nandan Desai: Hi");
        noOfUnreadMsgs.add(3);


        chatTitles.add("Bob");
        profilePics.add("https://images.unsplash.com/photo-1529665253569-6d01c0eaf7b6?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80");
        recentChatMsgs.add("Nandan Desai: You coming?");
        noOfUnreadMsgs.add(13);

        chatTitles.add("Alice");
        profilePics.add("https://www.trickscity.com/wp-content/uploads/2018/02/cute-girl-profile-pics.jpg");
        recentChatMsgs.add("Nandan Desai: Hi");
        noOfUnreadMsgs.add(0);


        chatTitles.add("Bob");
        profilePics.add("https://images.unsplash.com/photo-1529665253569-6d01c0eaf7b6?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80");
        recentChatMsgs.add("Nandan Desai: You coming?");
        noOfUnreadMsgs.add(13);
    }

}

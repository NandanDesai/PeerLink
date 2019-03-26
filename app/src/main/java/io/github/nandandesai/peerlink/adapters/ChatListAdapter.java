package io.github.nandandesai.peerlink.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.nandandesai.peerlink.ChatActivity;
import io.github.nandandesai.peerlink.R;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder>{

    private static final String TAG = "ChatListAdapter";

    private Context context;
    private ArrayList<String> chatTitles=new ArrayList<>();
    private ArrayList<String> profilePics=new ArrayList<>();
    private ArrayList<String> recentChatMsgs=new ArrayList<>();
    private ArrayList<Integer> noOfUnreadMsgs=new ArrayList<>();

    public ChatListAdapter(Context context, ArrayList<String> chatTitles, ArrayList<String> profilePics, ArrayList<String> recentChatMsgs, ArrayList<Integer> noOfUnreadMsgs) {
        this.context = context;
        this.chatTitles = chatTitles;
        this.profilePics = profilePics;
        this.recentChatMsgs = recentChatMsgs;
        this.noOfUnreadMsgs = noOfUnreadMsgs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_list_item,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: called");

        //set the profile picture
        Glide.with(context)
                .asBitmap()
                .load(profilePics.get(i))
                .into(viewHolder.profilePicImageView);

        //set the chat title
        viewHolder.chatTitleView.setText(chatTitles.get(i));

        //set the recent chat message
        viewHolder.recentChatMsgView.setText(recentChatMsgs.get(i));
        Log.d(TAG, "onBindViewHolder: array size: "+noOfUnreadMsgs.get(i));
        //set the unread messages count
        if(noOfUnreadMsgs.get(i)>0){
            viewHolder.unreadMsgCountView.setText(noOfUnreadMsgs.get(i)+"");
        }else{
            viewHolder.unreadMsgCountView.setVisibility(View.INVISIBLE);
        }

        //viewHolder.chatListItemLayout.setTag("<some id to get in the next activity>");

        viewHolder.chatListItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: chatTitle is :"+chatTitles.get(i));
                Intent intent=new Intent(context, ChatActivity.class);
                intent.putExtra("name", chatTitles.get(i));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return chatTitles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout chatListItemLayout;
        CircleImageView profilePicImageView;
        TextView chatTitleView;
        TextView recentChatMsgView;
        TextView unreadMsgCountView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chatListItemLayout=itemView.findViewById(R.id.chatListItemLayout);
            profilePicImageView=itemView.findViewById(R.id.profileImage);
            chatTitleView=itemView.findViewById(R.id.chatTitle);
            recentChatMsgView=itemView.findViewById(R.id.recentChatMsg);
            unreadMsgCountView=itemView.findViewById(R.id.noOfUnreadMsgs);
        }
    }
}

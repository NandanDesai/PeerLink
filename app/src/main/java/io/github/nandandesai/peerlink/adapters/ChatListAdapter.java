package io.github.nandandesai.peerlink.adapters;

import android.arch.lifecycle.LiveData;
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

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.nandandesai.peerlink.ChatActivity;
import io.github.nandandesai.peerlink.R;
import io.github.nandandesai.peerlink.models.ChatSession;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder>{

    private static final String TAG = "ChatListAdapter";

    private Context context;

    private List<ChatSession> chatSessions=new ArrayList<>();

    public ChatListAdapter(Context context, List<ChatSession> chatSessions){
        this.context=context;
        this.chatSessions=chatSessions;
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
                .load(chatSessions.get(i).getIcon())
                .into(viewHolder.profilePicImageView);

        //set the chat title
        viewHolder.chatTitleView.setText(chatSessions.get(i).getName());

        //set the recent chat message
        viewHolder.recentChatMsgView.setText(chatSessions.get(i).getLastMessage());
        //set the unread messages count
        if(chatSessions.get(i).getNoOfUnreadMessages()>0){
            viewHolder.unreadMsgCountView.setVisibility(View.VISIBLE);
            viewHolder.unreadMsgCountView.setText(chatSessions.get(i).getNoOfUnreadMessages()+"");
        }else{
            viewHolder.unreadMsgCountView.setVisibility(View.INVISIBLE);
        }

        //viewHolder.chatListItemLayout.setTag("<some id to get in the next activity>");

        viewHolder.chatListItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: chatTitle is :"+chatSessions.get(i).getChatId());
                Intent intent=new Intent(context, ChatActivity.class);
                intent.putExtra("chatId", chatSessions.get(i).getChatId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return chatSessions.size();
    }

    public void setChatSessions(List<ChatSession> chatSessions){
        this.chatSessions=chatSessions;
        notifyDataSetChanged();
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

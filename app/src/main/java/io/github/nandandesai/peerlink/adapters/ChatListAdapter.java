package io.github.nandandesai.peerlink.adapters;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vanniktech.emoji.EmojiTextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.nandandesai.peerlink.ChatActivity;
import io.github.nandandesai.peerlink.R;
import io.github.nandandesai.peerlink.models.ChatMessage;
import io.github.nandandesai.peerlink.models.ChatSession;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder>{

    private static final String TAG = "ChatListAdapter";

    private Context context;
    private List<DataHolder> dataHolders=new ArrayList<>();
    private LifecycleOwner lifecycleOwner;

    public ChatListAdapter(Context context, LifecycleOwner lifecycleOwner, List<DataHolder> dataHolders) {
        this.context = context;
        this.lifecycleOwner=lifecycleOwner;
        this.dataHolders=dataHolders;
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

        dataHolders.get(i).chatSession.observe(lifecycleOwner, new Observer<ChatSession>() {
            @Override
            public void onChanged(@Nullable ChatSession chatSession) {
                if(chatSession!=null){

                    //set profile pic
                    Glide.with(context)
                            .asBitmap()
                            .load(chatSession.getIcon())
                            .into(viewHolder.profilePicImageView);

                    //set chat title
                    viewHolder.chatTitleView.setText(chatSession.getName());

                    //configure onClickListener to go to next activity
                    viewHolder.chatListItemLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(context, ChatActivity.class);
                            intent.putExtra("chatId", chatSession.getChatId());
                            context.startActivity(intent);
                        }
                    });

                }
            }
        });

        //set the recent chat message
        dataHolders.get(i).recentMsg.observe(lifecycleOwner, new Observer<ChatMessage>() {
            @Override
            public void onChanged(@Nullable ChatMessage chatMessage) {
                viewHolder.recentChatMsgView.setText(chatMessage.getMessageContent());
            }
        });

        //set the unread messages count
        dataHolders.get(i).noOfUnreadMsgs.observe(lifecycleOwner, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer unreadMsgs) {
                if(unreadMsgs!=null && unreadMsgs>0){
                    viewHolder.unreadMsgCountView.setVisibility(View.VISIBLE);
                    viewHolder.unreadMsgCountView.setText(unreadMsgs+"");
                }else{
                    viewHolder.unreadMsgCountView.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    public void setDataHolders(List<DataHolder> dataHolders) {
        this.dataHolders = dataHolders;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataHolders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout chatListItemLayout;
        CircleImageView profilePicImageView;
        TextView chatTitleView;
        EmojiTextView recentChatMsgView;
        TextView unreadMsgCountView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            chatListItemLayout=itemView.findViewById(R.id.chatListItemLayout);
            profilePicImageView=itemView.findViewById(R.id.profileImage);
            chatTitleView=itemView.findViewById(R.id.chatTitle);
            recentChatMsgView=itemView.findViewById(R.id.recentChatMsg);
            unreadMsgCountView=itemView.findViewById(R.id.noOfUnreadMsgs);
        }
    }

    public static class DataHolder{
        LiveData<ChatSession> chatSession;
        LiveData<ChatMessage> recentMsg;
        LiveData<Integer> noOfUnreadMsgs;

        public DataHolder(LiveData<ChatSession> chatSession, LiveData<ChatMessage> recentMsg, LiveData<Integer> noOfUnreadMsgs) {
            this.chatSession = chatSession;
            this.recentMsg = recentMsg;
            this.noOfUnreadMsgs = noOfUnreadMsgs;
        }

    }

}

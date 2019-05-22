package io.github.nandandesai.peerlink.adapters;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vanniktech.emoji.EmojiTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import io.github.nandandesai.peerlink.R;
import io.github.nandandesai.peerlink.models.ChatMessage;
import io.github.nandandesai.peerlink.utils.PeerLinkPreferences;

public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int OUTGOING_VIEWTYPE=0;
    private static final int INCOMING_VIEWTYPE=1;
    private static final String TAG = "ChatMessageAdapter";

    private Context context;
    private List<ChatMessage> chatMessages=new ArrayList<>();

    public ChatMessageAdapter(Context context, List<ChatMessage> chatMessages) {
        this.context = context;
        this.chatMessages = chatMessages;
    }

    @Override
    public int getItemViewType(int position) {
        if(chatMessages.get(position).getMessageFrom().equalsIgnoreCase(new PeerLinkPreferences(context).getMyOnionAddress())){
            return OUTGOING_VIEWTYPE;
        }else{
            return INCOMING_VIEWTYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType){
            case OUTGOING_VIEWTYPE: {
                View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.outgoing_chat_msg_item,viewGroup,false);
                return new OutgoingMessageViewHolder(view);
            }
            case INCOMING_VIEWTYPE: {
                View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.incoming_chat_msg_item,viewGroup,false);
                return new IncomingMessageViewHolder(view);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ChatMessage chatMessage=chatMessages.get(i);
        switch (viewHolder.getItemViewType()){
            case OUTGOING_VIEWTYPE:{
                OutgoingMessageViewHolder outgoingMessageViewHolder=(OutgoingMessageViewHolder) viewHolder;
                outgoingMessageViewHolder.messageTextView.setText(chatMessage.getMessageContent());
                outgoingMessageViewHolder.messageTimeView.setText(getFormattedMessageTime(chatMessage.getMessageTime()));
                break;
            }
            case INCOMING_VIEWTYPE:{
                IncomingMessageViewHolder incomingMessageViewHolder=(IncomingMessageViewHolder) viewHolder;
                incomingMessageViewHolder.messageTextView.setText(chatMessage.getMessageContent());
                //incomingMessageViewHolder.messageAuthorView.setText(chatMessage.getMessageFrom());
                incomingMessageViewHolder.messageAuthorView.setText("");/////////////////CHANGE THIS MAYBE?
                incomingMessageViewHolder.messageTimeView.setText(getFormattedMessageTime(chatMessage.getMessageTime()));
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public void setChatMessages(List<ChatMessage> chatMessages){
        this.chatMessages=chatMessages;
        notifyDataSetChanged();
    }

    public void addChatMessages(List<ChatMessage> chatMessages){
        this.chatMessages.addAll(chatMessages);

    }


    public void addChatMessage(ChatMessage chatMessage){
        this.chatMessages.add(chatMessage);
        notifyItemInserted(chatMessages.size());
    }

    private class IncomingMessageViewHolder extends RecyclerView.ViewHolder{
        TextView messageAuthorView;
        TextView messageTimeView;
        EmojiTextView messageTextView;

        public IncomingMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.messageAuthorView=itemView.findViewById(R.id.incomingMsgAuthor);
            this.messageTimeView=itemView.findViewById(R.id.incomingMsgTime);
            this.messageTextView=itemView.findViewById(R.id.incomingMsgText);
        }
    }

    private class OutgoingMessageViewHolder extends RecyclerView.ViewHolder{
        ImageView messageStatusView;
        TextView messageTimeView;
        EmojiTextView messageTextView;

        public OutgoingMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            this.messageStatusView=itemView.findViewById(R.id.outgoingMsgStatus);
            this.messageTimeView=itemView.findViewById(R.id.outgoingMsgTime);
            this.messageTextView=itemView.findViewById(R.id.outgoingMsgText);
        }
    }

    //taken from: https://gist.github.com/nesquena/d09dc68ff07e845cc622
    public static abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        // The minimum amount of items to have below your current scroll position
        // before loading more.
        private int visibleThreshold = 5;
        // The current offset index of data you have loaded
        private int currentPage = 0;
        // The total number of items in the dataset after the last load
        private int previousTotalItemCount = 0;
        // True if we are still waiting for the last set of data to load.
        private boolean loading = true;
        // Sets the starting page index
        private int startingPageIndex = 0;

        RecyclerView.LayoutManager mLayoutManager;

        public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
            this.mLayoutManager = layoutManager;
        }

        public EndlessRecyclerViewScrollListener(GridLayoutManager layoutManager) {
            this.mLayoutManager = layoutManager;
            visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        }

        public EndlessRecyclerViewScrollListener(StaggeredGridLayoutManager layoutManager) {
            this.mLayoutManager = layoutManager;
            visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        }

        public int getLastVisibleItem(int[] lastVisibleItemPositions) {
            int maxSize = 0;
            for (int i = 0; i < lastVisibleItemPositions.length; i++) {
                if (i == 0) {
                    maxSize = lastVisibleItemPositions[i];
                }
                else if (lastVisibleItemPositions[i] > maxSize) {
                    maxSize = lastVisibleItemPositions[i];
                }
            }
            return maxSize;
        }

        // This happens many times a second during a scroll, so be wary of the code you place here.
        // We are given a few useful parameters to help us work out if we need to load some more data,
        // but first we check if we are waiting for the previous load to finish.
        @Override
        public void onScrolled(RecyclerView view, int dx, int dy) {
            int lastVisibleItemPosition = 0;
            int totalItemCount = mLayoutManager.getItemCount();

            if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
                // get maximum element within the list
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
            } else if (mLayoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            } else if (mLayoutManager instanceof LinearLayoutManager) {
                lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            }

            // If the total item count is zero and the previous isn't, assume the
            // list is invalidated and should be reset back to initial state
            if (totalItemCount < previousTotalItemCount) {
                this.currentPage = this.startingPageIndex;
                this.previousTotalItemCount = totalItemCount;
                if (totalItemCount == 0) {
                    this.loading = true;
                }
            }
            // If it’s still loading, we check to see if the dataset count has
            // changed, if so we conclude it has finished loading and update the current page
            // number and total item count.
            if (loading && (totalItemCount > previousTotalItemCount)) {
                loading = false;
                previousTotalItemCount = totalItemCount;
            }

            // If it isn’t currently loading, we check to see if we have breached
            // the visibleThreshold and need to reload more data.
            // If we do need to reload some more data, we execute onLoadMore to fetch the data.
            // threshold should reflect how many total columns there are too
            if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
                currentPage++;
                onLoadMore(currentPage, totalItemCount, view);
                loading = true;
            }
        }

        // Call this method whenever performing new searches
        public void resetState() {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = 0;
            this.loading = true;
        }

        // Defines the process for actually loading more data based on page
        public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);

    }

    private String getFormattedMessageTime(long unixTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(unixTime);
        Date date = new Date(calendar.getTimeInMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(calendar.getTime());
    }

}

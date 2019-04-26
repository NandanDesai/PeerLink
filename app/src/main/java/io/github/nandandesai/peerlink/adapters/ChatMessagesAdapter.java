package io.github.nandandesai.peerlink.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vanniktech.emoji.EmojiTextView;

import java.util.ArrayList;
import java.util.List;

import io.github.nandandesai.peerlink.R;
import io.github.nandandesai.peerlink.models.ChatMessage;
import io.github.nandandesai.peerlink.utils.PeerLinkPreferences;

public class ChatMessagesAdapter extends BaseAdapter {

    private Context context;
    private List<ChatMessage> chatMessages=new ArrayList<>();

    public ChatMessagesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return chatMessages.size();
    }

    @Override
    public Object getItem(int i) {
        return chatMessages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ChatMessage chatMessage=chatMessages.get(i);
        IncomingMessageViewHolder incomingMessageViewHolder;
        OutgoingMessageViewHolder outgoingMessageViewHolder;
        View v=null;

        //checking if the message is from the You or not
        //if it is not from You, then it is an incoming message from another party
        if(!chatMessage.getMessageFrom().equalsIgnoreCase(new PeerLinkPreferences(context).getMyOnionAddress())){
            if(view!=null && view.getTag() instanceof OutgoingMessageViewHolder){
                view=null;
            }
            if(view==null){
                v = LayoutInflater.from(context).inflate(R.layout.incoming_chat_msg_item, null, false);;
                incomingMessageViewHolder=new IncomingMessageViewHolder();

                incomingMessageViewHolder.messageAuthorView=v.findViewById(R.id.incomingMsgAuthor);
                incomingMessageViewHolder.messageTimeView=v.findViewById(R.id.incomingMsgTime);
                incomingMessageViewHolder.messageTextView=v.findViewById(R.id.incomingMsgText);

                v.setTag(incomingMessageViewHolder);
            }else{
                v=view;
                incomingMessageViewHolder=(IncomingMessageViewHolder) v.getTag();
            }

            incomingMessageViewHolder.messageTextView.setText(chatMessage.getMessageContent());
            incomingMessageViewHolder.messageAuthorView.setText(chatMessage.getMessageFrom());
            incomingMessageViewHolder.messageTimeView.setText(chatMessage.getMessageTime()+"");

        }else {
            if(view!=null && view.getTag() instanceof IncomingMessageViewHolder){
               view=null;
            }
            if (view==null){
                v = LayoutInflater.from(context).inflate(R.layout.outgoing_chat_msg_item, null, false);;
                outgoingMessageViewHolder=new OutgoingMessageViewHolder();

                //outgoingMessageViewHolder.messageStatusView= //complete this ;
                outgoingMessageViewHolder.messageTimeView=v.findViewById(R.id.outgoingMsgTime);
                outgoingMessageViewHolder.messageTextView=v.findViewById(R.id.outgoingMsgText);

                v.setTag(outgoingMessageViewHolder);
            }else{
                v=view;
                outgoingMessageViewHolder=(OutgoingMessageViewHolder) v.getTag();
            }

            outgoingMessageViewHolder.messageTextView.setText(chatMessage.getMessageContent());
            outgoingMessageViewHolder.messageTimeView.setText(chatMessage.getMessageTime()+"");
            //complete this
            //outgoingMessageViewHolder.messageStatusView.setImageDrawable(context.getResources().getDrawable(R.drawable.message_got_receipt_from_target));
        }
        return v;
    }

    public void setChatMessages(List<ChatMessage> chatMessages){
        this.chatMessages=chatMessages;
        notifyDataSetChanged();
    }

    private class IncomingMessageViewHolder{
        TextView messageAuthorView;
        TextView messageTimeView;
        EmojiTextView messageTextView;
    }

    private class OutgoingMessageViewHolder{
        ImageView messageStatusView;
        TextView messageTimeView;
        EmojiTextView messageTextView;
    }

}

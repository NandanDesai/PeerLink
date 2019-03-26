package io.github.nandandesai.peerlink.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.nandandesai.peerlink.R;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder>{
    private Context context;
    private ArrayList<String> profilePics=new ArrayList<>();
    private ArrayList<String> contactNames=new ArrayList<>();

    public ContactListAdapter(Context context, ArrayList<String> profilePics, ArrayList<String> contactNames) {
        this.context = context;
        this.profilePics = profilePics;
        this.contactNames = contactNames;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_list_item,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Glide.with(context)
                .asBitmap()
                .load(profilePics.get(i))
                .into(viewHolder.profilePicImageView);

        viewHolder.contactNameView.setText(contactNames.get(i));

        viewHolder.contactListItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Contact Item Clicked: "+contactNames.get(i), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactNames.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout contactListItemLayout;
        CircleImageView profilePicImageView;
        TextView contactNameView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactListItemLayout=itemView.findViewById(R.id.contactListItemLayout);
            profilePicImageView=itemView.findViewById(R.id.contactProfileImage);
            contactNameView=itemView.findViewById(R.id.contactName);
        }
    }
}

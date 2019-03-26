package io.github.nandandesai.peerlink;

import android.content.Context;
import android.content.Intent;
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

import io.github.nandandesai.peerlink.adapters.ContactListAdapter;


public class ContactListFragment extends Fragment {

    private ArrayList<String> profilePics=new ArrayList<>();
    private ArrayList<String> contactNames=new ArrayList<>();

    private RecyclerView recyclerView;

    public ContactListFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_contact_list, container, false);
        FloatingActionButton fab = view.findViewById(R.id.contact_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add new contact
                Intent intent=new Intent(getContext(), CreateContactActivity.class);
                startActivity(intent);
            }
        });
        initRecyclerView(view);
        initTEST();
        return view;
    }

    private void initRecyclerView(View view){
        recyclerView=view.findViewById(R.id.contact_list);
        Context context=getContext();
        ContactListAdapter contactListAdapter=new ContactListAdapter(context, profilePics, contactNames);
        recyclerView.setAdapter(contactListAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider));
        recyclerView.addItemDecoration(divider);
    }

    private void initTEST(){
        contactNames.add("Bob");
        profilePics.add("https://images.unsplash.com/photo-1529665253569-6d01c0eaf7b6?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80");

        contactNames.add("Alice");
        profilePics.add("https://images.unsplash.com/photo-1529665253569-6d01c0eaf7b6?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80");

        contactNames.add("Bob");
        profilePics.add("https://images.unsplash.com/photo-1529665253569-6d01c0eaf7b6?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80");


    }
}

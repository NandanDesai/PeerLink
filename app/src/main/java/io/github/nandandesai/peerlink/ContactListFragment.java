package io.github.nandandesai.peerlink;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
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

import io.github.nandandesai.peerlink.adapters.ContactListAdapter;
import io.github.nandandesai.peerlink.models.Contact;
import io.github.nandandesai.peerlink.viewmodels.ChatActivityViewModel;
import io.github.nandandesai.peerlink.viewmodels.ContactListViewModel;


public class ContactListFragment extends Fragment {
    private List<Contact> contacts=new ArrayList<>();

    private RecyclerView recyclerView;

    private ContactListViewModel contactListViewModel;

    private ContactListAdapter contactListAdapter;

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

        contactListViewModel = ViewModelProviders.of(this).get(ContactListViewModel.class);
        contactListViewModel.getContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(@Nullable List<Contact> contacts) {
                contactListAdapter.setContactList(contacts);
            }
        });

        return view;
    }

    private void initRecyclerView(View view){
        recyclerView=view.findViewById(R.id.contact_list);
        Context context=getContext();
        contactListAdapter=new ContactListAdapter(context, contacts);
        recyclerView.setAdapter(contactListAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider));
        recyclerView.addItemDecoration(divider);
    }


}

package io.github.nandandesai.peerlink.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import io.github.nandandesai.peerlink.models.Contact;
import io.github.nandandesai.peerlink.repositories.ContactRepository;

public class ContactListViewModel  extends AndroidViewModel {
    private ContactRepository contactRepository;
    private LiveData<List<Contact>> contacts;

    public ContactListViewModel(@NonNull Application application) {
        super(application);
        contactRepository=new ContactRepository(application);
        contacts=contactRepository.getContacts();
    }

    public ContactRepository getContactRepository() {
        return contactRepository;
    }

    public LiveData<List<Contact>> getContacts() {
        return contacts;
    }
}

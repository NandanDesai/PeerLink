package io.github.nandandesai.peerlink.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import java.util.List;

import io.github.nandandesai.peerlink.database.ContactDao;
import io.github.nandandesai.peerlink.database.PeerLinkDatabase;
import io.github.nandandesai.peerlink.models.Contact;

public class ContactRepository {
    private ContactDao contactDao;
    private LiveData<List<Contact>> contacts;

    public ContactRepository(Application application){
        PeerLinkDatabase peerLinkDatabase=PeerLinkDatabase.getInstance(application);
        contactDao=peerLinkDatabase.contactDao();
        contacts=contactDao.getAllContacts();
    }

    public void insert(Contact contact){
        new Thread(new Runnable() {
            @Override
            public void run() {
                contactDao.insert(contact);
            }
        }).start();
    }

    public void update(Contact contact){
        new Thread(new Runnable() {
            @Override
            public void run() {
                contactDao.update(contact);
            }
        }).start();
    }

    public LiveData<Contact> getContact(String id){
       return contactDao.getContact(id);
    }

    public void delete(String id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                contactDao.delete(id);
            }
        }).start();
    }

    public LiveData<List<Contact>> getContacts() {
        return contacts;
    }
}

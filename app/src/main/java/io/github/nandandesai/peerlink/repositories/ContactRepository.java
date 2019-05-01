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
    private PeerLinkDatabase peerLinkDatabase;
    public ContactRepository(Application application){
        peerLinkDatabase=PeerLinkDatabase.getInstance(application);
        contactDao=peerLinkDatabase.contactDao();
        contacts=contactDao.getAllContacts();
    }

    public void insert(Contact contact){
        new Thread(new Runnable() {
            @Override
            public void run() {
                contactDao.insert(contact);
                if(peerLinkDatabase.chatSessionDao().chatSessionExists(contact.getId())==1){
                    peerLinkDatabase.chatSessionDao().updateChatName(contact.getId(), contact.getName());
                }
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

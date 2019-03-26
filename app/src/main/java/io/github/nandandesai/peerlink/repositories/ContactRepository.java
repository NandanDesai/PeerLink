package io.github.nandandesai.peerlink.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

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
        new InsertContact(contactDao).execute(contact);
    }

    public void update(Contact contact){
        new UpdateContact(contactDao).execute(contact);
    }

    public void delete(String id){
        new DeleteContact(contactDao).execute(id);
    }

    public LiveData<List<Contact>> getContacts() {
        return contacts;
    }

    private static class InsertContact extends AsyncTask<Contact, Void, Void>{

        private ContactDao contactDao;

        public InsertContact(ContactDao contactDao) {
            this.contactDao = contactDao;
        }

        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDao.insert(contacts[0]);
            return null;
        }
    }

    private static class UpdateContact extends AsyncTask<Contact, Void, Void>{

        private ContactDao contactDao;

        public UpdateContact(ContactDao contactDao) {
            this.contactDao = contactDao;
        }

        @Override
        protected Void doInBackground(Contact... contacts) {
            contactDao.update(contacts[0]);
            return null;
        }
    }

    private static class DeleteContact extends AsyncTask<String, Void, Void>{

        private ContactDao contactDao;

        public DeleteContact(ContactDao contactDao) {
            this.contactDao = contactDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            contactDao.delete(strings[0]);
            return null;
        }
    }
}

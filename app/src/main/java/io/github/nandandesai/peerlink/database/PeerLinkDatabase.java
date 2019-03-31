package io.github.nandandesai.peerlink.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import com.commonsware.cwac.saferoom.SafeHelperFactory;


import io.github.nandandesai.peerlink.models.ChatSession;
import io.github.nandandesai.peerlink.models.ChatMessage;
import io.github.nandandesai.peerlink.models.Contact;
import io.github.nandandesai.peerlink.models.PeerLinkIdentityKey;
import io.github.nandandesai.peerlink.models.PeerLinkPreKey;
import io.github.nandandesai.peerlink.models.PeerLinkSession;
import io.github.nandandesai.peerlink.models.PeerLinkSignedPreKey;

@Database(entities = {ChatMessage.class, ChatSession.class, Contact.class, PeerLinkIdentityKey.class, PeerLinkPreKey.class, PeerLinkSession.class, PeerLinkSignedPreKey.class},version = 1)
public abstract class PeerLinkDatabase extends RoomDatabase {
    private static final String TAG = "PeerLinkDatabase";
    private static PeerLinkDatabase instance;
    private static final String DB_NAME="peerlink.db";
    public abstract ChatMessageDao chatMessageDao();
    public abstract ChatSessionDao chatDao();
    public abstract ContactDao contactDao();
    public abstract IdentityKeyDao identityKeyStoreDao();
    public abstract PreKeyDao preKeyStoreDao();
    public abstract SessionDao sessionStoreDao();
    public abstract SignedPreKeyDao signedPreKeyStoreDao();

    public static synchronized PeerLinkDatabase getInstance(Context context){

        if(instance==null) {
            String passPhrase="peerlinkSecret";
            SafeHelperFactory factory = new SafeHelperFactory(passPhrase.toCharArray());
            instance=Room.databaseBuilder(context, PeerLinkDatabase.class, DB_NAME)
                    .openHelperFactory(factory)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        //Log.d(TAG,"Is database encrypted? : "+ SQLCipherUtils.getDatabaseState(context, DB_NAME));
        return instance;
    }

    private static RoomDatabase.Callback roomCallback=new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            new InitializeDb(instance).execute();
        }
    };

    private static class InitializeDb extends AsyncTask<Void, Void, Void>{
        private ContactDao contactDao;

        public InitializeDb(PeerLinkDatabase database) {
            this.contactDao = database.contactDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Contact contact = new Contact("< incomplete >", "< incomplete >", "Me","Hey there! I'm using PeerLink.");
            contactDao.insert(contact);
            return null;
        }
    }

}

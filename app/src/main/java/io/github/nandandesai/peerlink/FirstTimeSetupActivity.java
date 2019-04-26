package io.github.nandandesai.peerlink;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kofigyan.stateprogressbar.StateProgressBar;

import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.state.SignalProtocolStore;

import io.github.nandandesai.peerlink.core.PeerLinkKeyManager;
import io.github.nandandesai.peerlink.repositories.PeerLinkKeyRepository;
import io.github.nandandesai.peerlink.utils.OrbotUtils;
import io.github.nandandesai.peerlink.utils.PeerLinkPreferences;


///////////////////////////////////////
//////////  THIS CLASS IS A TEMPORARY SETUP
//////////  A LOT OF JUGAAD HAS BEEN DONE HERE WITH HANDLING OF ORBOT FUNCTIONS
//////////  REVISE THIS CLASS PLEASE.
//////////  TEMPORARILY YOU CAN USE THIS THOUGH.
////////////////////////////////////////
public class FirstTimeSetupActivity extends AppCompatActivity {
    private static final String TAG = "FirstTimeSetupActivity";
    private String[] descriptionData = {"Orbot\nStart", "Generate\nOnion Service", "Key\nGeneration", "Finalize\nSetup"};
    private StateProgressBar stateProgressBar;
    private ProgressBar progressBar;
    private TextView progressText;
    private OrbotUtils orbotUtils;
    private PeerLinkPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_setup);
        stateProgressBar=findViewById(R.id.state_progress_bar);
        progressBar=findViewById(R.id.setup_progress_bar);
        progressText=findViewById(R.id.setup_progress_text);

        stateProgressBar.setStateDescriptionData(descriptionData);


        orbotUtils=new OrbotUtils(this);
        preferences=new PeerLinkPreferences(this);

        startOrInstallOrbot();


        Log.d(TAG, "onCreate: Onion address from the preferences is: "+preferences.getMyOnionAddress());

    }

    private void startOrInstallOrbot(){
        progressText.setText("Starting Orbot...");
        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
        if(orbotUtils.isOrbotInstalled()){
            if(!orbotUtils.isOrbotRunning()){
                AlertDialog.Builder alertDialog=orbotUtils.requestOrbotStart(this).setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        generateOnionService();
                    }
                });
                alertDialog.show();
            }else{
                generateOnionService();
            }
        }else{
            orbotUtils.requestOrbotInstall(this);
        }
    }

    private void generateOnionService(){
        progressText.setText("Requesting Orbot to generate a new Onion Service...");
        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
        AlertDialog.Builder alertDialog=orbotUtils.requestHiddenServiceOnPort(this, 9000).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                new SetupTask(FirstTimeSetupActivity.this, stateProgressBar, progressText).execute();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==9999) {
            Log.d(TAG, "onActivityResult: onion service request result");
            Bundle bundle = data.getExtras();
            if (bundle.isEmpty()) {
                Log.d(TAG, "onActivityResult: no result from Orbot");
            } else {
                String onionAddress=bundle.getString("hs_host");
                Log.d(TAG, "onActivityResult: The onion link is:"  + onionAddress);
                preferences.setMyOnionAddress(onionAddress);
            }
        }else if(requestCode==1){
            Log.d(TAG, "onActivityResult: Start orbot request result");
            //after starting Orbot, generate Onion Service
            //generateOnionService();
        }
    }

    private static class SetupTask extends AsyncTask<Void, String, Void>{
        private Activity activity;
        private StateProgressBar stateProgressBar;
        private TextView progressTextView;

        public SetupTask(Activity activity, StateProgressBar stateProgressBar, TextView progressTextView) {
            this.activity = activity;
            this.stateProgressBar = stateProgressBar;
            this.progressTextView = progressTextView;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            publishProgress("Creating cryptographic keys (used in Signal protocol)", "THREE");
            SignalProtocolStore store=new PeerLinkKeyRepository(activity.getApplication());
            PeerLinkKeyManager keyManager=new PeerLinkKeyManager(activity, store);
            try {
                keyManager.firstTimeInitialization();
            }catch (InvalidKeyException invalidKeyEx){
                invalidKeyEx.printStackTrace();
                publishProgress("Error creating keys :( Process stopped", "THREE");
                //figure out a fail-proof method to generate the keys again.
                return null;
            }

            publishProgress("Finalizing setup...", "FOUR");
            try {
                //sleep for maybe 4 seconds for the background services to start and the database to get setup
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... updates){
            progressTextView.setText(updates[0]);
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.valueOf(updates[1]));
        }

        @Override
        protected void onPostExecute(Void voids){
            //store whatever necessary data in shared preferences here
            //and then finish this activity
            activity.finish();
        }
    }
}

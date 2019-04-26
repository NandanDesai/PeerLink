package io.github.nandandesai.peerlink.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;

import java.util.List;

import io.github.nandandesai.peerlink.R;


//THIS NEEDS REVISION.
//Take content from OrbotHelper class in NetCipher repository.


////////////////////////////////////////
//////////  THIS CLASS IS A TEMPORARY SETUP
////////////////////////////////////////
public class OrbotUtils {
    private final static int HS_REQUEST_CODE = 9999;
    private final static String ACTION_REQUEST_HS = "org.torproject.android.REQUEST_HS_PORT";
    private final static String ORBOT_PACKAGE_NAME = "org.torproject.android";
    private final static String ACTION_START_TOR = "org.torproject.android.START_TOR";
    private Context context;

    public OrbotUtils(Context context) {
        this.context = context;
    }

    public AlertDialog.Builder requestHiddenServiceOnPort(Activity activity, int port) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.AlertDialogCustom);
        alertDialog.setTitle("Generate Onion Service");
        alertDialog.setMessage("Proceed to generate Onion Service? If already generated, then the same Onion Service will be used.");
        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent = new Intent(ACTION_REQUEST_HS);
                intent.setPackage(ORBOT_PACKAGE_NAME);
                intent.putExtra("hs_port", port);
                activity.startActivityForResult(intent, HS_REQUEST_CODE);
            }
        });
        return alertDialog;
    }

    public boolean isOrbotInstalled() {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(ORBOT_PACKAGE_NAME, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {}
        return installed;
    }

    public boolean isOrbotRunning(){
        ActivityManager localActivityManager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = localActivityManager.getRunningServices(200);

        for(ActivityManager.RunningServiceInfo runningAppProcessInfo: runningServiceInfos){
            if(runningAppProcessInfo.process.equals(ORBOT_PACKAGE_NAME)){
                return true;
            }
        }
        return false;
    }


    public void orbotStart(Activity activity) {
        Intent intent = new Intent(ORBOT_PACKAGE_NAME);
        intent.setAction(ACTION_START_TOR);
        activity.startActivityForResult(intent, 1);
    }

    public AlertDialog.Builder requestOrbotStart(final Activity activity){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.AlertDialogCustom);
        alertDialog.setTitle("Start Orbot");
        alertDialog.setMessage("Orbot is not running. Would you like to start it?");
        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent = new Intent(ORBOT_PACKAGE_NAME);
                intent.setAction(ACTION_START_TOR);
                activity.startActivityForResult(intent, 1);
            }
        });
        return alertDialog;
    }

    public void requestOrbotInstall(final Activity activity){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.AlertDialogCustom);
        alertDialog.setTitle("Install Orbot");
        alertDialog.setMessage("It looks like Orbot app is not installed on your phone. Orbot is required for PeerLink to function.");
        alertDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                activity.finishAndRemoveTask();
            }
        });
        alertDialog.show();
    }

}

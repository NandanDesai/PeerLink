package io.github.nandandesai.peerlinkcomm.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import java.util.List;

public class OrbotUtils {
    private final static int HS_REQUEST_CODE = 9999;
    private final static String ACTION_REQUEST_HS = "org.torproject.android.REQUEST_HS_PORT";
    private final static String ORBOT_PACKAGE_NAME = "org.torproject.android";
    private final static String ACTION_START_TOR = "org.torproject.android.START_TOR";
    private Context context;

    public OrbotUtils(Context context) {
        this.context = context;
    }

    public static void requestHiddenServiceOnPort(Activity activity, int port) {
        Intent intent = new Intent(ACTION_REQUEST_HS);
        intent.setPackage(ORBOT_PACKAGE_NAME);
        intent.putExtra("hs_port", port);
        activity.startActivityForResult(intent, HS_REQUEST_CODE);
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


    public void requestOrbotStart(final Activity activity){
        /*
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.AlertDialogCustom);
        alertDialog.setTitle("Start Orbot");
        alertDialog.setMessage("Tor is not running. Would you like to start it?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(ORBOT_PACKAGE_NAME);
                intent.setAction(ACTION_START_TOR);
                activity.startActivityForResult(intent, 1);
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialog.show();
        */
    }

    public void requestOrbotInstall(final Activity activity){
        /*
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity, R.style.AlertDialogCustom);
        alertDialog.setTitle("Install Orbot");
        alertDialog.setMessage("It looks like Orbot app is not installed on your phone. It is recommended that you install Orbot for Tor proxy.");
        alertDialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialog.show();
        */
    }

}

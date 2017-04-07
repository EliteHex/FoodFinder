package com.ramnarayanan.foodfinder.Data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.ramnarayanan.foodfinder.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shadow on 3/3/2017.
 */

public class PermissionsManager {
    private static Map<String, Integer> permissions_table = new HashMap<>();
    private static final String TAG = "PermissionsManager";
    private static int PERMISSIONS_INT_COUNTER = 1;


//    public static Boolean getPermissions(Activity currentActivity, String requiredPermission) {
//        if (permissions_table.get(requiredPermission)!=null){
//            if(permissions_table.get(requiredPermission)==PackageManager.PERMISSION_GRANTED){
//                return true;
//            }else{
//                return false;
//            }
//        }
//        return false;
//    }

//public static void setPermissions() {
    //permissions_table.put("ACCESS_FINE_LOCATION_INT",1);
//}

    public static boolean checkPermissions(final Context context, String permission) {
        final Activity currentActivity = (Activity) context;
        final String permissionParameter = permission;
        final View currentView = currentActivity.findViewById(R.id.map_fragment);

        if (permissions_table.get(permissionParameter) == null) {
            permissions_table.put(permissionParameter, PERMISSIONS_INT_COUNTER);
            PERMISSIONS_INT_COUNTER++;
        }

        if (ActivityCompat.checkSelfPermission(currentActivity, permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            Snackbar.make(currentView, "Requires " + permission, Snackbar.LENGTH_INDEFINITE)
                    .setAction("Grant Access", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, "Snackbar onClick: begins");
                            if (ActivityCompat.shouldShowRequestPermissionRationale(currentActivity, permissionParameter)) {
                                Log.d(TAG, "checkPermissions: show dialog true");
                                ActivityCompat.requestPermissions(currentActivity,
                                        new String[]{permissionParameter},
                                        permissions_table.get(permissionParameter));

                            } else {
                                Log.d(TAG, "checkPermissions: show dialog false");
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                                Log.d(TAG, "Snackbar onClick: Intent Uri is " + uri.toString());
                                intent.setData(uri);
                                currentActivity.startActivity(intent);
                            }
                            Log.d(TAG, "Snackbar onClick: ends");

                        }
                    }).show();
        }
        return false;
    }
}
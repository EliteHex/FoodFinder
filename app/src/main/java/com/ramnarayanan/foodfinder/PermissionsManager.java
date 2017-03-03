package com.ramnarayanan.foodfinder;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by Shadow on 3/3/2017.
 */

public class PermissionsManager {
    private static Map<String, Boolean> permissions_table = new HashMap<>();
    private static Activity current_activity;
    private static final int ACCESS_FINE_LOCATION_INT = 10;

    public PermissionsManager(Activity currentActivity) {
        current_activity = currentActivity;
    }

    public static boolean getPermissions(String[] requiredPermissions) {
        if (requiredPermissions.length == 0) return true;

        for (String permission : requiredPermissions) {
            if (permissions_table.get(permission) == null) {
                ActivityCompat.requestPermissions(current_activity, new String[]{permission}, ACCESS_FINE_LOCATION_INT);
                return false;
            } else if (permissions_table.get(permission) == false) {
                return false;
            } else if (!checkPermissions(permission)) {
                return false;
            }
        }
        return true;
    }

    public static void setPermissions() {

    }

    public static boolean checkPermissions(String permission) {
        if (ActivityCompat.checkSelfPermission(current_activity, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;

    }
}
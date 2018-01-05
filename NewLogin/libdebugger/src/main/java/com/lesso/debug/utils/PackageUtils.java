package com.lesso.debug.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.IntentCompat;

/**
 * Created by zhengxiaoming on 2017/8/29.
 */
public class PackageUtils {

    public static String getPackageName(Context context) {
        PackageManager pm = context.getPackageManager();
        String name;
        try {
            name = pm.getApplicationLabel(pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            name = "";
        }
        return name;
    }

    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
        }
        return "";
    }

    public static void restart(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = IntentCompat.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        System.exit(0);
    }
}

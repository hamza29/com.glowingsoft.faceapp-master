package com.makemeold.agefaceeditor.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.graphics.Color;

import java.util.List;

public class Wrapper extends Activity {


    protected static boolean isVisible = false;
    boolean isDestroyed;



    protected void onResume() {
        super.onResume();
        isVisible = true;
    }

    protected void onPause() {
        super.onPause();
        isVisible = false;
    }


    private boolean isAppOnForeground(Context context) {
        @SuppressLint("WrongConstant") List<RunningAppProcessInfo> appProcesses = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        String packageName = context.getPackageName();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == 100 && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }




    protected void onDestroy() {
        super.onDestroy();
        this.isDestroyed = true;

    }






}

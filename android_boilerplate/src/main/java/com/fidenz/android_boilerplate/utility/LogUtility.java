package com.fidenz.android_boilerplate.utility;

import android.util.Log;

/**
 * @author chalana
 * @created 2023/05/12 | 8:20 AM
 * @contact Chalana.n@fidenz.com | 071 6 359 376
 */

public class LogUtility {

    private static boolean isReleaseMode = false;
    public static void setReleaseMode(boolean isReleaseMode){
        LogUtility.isReleaseMode = isReleaseMode;
    }

    public static void error(String TAG, Exception error) {
        //Send logs to firebase
        // FirebaseManager.setFirebaseLog(TAG + " " + error.getLocalizedMessage());
        if (!isReleaseMode) {
            Log.e(TAG, error.getMessage());
            error.printStackTrace();
        }
    }

    public static void error(String TAG, String error) {
        //Send logs to firebase
        if (!isReleaseMode) {
            Log.e(TAG, error);
        }
    }

    public static void debug(String TAG, String error) {
        //Send logs to firebase
        if (!isReleaseMode) {
            Log.e(TAG, error);
        }
    }

    public static void debug(String TAG, String error, Exception e) {
        //Send logs to firebase
        if (!isReleaseMode) {
            Log.e(TAG, error);
        }
        e.printStackTrace();
    }


    public static void info(String TAG, String error) {
        //Send logs to firebase
        if (!isReleaseMode) {
            Log.e(TAG, error);
        }
    }

    public static void warning(String TAG, String error) {
        //Send logs to firebase
        if (!isReleaseMode) {
            Log.e(TAG, error);
        }
    }

}

package com.fidenz.android_boilerplate.utility;

import static android.content.Context.VIBRATOR_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Vibrator;

import es.dmoral.toasty.BuildConfig;

/**
 * @author chalana
 * @contact Chalana.n@fidenz.com | 071 6 359 376
 */

public class DeviceManagerUtility {

    private static Activity mActivity;
    private static Vibrator myVib;

    private static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (!StringUtility.isNotNull(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }



    public static void vibrate(Context context) {

        if (myVib == null) {
            myVib = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        }
        myVib.vibrate(50);
    }


    public static String getAppVersion() {
        return BuildConfig.VERSION_NAME;
    }
}

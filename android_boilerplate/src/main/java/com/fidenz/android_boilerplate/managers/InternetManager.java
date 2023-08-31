package com.fidenz.android_boilerplate.managers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.view.View;


import com.fidenz.android_boilerplate.listeners.NetworkChangeListener;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class InternetManager {

    private Dialog progressDialog;

    public boolean isAvailable(Context context) {
        boolean isOnline;
        progressDialog = ProgressDialog.createProgressDialog(context);
        progressDialog.show();
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();

                isOnline = (urlc.getResponseCode() == 200);
                hideProgress();

            } catch (IOException e) {
                isOnline = false;
                hideProgress();
            }
        } else {
            isOnline = false;
            hideProgress();
        }
        return isOnline;
    }

    public void isAvailable(Context context, boolean isShowProgress, NetworkChangeListener listener) {
        boolean isOnline;
        if (isShowProgress) {
            progressDialog = ProgressDialog.createProgressDialog(context);
            progressDialog.show();
        }
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();

                isOnline = (urlc.getResponseCode() == 200);
                if (isShowProgress) {
                    hideProgress();
                }

            } catch (IOException e) {
                isOnline = false;
                if (isShowProgress) {
                    hideProgress();
                }
            }
        } else {
            isOnline = false;
            if (isShowProgress) {
                hideProgress();
            }
        }
        listener.onResponse(isOnline);
    }

    public boolean isAvailable(Context context, View progressView) {
        boolean isOnline;
        progressDialog = ProgressDialog.createProgressDialog(context);
        progressDialog.show();
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();

                isOnline = (urlc.getResponseCode() == 200);
                hideProgress();

            } catch (IOException e) {
                isOnline = false;
                hideProgress();
            }
        } else {
            isOnline = false;
            hideProgress();
        }
        return isOnline;
    }

    public boolean isAvailable(Activity activity, Context context, int progressLayoutId) {
        boolean isOnline;
        progressDialog = ProgressDialog.createProgressDialog(context);
        progressDialog.show();
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();

                isOnline = (urlc.getResponseCode() == 200);
                hideProgress();

            } catch (IOException e) {
                isOnline = false;
                hideProgress();
            }
        } else {
            isOnline = false;
            hideProgress();
        }
        return isOnline;
    }

    private boolean isNetworkAvailable(Context context) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


    private void hideProgress() {
        progressDialog.dismiss();
    }
}

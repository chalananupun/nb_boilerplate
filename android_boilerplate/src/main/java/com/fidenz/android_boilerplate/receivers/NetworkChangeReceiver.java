package com.fidenz.android_boilerplate.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fidenz.android_boilerplate.listeners.NetworkChangeListener;
import com.fidenz.android_boilerplate.utility.NetworkUtility;

/**
 * @author chalana
 * @created 2023/05/12 | 7:54 AM
 * @contact Chalana.n@fidenz.com | 071 6 359 376
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    private static NetworkChangeListener listener;
    private String TAG = "NetworkChangeReceiver";

    //Network Change Listener should be passed to the Constructor, and when the network state changes, the current state will be passed through the listener.
    public NetworkChangeReceiver(NetworkChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if(listener !=null){
            int status = NetworkUtility.getConnectivityStatusString(context);
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                listener.onResponse(status != NetworkUtility.NETWORK_STATUS_NOT_CONNECTED);
            }
        }
    }
}
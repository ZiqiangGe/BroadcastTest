package com.example.dmdm.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DMDM on 2018/4/14.
 */

public class NetworkUtils {

    private static final String TAG = "NetworkUtils";

    private static List<NetworkChangeListener> sList= new ArrayList();

    private static NetWorkBroadcastReceiver sNetWorkBroadcastReceiver;

    private static boolean isRegister = false;

    private static IntentFilter sIntentFilter;

    private static Context sContext;

    private NetworkUtils()
    {

    }

    /**
     * 初始化
     * @param context
     */
    public static void initial(Context context)
    {
        sIntentFilter = new IntentFilter();
        sIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        sNetWorkBroadcastReceiver = new NetWorkBroadcastReceiver();

    }

    public static void registerNetWorkChange(@NonNull NetworkChangeListener listener)
    {
        sList.add(listener);

        if (!isRegister)
        {
            isRegister = true;
            sContext.registerReceiver(sNetWorkBroadcastReceiver,sIntentFilter);
        }

    }

    public static void removeNetWorkChange(NetworkChangeListener listener)
    {
        if (sList.contains(listener))
        {
            sList.remove(listener);
        }
    }

    static class NetWorkBroadcastReceiver extends BroadcastReceiver
    {

        private long lastChangeTime = 0;
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: " +context);
            long currentTime = System.currentTimeMillis();
            if (currentTime -lastChangeTime <= 800)
            {
                lastChangeTime = currentTime;
                return;
            }

            ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            boolean available = networkInfo != null && networkInfo.isAvailable();
            for (NetworkChangeListener listener : sList)
            {
                listener.onNetworkChangeed(context,available);
            }
        }
    }

    public interface NetworkChangeListener
    {
        void onNetworkChangeed(Context context, boolean isAviable);
    }

}

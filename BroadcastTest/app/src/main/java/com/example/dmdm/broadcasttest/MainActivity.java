package com.example.dmdm.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";

    NetworkChangeReceiver mNetworkChangeReceiver;

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = findViewById(R.id.button);
        Log.d(TAG, "onCreate: "+mButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: "+v.getId());
                Intent intent = new Intent();
                intent.setAction("com.example.dmdm.broadcasttest.MyBroadcastReceiver");
                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                MainActivity.this.sendBroadcast(intent);
            }
        });
    }

    private void registNetwork()
    {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mNetworkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(mNetworkChangeReceiver,intentFilter);
    }

    class NetworkChangeReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: ");
            ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            boolean available = networkInfo != null && networkInfo.isAvailable();
            if (available)
            {
                Toast.makeText(context,"YES",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context,"NO",Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mNetworkChangeReceiver !=null)
        {
            unregisterReceiver(mNetworkChangeReceiver);
        }
    }
}

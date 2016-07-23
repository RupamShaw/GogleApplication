package com.jagdiv.android.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String testing=intent.getStringExtra("testHeloo");
        System.out.println("Broadcast Intent Detected.....");
        Toast.makeText(context, "Broadcast Intent Detected. "+testing,
                Toast.LENGTH_LONG).show();
       // throw new UnsupportedOperationException("Not yet implemented");
    }
}

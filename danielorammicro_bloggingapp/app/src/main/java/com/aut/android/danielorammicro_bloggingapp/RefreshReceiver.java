package com.aut.android.danielorammicro_bloggingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Administrator on 13/08/2015.
 */
public class RefreshReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
        Log.d("RefreshReceiver", "onReceive");
        context.startService( new Intent(context, RefreshService.class));
    }
}

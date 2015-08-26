package com.aut.android.danielorammicro_bloggingapp;

import android.app.Dialog;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Debug;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.marakana.android.yamba.clientlib.YambaClient;

/**
 * Created by cwc8902 on 13/08/2015.
 */
public class RefreshService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    public static final String TAG = "RefreshService";
    private YambaClient yambaClient;
    private FriendsTimeline friendsTimeline;

    public RefreshService(String name) {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Dialog dialog;
        String username = null;
        String password = null;
        String server = null;

        try {
            Debug.startMethodTracing("RefreshService.trace");

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            username = prefs.getString("username",null);
            password = prefs.getString("password",null);
            server = prefs.getString("server",null);

            yambaClient = new YambaClient(username,password);
            if (server != null && server.length() > 0)yambaClient.setApiRoot(server);

            friendsTimeline = new FriendsTimeline();
        } catch (IllegalArgumentException e){
            e.printStackTrace();
        }

        Log.d(TAG, String.format("onCreate() yambaClient with %s:%s@%s", username, password, server));
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onStartCommand");

        //fetches friends timeline from the cloud
        try{
            yambaClient.fetchFriendsTimeline(friendsTimeline); //could take awhile
        } catch (Exception e){
            Log.e(TAG, "Failed to fetch timeline", e);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy");

        Debug.stopMethodTracing();
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    class FriendsTimeline implements YambaClient.TimelineProcessor {

        @Override
        public void onStartProcessingTimeline(){
            Log.d(TAG,"onStartProcessingTimeline");
        }

        @Override
        public void onTimelineStatus(YambaClient.TimelineStatus status){
            Log.d(TAG, String.format("%s: %s", status.getUser(), status.getMessage()));

            ContentValues values = new ContentValues();
            values.put(StatusContract.Columns._ID,status.getId());
            values.put(StatusContract.Columns.CREATED_AT,status.getCreatedAt().getTime());
            values.put(StatusContract.Columns.TEXT, status.getMessage());

            Uri uri = getContentResolver().insert(StatusContract.CONTENT_URI,values);

            //DO we have a new status?
            if (uri != null){
                Intent intent = new Intent("com.marakana.android.yamba.NEW_STATUS");
                intent.putExtra(StatusContract.Columns.TEXT,status.getMessage());
                sendBroadcast(intent);
            }
        }

        @Override
        public boolean isRunnable(){
            return true;
        }

        @Override
        public void onEndProcessingTimeline() {
            Log.d(TAG,"onEndProcessingTimeline");
        }
    }
}



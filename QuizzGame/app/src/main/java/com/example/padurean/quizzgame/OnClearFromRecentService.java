package com.example.padurean.quizzgame;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import static android.app.Service.START_NOT_STICKY;

/**
 * Created by Asus on 20.06.2017.
 */

public class OnClearFromRecentService extends Service {

    public OnClearFromRecentService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ClearFromRecentService", "Service Started");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ClearFromRecentService", "Service Destroyed");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearFromRecentService", "END");
        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(
                new Intent(MainActivity.FILTER).putExtra(MainActivity.KEY, "cevaa")
        );
        stopSelf();
    }
}
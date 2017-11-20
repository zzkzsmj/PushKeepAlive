package com.example.zzk.pushkeepalive.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.zzk.pushkeepalive.utils.PushUtils;

/**
 * Created by zhaozhikang on 2017/11/3.
 */

public class PushService extends Service{

    private static final String TAG = "ALive";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags, startId);
        if (PushUtils.DEBUG_TAG)
            Log.d(TAG, "PushService:onStartCommand()");
        ForegroundService.setForeground(this);
        startService(new Intent(this,ForegroundService.class));
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

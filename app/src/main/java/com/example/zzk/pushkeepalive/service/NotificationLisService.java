package com.example.zzk.pushkeepalive.service;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.zzk.pushkeepalive.manage.KeepLiveManager;
import com.example.zzk.pushkeepalive.utils.AliveChannel;
import com.example.zzk.pushkeepalive.utils.PushUtils;

/**
 * Created by zhaozhikang on 2017/11/4.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationLisService extends NotificationListenerService {

    private static final String TAG = "ALive";
    private final String LOGSTR = "NotificationLisService";
    // NLService的状态，可以设置enable,disable
    public static final String SET_ENABLED_ACTION = "nl_service_status";
    public static final String ACTION_POSTED = "on_notification_posted";
    public static final String ACTION_REMOVED = "on_notification_removed";
    public static final String KEY_NOTIFICATION = "StatusBarNotification";
    // intent action
    // SET_ENABLED_ACTION需要带的参数, value为boolean
    public static final String INTENT_SET_ENABLED = "enable";

    private static boolean sServiceRunning;

    private BroadcastReceiver mReceiver;

    private boolean mInit;

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (PushUtils.DEBUG_TAG)
                Log.d(TAG, "NotificationLisService-onDestroy");

            sServiceRunning = false;
            if (mReceiver != null) {
                LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
                mReceiver = null;
            }
        } catch (Exception e) {
            if (PushUtils.DEBUG_TAG) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (PushUtils.DEBUG_TAG) {
            Log.d(TAG, "NotificationLisService - onBind ");
        }
        return super.onBind(intent);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (PushUtils.DEBUG_TAG) {
            Log.d(TAG, "NotificationLisService - onCreate ");
        }
        doCreate();
    }

    private void doCreate() {
        try{
            startSelf();
        }catch (Exception e){
            if (PushUtils.DEBUG_TAG)
                Log.d(TAG,"NotificationLisService -- Exc :"+e.getMessage());
        }

        if (mInit) {
            return;
        }
        mInit = true;

        if (PushUtils.DEBUG_TAG) {
            Log.d(TAG, "NotificationLisService - doCreate ");
        }
        sServiceRunning = true;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.d(TAG,"NotificationLisService -- open :"+sbn.toString());
        try{
            startSelf();
        }catch (Exception e){
            if (PushUtils.DEBUG_TAG)
                Log.d(TAG,"NotificationLisService -- Exc :"+e.getMessage());
        }
        if (PushUtils.DEBUG_TAG) {
            Log.d(TAG, "onNotificationPosted");
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.d(TAG,"NotificationLisService -- remove :"+sbn.toString());
        try{
            startSelf();
        }catch (Exception e){
            if (PushUtils.DEBUG_TAG)
                Log.d(TAG,"NotificationLisService -- Exc :"+e.getMessage());
        }
        if (PushUtils.DEBUG_TAG) {
            Log.d(TAG, "onNotificationRemoved");
        }
    }

    public static boolean isRunning() {
        return sServiceRunning;
    }

    private void startSelf(){
        KeepLiveManager.getInstance().startKeepAlive(AliveChannel.NOTIFICATION_SYS);
    }
}

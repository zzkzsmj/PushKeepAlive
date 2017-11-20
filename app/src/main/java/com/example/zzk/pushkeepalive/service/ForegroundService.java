package com.example.zzk.pushkeepalive.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

/**
 * Created by zhaozhikang on 2017/11/17.
 */

public class ForegroundService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        setForeground(this);
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }

    public static void setForeground(Service service){
        final int foregroundPushId=200;
        if(service!=null){
            if(Build.VERSION.SDK_INT< Build.VERSION_CODES.JELLY_BEAN_MR2){
                service.startForeground(foregroundPushId,new Notification());
            }else if(Build.VERSION.SDK_INT<= Build.VERSION_CODES.N){
                NotificationCompat.Builder builder = new NotificationCompat.Builder(service);
                service.startForeground(foregroundPushId,builder.build());
            }
        }
    }

    public void setForeground(final Service keepLiveService, final Service innerService){
        final int foregroundPushId=200;
        if(keepLiveService!=null){
            if(Build.VERSION.SDK_INT< Build.VERSION_CODES.JELLY_BEAN_MR2){
                keepLiveService.startForeground(foregroundPushId,new Notification());
            }else if(Build.VERSION.SDK_INT<= Build.VERSION_CODES.N){
                NotificationCompat.Builder builder = new NotificationCompat.Builder(keepLiveService);
                Notification notify=builder.build();
                keepLiveService.startForeground(foregroundPushId,notify);
                if(innerService!=null){
                    innerService.startForeground(foregroundPushId,notify);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SystemClock.sleep(1000);
                            innerService.stopForeground(true);
                            NotificationManager manager=(NotificationManager) keepLiveService.getSystemService(Context.NOTIFICATION_SERVICE);
                            manager.cancel(foregroundPushId);
                            innerService.stopSelf();
                        }
                    }).start();
                }
            }
        }
    }
}

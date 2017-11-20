package com.example.zzk.pushkeepalive.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Created by zhaozhikang on 2017/11/3.
 */

public class ScreenReceiverUtil {
    private Context mContext;
    private SreenBroadcastReceiver mScreenReceiver;
    private SreenStateListener mStateReceiverListener;
    private static final String TAG = "ALive";
    public ScreenReceiverUtil(Context mContext){
        this.mContext = mContext;
    }

    public void setScreenReceiverListener(SreenStateListener mStateReceiverListener){
        this.mStateReceiverListener = mStateReceiverListener;
        this.mScreenReceiver = new SreenBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        mContext.registerReceiver(mScreenReceiver,filter);
    }

    public void stopScreenReceiverListener(){
        mContext.unregisterReceiver(mScreenReceiver);
    }

    public  class SreenBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG,"SreenLockReceiver-->："+action);
            if(mStateReceiverListener == null){
                return;
            }
            if(Intent.ACTION_SCREEN_ON.equals(action)){         // 开屏
                mStateReceiverListener.onSreenOn();
            }else if(Intent.ACTION_SCREEN_OFF.equals(action)){  // 锁屏
                mStateReceiverListener.onSreenOff();
            }else if(Intent.ACTION_USER_PRESENT.equals(action)){ // 解锁
                mStateReceiverListener.onUserPresent();
            }
        }
    }

    public interface SreenStateListener {
        void onSreenOn();
        void onSreenOff();
        void onUserPresent();
    }
}

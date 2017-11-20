package com.example.zzk.pushkeepalive.manage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.zzk.pushkeepalive.ui.SinglePixelActivity;
import com.example.zzk.pushkeepalive.utils.PushUtils;

import java.lang.ref.WeakReference;

/**
 * Created by zhaozhikang on 2017/11/3.
 */

public class ScreenManager {
    private static final String TAG = "ALive";
    private Context mContext;
    private static ScreenManager mSreenManager;
    private WeakReference<Activity> mActivityRef;

    private ScreenManager(Context mContext){
        this.mContext = mContext;
    }

    public static ScreenManager getScreenManagerInstance(Context context){
        if(mSreenManager == null){
            mSreenManager = new ScreenManager(context);
        }
        return mSreenManager;
    }

    public void setSingleActivity(Activity mActivity){
        mActivityRef = new WeakReference<>(mActivity);
    }

    public void startActivity(){
        if (PushUtils.DEBUG_TAG)
            Log.d(TAG,"start one...");
        Intent intent = new Intent(mContext,SinglePixelActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    public void finishActivity(){
        if (PushUtils.DEBUG_TAG)
            Log.d(TAG,"finish one...");
        if(mActivityRef != null){
            Activity mActivity = mActivityRef.get();
            if(mActivity != null){
                mActivity.finish();
            }
        }
    }
}

package com.example.zzk.pushkeepalive;

import android.app.Application;
import android.content.Context;
import com.example.zzk.pushkeepalive.manage.KeepLiveManager;

/**
 * Created by zhaozhikang on 2017/11/20.
 */

public class KeepApplication extends Application {

    public static Context mContext;

    public static Context getAppContext() {
        return mContext;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        KeepLiveManager.getInstance().initManager(this);
    }
}

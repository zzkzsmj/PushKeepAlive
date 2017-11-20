package com.example.zzk.pushkeepalive.manage;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.zzk.pushkeepalive.service.PushService;
import com.example.zzk.pushkeepalive.utils.AccountSyncHelper;
import com.example.zzk.pushkeepalive.utils.AliveChannel;
import com.example.zzk.pushkeepalive.utils.PushUtils;
import com.example.zzk.pushkeepalive.utils.ScreenReceiverUtil;

/**
 * Created by zhaozhikang on 2017/11/3.
 */

public class KeepLiveManager {

    private static final String TAG = "Alive";

    private Context mContext;
    private static KeepLiveManager mKeepLiveManager=null;
    private JobSchedulerManager mJobManager;
    private ScreenReceiverUtil mScreenListener;
    private ScreenManager mScreenManager;

    public static KeepLiveManager getInstance(){
        if(mKeepLiveManager==null)
            mKeepLiveManager=new KeepLiveManager();
        return mKeepLiveManager;
    }

    public void initManager(Context context){
        this.mContext=context;
        startKeepService();
        initJobScheduler(context);

        mScreenListener = new ScreenReceiverUtil(context);
        mScreenManager = ScreenManager.getScreenManagerInstance(context);
        mScreenListener.setScreenReceiverListener(mScreenListenerer);

        AccountSyncHelper.addMobileSafeAccount(mContext, PushUtils.SYNC_TIME);

    }
    private void initJobScheduler(Context context){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            mJobManager = JobSchedulerManager.getJobSchedulerInstance(context);
            mJobManager.startJobScheduler();
        }
    }
    private void startKeepService(){
        mContext.startService(new Intent(mContext, PushService.class));
    }

    public void startKeepAlive(int channel){
        if(!PushUtils.checkAPPALive(mContext, PushUtils.PACKAGE_NAME)){
            startKeepService();
            if (PushUtils.DEBUG_TAG)
                Log.d(TAG, "relive:"+ AliveChannel.getChannelName(channel));
        }
    }

    private ScreenReceiverUtil.SreenStateListener mScreenListenerer = new ScreenReceiverUtil.SreenStateListener() {
        @Override
        public void onSreenOn() {
            mScreenManager.finishActivity();
        }

        @Override
        public void onSreenOff() {
            mScreenManager.startActivity();
        }

        @Override
        public void onUserPresent() {

        }
    };
}

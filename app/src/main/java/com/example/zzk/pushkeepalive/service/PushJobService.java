package com.example.zzk.pushkeepalive.service;

import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;

import com.example.zzk.pushkeepalive.manage.KeepLiveManager;
import com.example.zzk.pushkeepalive.utils.AliveChannel;

/**
 * Created by zhaozhikang on 2017/11/3.
 */
@RequiresApi(api= Build.VERSION_CODES.LOLLIPOP)
public class PushJobService extends JobService {
    
    private static final String TAG = "ALive";
    private volatile static Service mKeepAliveService = null;
    private static final int MESSAGE_ID_TASK = 0x01;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            KeepLiveManager.getInstance().startKeepAlive(AliveChannel.JOB_SERVICE);
            jobFinished( (JobParameters) msg.obj, false );
            return true;
        }
    });
    public static boolean isJobServiceAlive(){
        return mKeepAliveService != null;
    }
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Message msg = Message.obtain(mHandler, MESSAGE_ID_TASK, jobParameters);
        mHandler.sendMessage(msg);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        mHandler.removeMessages(MESSAGE_ID_TASK);
        return false;
    }
}

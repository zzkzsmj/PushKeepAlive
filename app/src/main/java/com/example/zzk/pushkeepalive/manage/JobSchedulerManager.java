package com.example.zzk.pushkeepalive.manage;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

import com.example.zzk.pushkeepalive.service.PushJobService;

/**
 * Created by zhazohikang on 2017/11/3.
 */
@TargetApi(21)
public class JobSchedulerManager {
    private static final int JOB_ID = 1;
    private static JobSchedulerManager mJobManager;
    private JobScheduler mJobScheduler;
    private static Context mContext;

    private JobSchedulerManager(Context ctxt){
        this.mContext = ctxt;
        mJobScheduler = (JobScheduler)ctxt.getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    public final static JobSchedulerManager getJobSchedulerInstance(Context ctxt){
        if(mJobManager == null){
            mJobManager = new JobSchedulerManager(ctxt);
        }
        return mJobManager;
    }

    @TargetApi(21)
    public void startJobScheduler(){
        if(PushJobService.isJobServiceAlive() || isBelowLOLLIPOP()){
            return;
        }
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID,new ComponentName(mContext, PushJobService.class));
        builder.setPeriodic(3000);
//        builder.setPersisted(true);
        builder.setRequiresCharging(true);
        JobInfo info = builder.build();
        mJobScheduler.schedule(info);
    }

    @TargetApi(21)
    public void stopJobScheduler(){
        if(isBelowLOLLIPOP())
            return;
        mJobScheduler.cancelAll();
    }

    private boolean isBelowLOLLIPOP(){
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }
}

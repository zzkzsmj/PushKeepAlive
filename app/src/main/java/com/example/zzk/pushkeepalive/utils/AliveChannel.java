package com.example.zzk.pushkeepalive.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhaozhikang on 2017/11/4.
 */

public class AliveChannel {
    public static int NOTIFICATION_SYS=1;
    public static int JOB_SERVICE=5;
    public static int FOREGROUND_SERVICE=10;
    public static int SYS_BROADCAST=15;
    public static int ONE_PIXEL=20;
    private static Map mMap=new HashMap();

    private static void initData(){
        if(mMap!=null)
            return;
        mMap.put(1,NOTIFICATION_SYS);
        mMap.put(5,JOB_SERVICE);
        mMap.put(10,FOREGROUND_SERVICE);
        mMap.put(15,SYS_BROADCAST);
        mMap.put(20,ONE_PIXEL);
    }
    public static String getChannelName(int channel){
        initData();
        return (String)mMap.get(channel);
    }
}

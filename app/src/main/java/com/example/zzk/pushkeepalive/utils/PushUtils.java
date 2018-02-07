package com.example.zzk.pushkeepalive.utils;

import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.zzk.pushkeepalive.R;

import java.util.List;

/**
 * Created by zhaozhikang on 2017/11/3.
 */

public class PushUtils {

    private static final String TAG = "Alive";
    public static boolean DEBUG_TAG = true;
    private static final String NL_RW_REQUEST_SHOW_KET = "nl_rw_request";
    private static final String NL_OPEN_REQUEST_SHOW_KET = "nl_open_request";
    public static String PACKAGE_NAME = "com.example.zzk.pushkeepalive";
    public static long SYNC_TIME = 30;
    private static Context mContext;

    public static boolean checkAPPALive(Context mContext, String packageName) {
        boolean isAPPRunning = false;
        if(mContext!=null){
            ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
            if(activityManager!=null){
                List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = activityManager.getRunningAppProcesses();
                if(appProcessInfoList!=null){
                    for (ActivityManager.RunningAppProcessInfo appInfo : appProcessInfoList) {
                        if (packageName.equals(appInfo.processName)) {
                            isAPPRunning = true;
                            break;
                        }
                    }
                }
                if(DEBUG_TAG)
                    Log.d(TAG, "checkAPPALive:" + isAPPRunning);
            }
        }
        return isAPPRunning;
    }

    public static void checkRequestsForPermissions(Context context) {
        if(context==null)
            return;
        mContext = context;
        if (!SPUtils.getInstance().getBoolean(NL_OPEN_REQUEST_SHOW_KET, false)
                && ifNotifyPermissionsDenied(context))
            requestNLOpenPermissions(mContext);
        else if (!SPUtils.getInstance().getBoolean(NL_RW_REQUEST_SHOW_KET, false))
            handleNLReadWritePermissions();
    }
//
    public static boolean ifNotifyPermissionsDenied(Context context) {
        boolean denied = false;
        if (context!=null&&Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (NotificationManagerCompat.from(context).areNotificationsEnabled())
                return false;
            NotificationManager manager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (null != manager && Build.VERSION.SDK_INT >= 24) {
                int importance = manager.getImportance();
                if (importance == NotificationManager.IMPORTANCE_NONE ||
                        importance == NotificationManager.IMPORTANCE_MIN) {
                    denied = true;
                }
            }
        }
        return denied;
    }

    public static void handleNLReadWritePermissions() {
        if (!NLServiceHelper.getNotiSetting(mContext)) {
            requestNLReadWritePermissions(mContext);
        } else {
            Log.d(TAG, "handleNLReadWritePermissions--NL already setting");
        }
    }

    public static boolean checkNLReadWritePermissions() {
        return NLServiceHelper.getNotiSetting(mContext);
    }

    public static void requestNLOpenPermissions(Context context) {
        if(context==null)
            return;
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setIcon(R.mipmap.ic_launcher);
        normalDialog.setTitle("温馨提示");
        normalDialog.setMessage("检测到您的通知栏被禁用，建议您开启");
        normalDialog.setPositiveButton("前往",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NLServiceHelper.startNotifySwitchPager(mContext);
                        handleNLReadWritePermissions();
                        SPUtils.getInstance().setBoolean(NL_OPEN_REQUEST_SHOW_KET, true);
                        Log.d(TAG, "NL switch");
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handleNLReadWritePermissions();
                        SPUtils.getInstance().setBoolean(NL_OPEN_REQUEST_SHOW_KET, true);
                        dialog.dismiss();
                    }
                });
        normalDialog.show();
    }

    public static void requestNLReadWritePermissions(final Context context) {
        if(context==null)
            return;
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setIcon(R.mipmap.ic_launcher);
        normalDialog.setTitle("温馨提示");
        normalDialog.setMessage("检测到您还未开启通知权限，建议您开启");
        normalDialog.setPositiveButton("前往",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NLServiceHelper.setEnable(1);
                        NLServiceHelper.startNotifySettingPager(context);
                        SPUtils.getInstance().setBoolean(NL_RW_REQUEST_SHOW_KET, true);
                        Log.d(TAG, "NL switch");
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SPUtils.getInstance().setBoolean(NL_RW_REQUEST_SHOW_KET, true);
                    }
                });
        normalDialog.show();
    }
}

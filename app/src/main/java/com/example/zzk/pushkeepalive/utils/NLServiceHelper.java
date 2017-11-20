package com.example.zzk.pushkeepalive.utils;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import com.example.zzk.pushkeepalive.KeepApplication;
import com.example.zzk.pushkeepalive.service.NotificationLisService;
import java.util.HashSet;
import java.util.Iterator;


/**
 * Created by zhaozhikang on 2017/11/4.
 */
public class NLServiceHelper {

    private static final String TAG = "ALive";

    /**
     * @param enable 1为开， 0为关
     */
    public static void setEnable(int enable) {
        // NLService只在api18以上生效
        if (Build.VERSION.SDK_INT < 18) {
            return;
        }
        try{
            Context ctx = KeepApplication.getAppContext();
            PackageManager pm = ctx.getPackageManager();
            if (pm == null) {
                return;
            }
            final ComponentName component = new ComponentName(ctx, NotificationLisService.class);
            if (enable != 0) {
                pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
            } else {
                pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
            }
        }catch(Exception e){
            if (PushUtils.DEBUG_TAG)
                Log.d(TAG,"NLServiceHelper - setEnable Exception :"+e.getMessage() );
        }
    }

    /**
     * NLService是否可用
     *
     * @return
     */
    public static boolean isEnabled() {
        // NLService只在api18以上生效
        if (Build.VERSION.SDK_INT < 18) {
            return false;
        }
        Context ctx =KeepApplication.getAppContext();
        PackageManager pm = ctx.getPackageManager();
        if (pm == null) {
            return false;
        }
        ComponentName component = new ComponentName(ctx, NotificationLisService.class);
        boolean r = (pm.getComponentEnabledSetting(component) == 1);

        return r;
    }

    public static void startNotifySettingPager(Context aContext) {
        if (null == aContext)
            return;
        Intent aT = new Intent();
        aT.setAction("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");

        if (aContext.getPackageManager().resolveActivity(aT, 0) != null) {
            aContext.startActivity(aT);
       }
    }

    public static void startNotifySwitchPager(Context context){
        if (null == context)
            return;
        Intent intent = new Intent();
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("app_package", context.getPackageName());
        intent.putExtra("app_uid", context.getApplicationInfo().uid);
        context.startActivity(intent);
    }

    public static boolean HasNotifPerm;

    /**
     * 通过查询setting的db来获取用户使用设置了读取通知栏权限
     */
    private static HashSet mEnabledListeners = new HashSet();
    public synchronized static boolean getNotiSetting(Context aContext) {
        if (null == aContext)
            return false;
        HasNotifPerm = false;
        // find this value in setting db.
        ContentResolver mContentResolver = aContext.getContentResolver();
        String aValue = Settings.Secure.getString(mContentResolver, "enabled_notification_listeners");
        if (PushUtils.DEBUG_TAG) {
            Log.d(TAG, "getNotiSetting Name--> " + aValue);
        }

        if (aValue != null && !"".equals(aValue)) {
            String[] listenersStringArray = aValue.split(":");

            for (int i = 0; i < listenersStringArray.length; ++i) {
                ComponentName cn = ComponentName.unflattenFromString(listenersStringArray[i]);
                if (cn != null) {
                    mEnabledListeners.add(cn);
                    // 是否匹配
                    if (cn.getPackageName() != null && cn.getPackageName().compareToIgnoreCase(
                            aContext.getPackageName()) == 0) {
                        HasNotifPerm = true;
                        if (PushUtils.DEBUG_TAG) {
                            Log.d(TAG, "getNotiSetting --> " + HasNotifPerm);
                        }
                        return HasNotifPerm;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 尝试去直接往Setting的Db中写入(失败的几率较高)
     */
    public static void saveEnabledListeners(Context aContext) {
//        String listenersValueString;
        if (!ShellUtils.checkRootPermission() || null == aContext) {
            return;
        }
        try{
            StringBuilder builder = null;
            Iterator iter = mEnabledListeners.iterator();
            if(null != iter) {
                while (iter.hasNext()) {
                    Object componentName = iter.next();
                    if (builder == null) {
                        builder = new StringBuilder();
                    } else {
                        builder.append(':');
                    }
                    builder.append(((ComponentName) componentName).flattenToString());
                }
                if(null == builder)
                    builder = new StringBuilder();

                builder.append(":" + aContext.getPackageName() + "/com.qihoo360.antilostwatch.service.NotificationLisService");

                String cmd = "content call --uri content://settings/secure --method PUT_secure --arg enabled_notification_listeners --extra _user:i:0 --extra value:s:" +
                        builder.toString();
                ShellUtils.CommandResult aResult = ShellUtils.execCommand(cmd, true);
                if (null != aResult && aResult.result == 0) {
                    HasNotifPerm = true;
                }
            }
        }catch (Exception e){
            if (PushUtils.DEBUG_TAG) {
                e.printStackTrace();
            }
        }
    }

}

package com.example.zzk.pushkeepalive.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.util.Log;
import com.example.zzk.pushkeepalive.manage.KeepLiveManager;
import com.example.zzk.pushkeepalive.utils.AliveChannel;
import com.example.zzk.pushkeepalive.utils.PushUtils;

/**
 * Created by zhaozhikang on 2017/11/3.
 */

public class SystemBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "Alive";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (PushUtils.DEBUG_TAG)
            Log.d(TAG,"SystemBroadcastReceiver---->SystemBroadcast："+action);
        getNetworkBroadcast(context,intent);
        KeepLiveManager.getInstance().startKeepAlive(AliveChannel.SYS_BROADCAST);
        if (PushUtils.DEBUG_TAG)
            Log.i(TAG,"SystemBroadcastReceiver---->revice app");
    }

    private void getNetworkBroadcast(Context context, Intent intent){
        String action = intent.getAction();
        if(WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)){
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,0);
            switch (wifiState){
                case WifiManager.WIFI_STATE_DISABLED:
                    if (PushUtils.DEBUG_TAG)
                        Log.i(TAG,"NetworkBroadcast---->wifi close");
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    if (PushUtils.DEBUG_TAG)
                        Log.i(TAG,"NetworkBroadcast---->wifi open");
                    break;
                default:
                    break;
            }
        }
        if(WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)){
            Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if(null != parcelableExtra){
                NetworkInfo networkInfo = (NetworkInfo)parcelableExtra;
                NetworkInfo.State state = networkInfo.getState();
                boolean isConnected = state == NetworkInfo.State.CONNECTED;
                if(isConnected){
                    Log.i(TAG,"NetworkBroadcast---->Connecting WiFi devices");
                }
            }
        }
        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(action)){
            ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(manager!=null){
                NetworkInfo gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if(gprs!=null&&gprs.isConnected()){
                    Log.i(TAG,"NetworkBroadcast---->Mobile network open");
                }else {
                    Log.i(TAG,"NetworkBroadcast---->Mobile network close");
                }
            }
        }
    }
}

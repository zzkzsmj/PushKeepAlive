package com.example.zzk.pushkeepalive.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import com.example.zzk.pushkeepalive.manage.KeepLiveManager;
import com.example.zzk.pushkeepalive.manage.ScreenManager;
import com.example.zzk.pushkeepalive.utils.AliveChannel;

/**
 * Created by zhazohikang on 2017/11/3.
 */

public class SinglePixelActivity extends AppCompatActivity {
    private static final String TAG = "Alive";
    private final int X=0;
    private final int Y=0;
    private final int HEIGHT=10;
    private final int WIDTH=10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"start one alive");
        Window mWindow = getWindow();
        mWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams attrParams = mWindow.getAttributes();
        attrParams.x = X;
        attrParams.y = Y;
        attrParams.height = HEIGHT;
        attrParams.width = HEIGHT;
        mWindow.setAttributes(attrParams);
        ScreenManager.getScreenManagerInstance(this).setSingleActivity(this);
    }

    @Override
    protected void onDestroy() {
        KeepLiveManager.getInstance().startKeepAlive(AliveChannel.ONE_PIXEL);
        super.onDestroy();
    }
}

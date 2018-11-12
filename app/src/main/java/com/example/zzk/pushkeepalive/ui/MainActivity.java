package com.example.zzk.pushkeepalive.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.zzk.pushkeepalive.R;
import com.example.zzk.pushkeepalive.utils.PushUtils;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }
    private void initView(){


    }
    private void initData(){
        PushUtils.checkRequestsForPermissions(this);
    }
}

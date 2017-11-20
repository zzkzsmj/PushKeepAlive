package com.example.zzk.pushkeepalive.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.zzk.pushkeepalive.KeepApplication;

/**
 * Created by zhaozhikang on 2017/11/19.
 */

public class SPUtils {

    private static SPUtils sInstance;

    private SharedPreferences mPreference;

    private static final String PREFREENCE = "push_keep_preference";

    private SPUtils(){
        mPreference = KeepApplication.getAppContext().getSharedPreferences(PREFREENCE, Context.MODE_PRIVATE);
    }
    
    public synchronized static SPUtils getInstance(){
        if(null == sInstance){
            sInstance = new SPUtils();
        }
        return sInstance;
    }

    public String getString(String key, String defaultValue) {
        return mPreference.getString(key, defaultValue);
    }

    public void setString(String key, String value) {
        mPreference.edit().putString(key, value).apply();

        String aTemp = mPreference.getString(key,"");
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mPreference.getBoolean(key, defaultValue);
    }

    public void setBoolean(String key, boolean value) {
        mPreference.edit().putBoolean(key, value).apply();
    }

    public void setInt(String key, int value) {
        mPreference.edit().putInt(key, value).apply();
    }

    public int getInt(String key, int defaultValue) {
        return mPreference.getInt(key, defaultValue);
    }

    public void setFloat(String key, float value) {
        mPreference.edit().putFloat(key, value).apply();
    }

    public float getFloat(String key, float defaultValue) {
        return mPreference.getFloat(key, defaultValue);
    }

    public void setLong(String key, long value) {
        mPreference.edit().putLong(key, value).apply();
    }

    public long getLong(String key, long defaultValue) {
        return mPreference.getLong(key, defaultValue);
    }

    public boolean isHas(String key) {
        return mPreference.contains(key);
    }

    public void remove(String key) {
        mPreference.edit().remove(key).apply();
    }

}

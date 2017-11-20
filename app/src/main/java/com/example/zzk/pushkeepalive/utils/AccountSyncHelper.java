package com.example.zzk.pushkeepalive.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.example.zzk.pushkeepalive.R;

/**
 * Created by zhaozhikang on 2017/11/3.
 */

public class AccountSyncHelper {

    private static final String TAG = "ALive";
    public static final String ACCOUNT_TYPE = "com.example.zzk.pushkeepalive.account.type";

    public static boolean isMobilseSafeAccountExist(Context c) {
        return isMobilseSafeAccountExist(AccountManager.get(c), c.getString(R.string.app_name),
                ACCOUNT_TYPE);
    }

    private static boolean isMobilseSafeAccountExist(AccountManager mAccountManager, String name,
                                                     String account_type) {

        if (mAccountManager == null || TextUtils.isEmpty(name)) {
            return false;
        }
        Account[] accounts = mAccountManager.getAccountsByType(account_type);
        if (accounts == null) {
            return false;
        }

        for (Account account : accounts) {
            if (account != null && name.equalsIgnoreCase(account.name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean addMobileSafeAccount(Context c, long syncTime) {
        Log.d(TAG, "addMobileSafeAccount() ");
        try {
            if (syncTime > 0) {
                // set account
                Account account = new Account(c.getString(R.string.app_name),ACCOUNT_TYPE);
                AccountManager mAccountManager = AccountManager.get(c);

                if (!isMobilseSafeAccountExist(c)) {
                    // 如果同步总开关关闭 则先开总开关
                    if (!ContentResolver.getMasterSyncAutomatically())
                        ContentResolver.setMasterSyncAutomatically(true);
                    boolean r = mAccountManager.addAccountExplicitly(account, "", null);
                    if (PushUtils.DEBUG_TAG)
                        Log.d(TAG, "addMobileSafeAccount ---> " + r);
                    ContentResolver.setSyncAutomatically(account, AccountProvider.AUTHORITY, true);
                    ContentResolver.addPeriodicSync(account, AccountProvider.AUTHORITY, new Bundle(), syncTime);
                } else {
                    if (PushUtils.DEBUG_TAG)
                        Log.d(TAG, "addMobileSafeAccount --MobileSafeAccount already exist-> ");
                }
                return true;
            }
        } catch (Exception e) {
            if (PushUtils.DEBUG_TAG)
                Log.d(TAG, e.getMessage());
        }
        return false;
    }

}

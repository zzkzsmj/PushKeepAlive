package com.example.zzk.pushkeepalive.service;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.example.zzk.pushkeepalive.utils.PushUtils;

/**
 * Created by zhaozhikang on 2017/11/4.
 */

public class SystemAccountService extends Service {

    private static final String TAG = "ALive";
    private SystemAccountService.AccountAuthenticator mAuthenticator;
    private SystemAccountService.AccountAuthenticator getAuthenticator() {
        if (mAuthenticator == null)
            mAuthenticator = new SystemAccountService.AccountAuthenticator(this);
        return mAuthenticator;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        if (PushUtils.DEBUG_TAG)
            Log.d(TAG, "SystemAccountService - onCreate ");
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (PushUtils.DEBUG_TAG)
            Log.d(TAG, "SystemAccountService - onBind ");
        return getAuthenticator().getIBinder();
    }
    class AccountAuthenticator extends AbstractAccountAuthenticator {

        private final Context mContext;

        public AccountAuthenticator(Context context) {
            super(context);
            mContext = context;
        }

        @Override
        public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
            return null;
        }

        @Override
        public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options)
                throws NetworkErrorException {
            return options;
        }

        @Override
        public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
            return null;
        }

        @Override
        public String getAuthTokenLabel(String authTokenType) {
            return null;
        }

        @Override
        public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
            return null;
        }
    }
}

package com.fvt.fatburnproject;

import android.util.Log;

import androidx.viewbinding.BuildConfig;

public class StripsIDsClass {
    static String TAG="***StripeKey";
    private final static String testsecretKey = "sk_test_t2qvlU7H89Zpv06cvs9CSGxf00QK7CthR0";
    private final static String testpublishableKey = "pk_test_hUE52VSBGt7oUf7AORrcPRpD0024H5exOY";
    private final static String secretKey = "sk_live_1OPVbpFWuEOpOYP6hRpHSJPX008E7JJqDs";
    private final static String publishableKey = "pk_live_oshvc2Ez8B14PrQybrrIsqcM00PbaBGPDm";

    public static String getSecretEKey(){
        if (BuildConfig.DEBUG) {
            Log.e(TAG,"bebug apk ");
            return testsecretKey;
        }else{
            Log.e(TAG,"realse apk ");
            return secretKey;
        }
    }
    public static String getPubliserKey(){
        if (BuildConfig.DEBUG) {
            Log.e(TAG,"bebug apk ");
            return testpublishableKey;
        }else{
            Log.e(TAG,"realse apk ");
            return publishableKey;
        }
    }
}

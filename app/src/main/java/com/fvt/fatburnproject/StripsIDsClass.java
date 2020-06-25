package com.fvt.fatburnproject;

import android.util.Log;

public class StripsIDsClass {
    static String TAG="***StripeKey";
    private final static String testsecretKey = "sk_test_t2qvlU7H89Zpv06cvs9CSGxf00QK7CthR0";
    private final static String testpublishableKey = "pk_test_hUE52VSBGt7oUf7AORrcPRpD0024H5exOY";
    private final static String secretKey = "sk_live_rUioCqAlWqUJCSPAShuax3Yn00sGNulDvH";
    private final static String publishableKey = "pk_live_r25t3BcxBOyRV6T3DxjVbBGU008wbuuIcN";

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

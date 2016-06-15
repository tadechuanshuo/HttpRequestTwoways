package com.example.qiuqing.request;

import com.example.qiuqing.Myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * 网络判断工具类
 * Created by Zhp on 2014/7/3.
 */
public class NetworkUtils {

    /**
     * 获取网络状况
     *
     * @return true代表高速网络，false代表低速，null为WIFI
     */
    public static Boolean getNetWorkType() {
        NetworkInfo mNetworkInfo = getNetworkInfo();
        if (mNetworkInfo != null) {
            if (mNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return null;
            } else if (mNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (mNetworkInfo.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        return false;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        return true;
                    default:
                        return false;
                }
            }
        }
        return false;
    }

    public static boolean isWIfi() {
        return getNetworkInfo() == null;
    }

    public static boolean isConnected() {
        NetworkInfo mNetworkInfo = getNetworkInfo();
        return mNetworkInfo != null && mNetworkInfo.isConnected();
    }

    private static NetworkInfo getNetworkInfo() {
        ConnectivityManager mManager = (ConnectivityManager) Myapplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        return mManager.getActiveNetworkInfo();
    }
}

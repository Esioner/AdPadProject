package com.esioner.votecenter.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.esioner.votecenter.MyApplication;

/**
 * @author Esioner
 * @date 2018/1/8
 */

public class Utility {
    public static String getMacAdress() {
        SPUtils spUtils = SPUtils.getInstance();
        return spUtils.getString(Constant.SP_KEY);
    }
}

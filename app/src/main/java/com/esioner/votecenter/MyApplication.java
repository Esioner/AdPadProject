package com.esioner.votecenter;

import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.esioner.votecenter.utils.Constant;
import com.esioner.votecenter.utils.SPUtils;
import com.esioner.votecenter.utils.Utility;

/**
 * @author Esioner
 * @date 2018/1/8
 */

public class MyApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        //判断 mac 是否保存在本地，如果保存在本地，则直接从本地读取
        if ("".equals(Utility.getMacAdress())) {
            WifiManager wifi = (WifiManager) MyApplication.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String macAddress = info.getMacAddress();
            SPUtils.getInstance().putString(Constant.SP_KEY, macAddress);
        }
    }

    /**
     * 获取全局上下文环境
     *
     * @return
     */
    public static Context getContext() {
        return mContext;
    }
}

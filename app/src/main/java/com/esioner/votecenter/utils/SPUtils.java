package com.esioner.votecenter.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.esioner.votecenter.MyApplication;

/**
 * @author Esioner
 * @date 2018/1/8
 */

public class SPUtils {
    public static final String VERSION_ID = "version_id";
    private SharedPreferences sp;
    private static SPUtils spUtils;

    private SPUtils() {
        if (sp == null) {
            sp = MyApplication.getContext().getSharedPreferences("info", Context.MODE_PRIVATE);
        }
    }

    /**
     * 单例初始化
     *
     * @return
     */
    public static SPUtils getInstance() {
        if (spUtils == null) {
            spUtils = new SPUtils();
        }
        return spUtils;
    }

    /**
     * 写入数据
     *
     * @param key
     * @param value
     */
    public void putString(String key, String value) {
        sp.edit().putString(key, value).apply();
    }

    /**
     * 读取数据
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        return sp.getString(key, "");
    }

    /**
     * 写入数据
     *
     * @param key
     * @param value
     */
    public void putInt(String key, int value) {
        sp.edit().putInt(key, value).apply();
    }

    /**
     * 读取数据
     *
     * @param key
     * @return
     */
    public int getInt(String key) {
        return sp.getInt(key, -1);
    }
}

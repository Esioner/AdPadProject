package com.esioner.votecenter.utils;

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

package com.esioner.votecenter.utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Esioner
 * @date 2018/1/4
 */

public class OkHttpUtils {
    private static OkHttpUtils okHttpUtils;
    private OkHttpClient mClient;

    private OkHttpUtils() {
        if (mClient == null) {
            mClient = new OkHttpClient
                    .Builder()
                    .readTimeout(10000, TimeUnit.MILLISECONDS)
                    .connectTimeout(10000, TimeUnit.MILLISECONDS)
                    .writeTimeout(10000, TimeUnit.MILLISECONDS)
                    .build()
            ;
        }
    }

    public static OkHttpUtils getInstance() {
        if (okHttpUtils == null) {
            okHttpUtils = new OkHttpUtils();
        }
        return okHttpUtils;
    }

    /**
     * GET 请求获取数据
     *
     * @param url
     * @param callback
     */
    public void getData(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        mClient.newCall(request).enqueue(callback);
    }

    /**
     * GET 请求获取数据
     *
     * @param url
     */
    public Response download(String url) {
        Response response = null;
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            response = mClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}

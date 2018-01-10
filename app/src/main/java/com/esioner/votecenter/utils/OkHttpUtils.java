package com.esioner.votecenter.utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
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
    public void getDataAsyn(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        mClient.newCall(request).enqueue(callback);
    }

    /**
     * 同步获取
     * @param url
     * @return
     * @throws IOException
     */
    public Response getDataSync(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        return mClient.newCall(request).execute();

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

    public void postVoteData(String url, String jsonData, Callback callback) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        mClient.newCall(request).enqueue(callback);
    }

    /**
     * 发送弹幕
     *
     * @param url
     * @param content
     */
    public Response postBarrage(String url, String content) throws IOException {
        RequestBody requestBody = new FormBody.Builder()
                .add("mac", Utility.getMacAdress())
                .add("content", content)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return mClient.newCall(request).execute();
    }
}

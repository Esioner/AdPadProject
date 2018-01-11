package com.esioner.votecenter.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
                    .readTimeout(100000, TimeUnit.MILLISECONDS)
                    .connectTimeout(100000, TimeUnit.MILLISECONDS)
                    .writeTimeout(100000, TimeUnit.MILLISECONDS)
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
     *
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

    /**
     * 下载apk
     *
     * @param url
     */
    public void downloadApk(String url, final String appName, final DownloadListener downloadListener) {
        Request request = new Request.Builder().url(url).build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //失败回调
                downloadListener.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                FileOutputStream fos;
                InputStream input;
                input = response.body().byteStream();
                //总长度
                long allLength = response.body().contentLength();
                //下载路径
                String downloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "vote" + File.separator + "downloadApk";
                File fileDir = new File(downloadPath);
                if (!fileDir.exists()) {
                    fileDir.mkdir();
                }
                File file = new File(downloadPath, appName);
//                if (file.exists()) {
//                    if (file.length() == allLength) {
//                        downloadListener.onSuccess(file.getAbsolutePath());
//                    } else {
//                        file.delete();
//                    }
//                }
                fos = new FileOutputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                int temp;
                while ((temp = input.read(bytes)) != -1) {
                    fos.write(bytes, 0, temp);
                    len += temp;
                    int progress = (int) (len * 1.0f / allLength * 100);
//                    Log.d("OkHttpUtil", "progress: " + progress);
                    //进度回调
                    downloadListener.onProgress(progress);
                }
                input.close();
                fos.close();
                //成功回调
                downloadListener.onSuccess(file.getAbsolutePath());
            }
        });
    }

    public interface DownloadListener {

        /**
         * 下载失败
         *
         * @param e
         */
        void onFailure(Exception e);

        /**
         * 下载成功
         *
         * @param path 返回下载成功的文件
         */
        void onSuccess(String path);

        /**
         * 下载进度
         *
         * @param progress
         */
        void onProgress(int progress);
    }
}

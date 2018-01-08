package com.esioner.votecenter.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;

import com.esioner.votecenter.utils.OkHttpUtils;

import java.io.File;
import java.io.InputStream;

import okhttp3.Response;

/**
 * @author Esioner
 * @date 2018/1/8
 */

public class DownloadService extends IntentService {


    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
//        intent.getBundleExtra();
        File file;
//        file = new File();

        Response response = OkHttpUtils.getInstance().download("");
        InputStream input = response.body().byteStream();
    }
}

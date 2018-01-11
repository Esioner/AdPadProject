package com.esioner.votecenter.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.esioner.votecenter.MainActivity;
import com.esioner.votecenter.R;
import com.esioner.votecenter.entity.BaseData;
import com.esioner.votecenter.entity.ResponderBackgroundData;
import com.esioner.votecenter.entity.ResponderData;
import com.esioner.votecenter.utils.OkHttpUtils;
import com.esioner.votecenter.utils._URL;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author Esioner
 * @date 2017/12/29
 * 抢答页面
 */

public class ResponderResultFragment extends Fragment {
    private static final String TAG = "ResponderResultFragment";

    private TextView tvResult;
    private Context mContext;
    private ImageView iv;
    private String result;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = ((MainActivity) getActivity()).getContext();
        result = ((MainActivity) getActivity()).getResponderResult();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.responsder_result_fragment_layout, null);
        tvResult = view.findViewById(R.id.tv_result);
        iv = view.findViewById(R.id.iv_responder_result);
        tvResult.setText(result);
        initBackground();
        return view;
    }

    /**
     * 初始化背景
     */
    private void initBackground() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = OkHttpUtils.getInstance().getDataSync(_URL.RESPONDER_BACKGROUND_URL);
                    String jsonBody = response.body().string();
                    Log.d(TAG, "run: " + jsonBody);
                    final ResponderBackgroundData backgroundData = new Gson().fromJson(jsonBody, ResponderBackgroundData.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String imgSrc = "";
                            for (ResponderBackgroundData.Data data : backgroundData.getDatas()) {
                                //"抢答终端背景图片",type": 2
                                if (data.getType() == 2) {
                                    imgSrc = data.getImgSrc();
                                } else {
                                    continue;
                                }
                            }
                            Log.d(TAG, "run: " + imgSrc);
                            Glide.with(mContext).load(imgSrc).into(iv);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    /**
     * 手动获取抢答结果，由webSocket获取，终端无需手动获取
     */
    private void getData() {
        OkHttpUtils.getInstance().getDataAsyn(_URL.RESPONDER_RESULT_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonBody = response.body().string();
                Gson gson = new Gson();
                try {
                    final ResponderData responderData = gson.fromJson(jsonBody, ResponderData.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String name = responderData.getData().getName();
                            tvResult.setText(name);
                        }
                    });
                } catch (Exception e) {
                    final BaseData baseData = gson.fromJson(jsonBody, BaseData.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, baseData.getData(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }

    /**
     * 设置结果
     */
    public void setResult(String text) {
        Log.d(TAG, "setResult: " + text);
        if (tvResult != null) {
            tvResult.setText(text);
            Log.d(TAG, "setResult: " + tvResult.getText().toString());
        }
    }
}

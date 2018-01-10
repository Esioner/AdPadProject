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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.esioner.votecenter.MainActivity;
import com.esioner.votecenter.R;
import com.esioner.votecenter.entity.ResponderBackgroundData;
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

public class ResponderFragment extends Fragment {
    private static final String TAG = "ResponderFragment";
    private Button btnResponder;
    private int projectId;
    private Context mContext;
    private RelativeLayout rvResponderRootView;
    private ImageView iv;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        projectId = ((MainActivity) getActivity()).getProjectId();
        mContext = ((MainActivity) getActivity()).getContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.responsder_fragment_layout, null);
        btnResponder = view.findViewById(R.id.btn_responder);
        rvResponderRootView = view.findViewById(R.id.rl_responder_root_view);
        iv = view.findViewById(R.id.iv_responder);
        initBackground();
        setUnClick();
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
//                            SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
//                                @Override
//                                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
//                                    rvResponderRootView.setBackground(resource);
//                                }
//                            };
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
     * 设置按钮不可被点击
     */
    public void setUnClick() {
        btnResponder.setEnabled(false);
    }

    /**
     * 设置按钮可以被点击
     */
    public void setCanClick() {
        btnResponder.setEnabled(true);
    }
}

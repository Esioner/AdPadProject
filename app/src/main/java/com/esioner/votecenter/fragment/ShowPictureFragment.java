package com.esioner.votecenter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.esioner.votecenter.MainActivity;
import com.esioner.votecenter.R;
import com.esioner.votecenter.entity.TerminalData;
import com.esioner.votecenter.utils.OkHttpUtils;
import com.esioner.votecenter.utils.Utility;
import com.esioner.votecenter.utils._URL;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author Esioner
 * @date 2018/1/10
 */

public class ShowPictureFragment extends Fragment {
    private static final String TAG = "ShowPictureFragment";

    private ImageView ivShowPic;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = ((MainActivity) getActivity()).getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_picture_fragment_layout, null);
        ivShowPic = view.findViewById(R.id.iv_show_picture);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        OkHttpUtils.getInstance().getDataAsyn(_URL.TERMINAL_INFO + Utility.getMacAdress(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonBody = response.body().string();
                Log.d(TAG, "onResponse: " + jsonBody);
                TerminalData terminalData = new Gson().fromJson(jsonBody, TerminalData.class);
//                final String src = terminalData.getData().getSrc();
                final String src = "http://116.62.228.3:8089/adv/material/1e08ec3effa25cac10a06307ad156b0b.jpg";
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(mContext).load(src).into(ivShowPic);
                    }
                });
            }
        });
    }
}

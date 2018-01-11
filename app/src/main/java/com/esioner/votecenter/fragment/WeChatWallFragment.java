package com.esioner.votecenter.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.esioner.votecenter.MainActivity;
import com.esioner.votecenter.R;
import com.esioner.votecenter.adapter.SpaceItemDecoration;
import com.esioner.votecenter.adapter.WeChatWallRecyclerViewAdapter;
import com.esioner.votecenter.entity.BaseData;
import com.esioner.votecenter.entity.WeChatBackgroundData;
import com.esioner.votecenter.entity.WeChatDetailData;
import com.esioner.votecenter.entity.WeChatResultData;
import com.esioner.votecenter.utils.OkHttpUtils;
import com.esioner.votecenter.utils._URL;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * @author Esioner
 * @date 2017/12/29
 */

public class WeChatWallFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "WeChatWallFragment";

    private static int sendTime = 0;

    private RecyclerView rvWeChatWall;
    private WeChatWallRecyclerViewAdapter adapter;
    /**
     * Activity 的 Context
     */
    private Context mContext;
    /**
     * 存放所有数据
     */
    private List<WeChatDetailData> allDatas = new ArrayList<>();
    /**
     * 每次更新获取到的数据
     */
    private List<WeChatDetailData> newDatas;
    private EditText etInput;
    private Button btnSendBarrage;
    private InputMethodManager imm;
    private ImageView iv;
    private Thread bgThread;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = ((MainActivity) getActivity()).getContext();
        imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wechat_wall_fragment_layout, null);
        rvWeChatWall = view.findViewById(R.id.rv_wechat_wall);
        etInput = view.findViewById(R.id.et_input);
        btnSendBarrage = view.findViewById(R.id.btn_send_barrage);
        btnSendBarrage.setOnClickListener(this);
        iv = view.findViewById(R.id.iv_we_chat_bg);
        initBackground();
        updateView();
        return view;
    }

    /**
     * 初始化背景图片
     */
    private void initBackground() {
        //type": 2 抢答终端背景图片"
        bgThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = OkHttpUtils.getInstance().getDataSync(_URL.WE_CHAT_WALL_BACKGROUND_URL);
                    String jsonData = response.body().string();
                    Log.d(TAG, "onResponse: " + jsonData);
                    final WeChatBackgroundData backgroundData = new Gson().fromJson(jsonData, WeChatBackgroundData.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //type": 2 抢答终端背景图片"
                            if (backgroundData.getData().getType() == 0) {
                                String imageSrc = backgroundData.getData().getImgSrc();
                                Glide.with(mContext).load(imageSrc).into(iv);
                            }
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        bgThread.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bgThread.destroy();
    }

    /**
     * 弹吐司
     *
     * @param text
     */
    public void showToast(final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 显示数据
     * 判断该 adapter 是否已存在
     */
    private void updateView() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        if (adapter == null) {
            adapter = new WeChatWallRecyclerViewAdapter(allDatas);
            rvWeChatWall.addItemDecoration(new SpaceItemDecoration(5));
            rvWeChatWall.setLayoutManager(manager);
            rvWeChatWall.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
            //数据更新之后跳转到最新的
            moveToPosition(manager, rvWeChatWall, allDatas.size() - 1);
        }
    }

    /**
     * 更新数据
     *
     * @param list
     */
    public void initData(List<WeChatDetailData> list) {
        newDatas = list;
        allDatas.addAll(newDatas);
        //更新数据
        updateView();
    }

    /**
     * RecyclerView 移动到当前位置，
     *
     * @param manager       设置RecyclerView对应的manager
     * @param mRecyclerView 当前的RecyclerView
     * @param n             要跳转的位置
     */
    public static void moveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {
        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }

    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_barrage:
                final String content = etInput.getText().toString();
                if ("".equals(content.trim())) {
                    Toast.makeText(mContext, "输入不可为空,请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //发送弹幕
                        String url = "http://116.62.228.3:8089/adv/api/wxwall/publish";
                        Log.d(TAG, "onClick点击次数 " + sendTime);
                        sendTime++;
                        try {
                            Response response = OkHttpUtils.getInstance().postBarrage(url, content.trim());
                            String jsonBody = response.body().string();
                            Log.d(TAG, jsonBody);
                            try {
                                WeChatResultData weChatResultData = new Gson().fromJson(jsonBody, WeChatResultData.class);
                                if (weChatResultData.getStatus() == 0) {
                                    //使键盘消失
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //清空输入框内容，并使输入框消失
                                            etInput.setText("");
                                            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                                            Log.d(TAG, "EditText:" + etInput.getText());
                                        }
                                    });
                                    showToast("发送成功");
                                }
                            } catch (Exception e) {
                                BaseData baseData = new Gson().fromJson(jsonBody, BaseData.class);
                                if (baseData.getStatus() == 1) {
                                    showToast(baseData.getData());
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            default:
        }
    }
}

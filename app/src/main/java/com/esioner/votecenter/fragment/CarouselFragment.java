package com.esioner.votecenter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esioner.votecenter.MainActivity;
import com.esioner.votecenter.R;
import com.esioner.votecenter.adapter.ViewPagerAdapter;
import com.esioner.votecenter.entity.CarouselData;
import com.esioner.votecenter.utils.OkHttpUtils;
import com.esioner.votecenter.utils._URL;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author Esioner
 * @date 2018/1/2
 */

public class CarouselFragment extends Fragment implements ViewPagerAdapter.PlayListener {
    private static final String TAG = "CarouselFragment";

    private ViewPager viewPager;
    private int mPosition = 0;


    public static final int SWITCH_PAGE_NEXT = 1;
    public static final int SWITCH_PAGE_PREVIOUS = 0;
    private static final int GET_DATA_SUCCESS = 2;

    /**
     * 项目id
     */
    private int projectId;

    private Context mContext;
    private CarouselData.Data data;
    private List<CarouselData.Data.Materials> materialsList;
    private TimerTask mTask;
    private Timer mTimer;
    private ViewPagerAdapter mAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        projectId = ((MainActivity) getActivity()).getProjectId();
        mContext = ((MainActivity) getActivity()).getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.carousel_fragment_layout, null);
        viewPager = view.findViewById(R.id.vp);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public void initView() {
        mAdapter = new ViewPagerAdapter(materialsList, mContext);
        mAdapter.setPlayListener(this);
        viewPager.setAdapter(mAdapter);
        MyPagerChangeListener listener = new MyPagerChangeListener();
        viewPager.addOnPageChangeListener(listener);
        viewPager.setCurrentItem(mPosition);
//        viewPager.setOffscreenPageLimit(0);
        listener.setPageSelect(mPosition);
    }

    private void initData() {
        OkHttpUtils.getInstance().getDataAsyn(_URL.CAROUSEL_DATA_URL + projectId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonBody = response.body().string();
                Log.d(TAG, jsonBody);
                Log.d(TAG, "projectId: " + projectId);
                CarouselData carouselData = new Gson().fromJson(jsonBody, CarouselData.class);
                if (carouselData.getStatus() == 0) {
                    data = carouselData.getData();
                    materialsList = data.getMaterials();
                    List<String> urls = new ArrayList<>();
                    for (CarouselData.Data.Materials material : materialsList) {
                        urls.add(material.getSrc());
                    }
//                    download(materialsList);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initView();
                        }
                    });
//                    mHandler.sendEmptyMessage(GET_DATA_SUCCESS);
                }
            }
        });
    }

    /**
     * 视频播放完成的回调
     *
     * @param position
     */
    @Override
    public void playComplete(int position) {
        Log.d(TAG, "playComplete: 播放完成");
        switchNextPage();
    }


    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //如果滑动就重新计时
            removeTimer();
            mPosition = position;
            if (materialsList.get(position).getType() == 0 || materialsList.get(position).getType() == 1) {
                if (materialsList.get(position).getTime() != 0) {
                    startCountdown(materialsList.get(position).getTime());
                }
            }
        }

        @Override
        public void onPageSelected(int position) {
            mPosition = position;
            Log.d("MyPagerChangeListener", "onPageSelected: " + position);
//            如果播放图片开启计时器
            if (materialsList.get(position).getType() == 0 || materialsList.get(position).getType() == 1) {
                startCountdown(materialsList.get(position).getTime());
            } else if (materialsList.get(position).getType() == 2) {
                mAdapter.startPlay();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        public void setPageSelect(int position) {
            this.onPageSelected(position);
        }

    }

    /**
     * 开始计时
     *
     * @param duration
     */
    private void startCountdown(long duration) {
//        mRunnable = new Runnable() {
//            @Override
//            public void run() {
//                mHandler.sendEmptyMessage(SWITCH_PAGE_NEXT);
//            }
//        };
//        mHandler.postDelayed(mRunnable, duration);
        Log.d(TAG, "startCountdown: " + duration);
        removeTimer();
        mTask = new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switchNextPage();
                    }
                });
            }
        };
        mTimer = new Timer();
        mTimer.schedule(mTask, duration * 1000);
    }

    /**
     * 取消计时
     */
    private void removeTimer() {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    /**
     * 切换下一个页面
     */
    private void switchNextPage() {
        if (mPosition < materialsList.size() - 1) {
            mPosition++;
        } else {
            mPosition = 0;
        }
        Log.d(TAG, "switchNextPage: " + mPosition);
        viewPager.setCurrentItem(mPosition);
    }

    /**
     * 切换上一个页面
     */
    private void switchPreviousPage() {
        if (mPosition > 0) {
            mPosition--;
        }
        Log.d(TAG, "switchPreviousPage: " + mPosition);
        viewPager.setCurrentItem(mPosition);
    }

    @Override
    public void onPause() {
        super.onPause();
        removeTimer();
    }
}

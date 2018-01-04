package com.esioner.votecenter.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esioner.votecenter.Bean;
import com.esioner.votecenter.R;
import com.esioner.votecenter.adapter.ViewPagerAdapter;
import com.esioner.votecenter.entity.CarouselData;
import com.esioner.votecenter.utils.OkHttpUtils;
import com.esioner.votecenter.utils._URL;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author Esioner
 * @date 2018/1/2
 */

public class CarouselFragment extends Fragment implements ViewPagerAdapter.PlayListener {

    private ViewPager viewPager;
    private int mPosition = 0;
    /**
     * 资源实体类
     */
    List<Bean> beans = new ArrayList<>();
    private File[] files;
    private long duration = 2000;
    public static final int SWITCH_PAGE_NEXT = 1;
    public static final int SWITCH_PAGE_PREVIOUS = 0;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SWITCH_PAGE_NEXT:
                    if (mPosition < beans.size() - 1) {
                        mPosition++;
                    } else {
                        mPosition = 0;
                    }
                    break;
                case SWITCH_PAGE_PREVIOUS:
                    if (mPosition > 0) {
                        mPosition--;
                    }
                    break;
                default:
                    break;
            }
            Log.d("handleMessage", "handleMessage: " + mPosition);
            viewPager.setCurrentItem(mPosition);
        }

    };
    private Runnable mRunnable;

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
//        ViewPagerAdapter adapter = new ViewPagerAdapter(beans, getActivity().getApplicationContext());
//        adapter.setPlayListener(this);
//        viewPager.setAdapter(adapter);
//        MyPagerChangeListener listener = new MyPagerChangeListener();
//        viewPager.addOnPageChangeListener(listener);
//        viewPager.setCurrentItem(mPosition);
//        listener.setPageSelect(mPosition);

//        startCarousel();
    }

    private void initData() {
        OkHttpUtils.getInstance().getData(_URL.CAROUSEL_DATA_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonBody = response.body().string();
                Log.d("onResponse", "onResponse: " + jsonBody);
                CarouselData carouselData = new Gson().fromJson(jsonBody, CarouselData.class);
                Log.d("carouselData", carouselData.toString());

            }
        });
//        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "shopmanager" + File.separator + "advs");
//        Log.d("File", "file.path: " + file.getAbsolutePath());
//        if (file.isDirectory()) {
//            Log.d("File", "是文件夹 ");
//            files = file.listFiles();
//            Bean bean;
//            for (File file1 : files) {
//                bean = new Bean();
//                if (file1.getName().endsWith(".png") || file1.getName().endsWith(".jpg")) {
//                    bean.setPath(file1.getAbsolutePath());
//                    bean.setType(0);
//                } else if (file1.getName().endsWith(".mp4")) {
//                    bean.setType(1);
//                    bean.setPath(file1.getAbsolutePath());
//                }
//
//                if (!bean.isEmpty()) {
//                    beans.add(bean);
//                }
//
//            }
//
//        } else {
//            Log.d("File", "不是文件夹 ");
//
//        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (getUserVisibleHint()) {
            //当用户可见时，开始轮播
//            startCarousel();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void playComplete(int position) {
        mHandler.sendEmptyMessage(SWITCH_PAGE_NEXT);
    }


    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //如果滑动就重新计时
            mHandler.removeCallbacks(mRunnable);
            mPosition = position;
            if (beans.get(position).getType() == 0) {
                startCountdown();
            }
        }

        @Override
        public void onPageSelected(int position) {
            mPosition = position;
            Log.d("MyPagerChangeListener", "onPageSelected: " + position);
//            如果播放图片开启计时器
            if (beans.get(position).getType() == 0) {
                startCountdown();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        public void setPageSelect(int position) {
            this.onPageSelected(position);
        }

    }

    private void startCountdown() {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(SWITCH_PAGE_NEXT);
            }
        };
        mHandler.postDelayed(mRunnable, duration);
    }
}

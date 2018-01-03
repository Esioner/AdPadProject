package com.esioner.votecenter.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.esioner.votecenter.R;
import com.esioner.votecenter.adapter.ViewPagerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Esioner
 * @date 2018/1/2
 */

public class CarouselFragment extends Fragment {

    private ViewPager viewPager;
    private int position = 0;
    private List<View> viewList = new ArrayList<>();
    private File[] files;
    private long period = 5000;
    private Timer mTimer;
    private TimerTask mTask;
    private long duration = 5000;

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
        viewPager.setAdapter(new ViewPagerAdapter(viewList));
//        viewPager.setCurrentItem(0);
        startCarousel();
    }

    private void initData() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "shopmanager" + File.separator + "advs");
        Log.d("File", "file.path: " + file.getAbsolutePath());
        if (file.isDirectory()) {
            Log.d("File", "是文件夹 ");
            files = file.listFiles();
        } else {
            Log.d("File", "不是文件夹 ");
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout linearLayout;
        ImageView imageView;
        VideoView videoView;
        for (File mediaFile : files) {
            linearLayout = new LinearLayout(getActivity().getApplicationContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setLayoutParams(params);
            linearLayout.setBackgroundColor(Color.WHITE);
            Log.d("File", "initData: " + mediaFile.getName());
            if (mediaFile.isFile()) {
                if (mediaFile.getName().endsWith(".jpg") || mediaFile.getName().endsWith(".png") || mediaFile.getName().endsWith(".gif")) {
                    imageView = new ImageView(getActivity().getApplicationContext());
                    Glide.with(getActivity().getApplicationContext()).load(mediaFile).into(imageView);
                    linearLayout.addView(imageView);
                    viewList.add(linearLayout);
                } else if (mediaFile.getName().endsWith(".mp4")) {
                    videoView = new VideoView(getActivity().getApplicationContext());
                    videoView.setVideoPath(mediaFile.getAbsolutePath());
                    videoView.start();
                    linearLayout.addView(videoView);
                    viewList.add(linearLayout);
                }
            }
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (getUserVisibleHint()) {
            //当用户可见时，开始轮播
//            startCarousel();
        }
    }

    /**
     * 轮播
     */
    private void startCarousel() {
        Log.d("FILE", "startCarousel: 开始轮播");
        mTask = new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(position);
                        if (position < viewList.size() - 1) {
                            position = position + 1;
                        } else {
                            position = 0;
                        }
                    }
                });
            }
        };
        mTimer = new Timer();
        mTimer.schedule(mTask, duration);
//        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
//        executorService.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, 0, 5, TimeUnit.MICROSECONDS);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelTimer();
    }

    private void cancelTimer() {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}

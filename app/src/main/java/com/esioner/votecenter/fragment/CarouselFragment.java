package com.esioner.votecenter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
import com.esioner.votecenter.utils.Constant;
import com.esioner.votecenter.utils.OkHttpUtils;
import com.esioner.votecenter.utils._URL;
import com.google.gson.Gson;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;

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
    private static final String TAG = "CarouselFragment";

    private ViewPager viewPager;
    private int mPosition = 0;

    private long duration = 2000;

    public static final int SWITCH_PAGE_NEXT = 1;
    public static final int SWITCH_PAGE_PREVIOUS = 0;
    private static final int GET_DATA_SUCCESS = 2;

//    private final Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
////                case SWITCH_PAGE_NEXT:
////                    if (mPosition < materialsList.size() - 1) {
////                        mPosition++;
////                    } else {
////                        mPosition = 0;
////                    }
////                    break;
////                case SWITCH_PAGE_PREVIOUS:
////                    if (mPosition > 0) {
////                        mPosition--;
////
////                    }
////                    break;
////                case GET_DATA_SUCCESS:
////                    initView();
////                    break;
//                default:
//                    break;
//            }
//            Log.d("handleMessage", "handleMessage: " + mPosition);
//            viewPager.setCurrentItem(mPosition);
//        }

    //    };
    private Runnable mRunnable;
    private int projectId;
    private CarouselData.Data data;
    private List<CarouselData.Data.Materials> materialsList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        projectId = ((MainActivity) getActivity()).getProjectId();
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
        ViewPagerAdapter adapter = new ViewPagerAdapter(materialsList, getActivity().getApplicationContext());
        adapter.setPlayListener(this);
        viewPager.setAdapter(adapter);
        MyPagerChangeListener listener = new MyPagerChangeListener();
        viewPager.addOnPageChangeListener(listener);
        viewPager.setCurrentItem(mPosition);
        listener.setPageSelect(mPosition);
    }

    private void initData() {
        OkHttpUtils.getInstance().getData(_URL.CAROUSEL_DATA_URL + projectId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonBody = response.body().string();
                Log.d(TAG, jsonBody);
                CarouselData carouselData = new Gson().fromJson(jsonBody, CarouselData.class);
                if (carouselData.getStatus() == 0) {
                    data = carouselData.getData();
                    materialsList = data.getMaterials();
                    List<String> urls = new ArrayList<>();
                    for (CarouselData.Data.Materials material : materialsList) {
                        urls.add(material.getSrc());
                    }
                    download(materialsList);
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


    @Override
    public void playComplete(int position) {
//        mHandler.sendEmptyMessage(SWITCH_PAGE_NEXT);
    }


    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //如果滑动就重新计时
//            mHandler.removeCallbacks(mRunnable);
            mPosition = position;
            if (materialsList.get(position).getTime() != 0) {
                startCountdown(materialsList.get(position).getTime());
            }
        }

        @Override
        public void onPageSelected(int position) {
            mPosition = position;
            Log.d("MyPagerChangeListener", "onPageSelected: " + position);
//            如果播放图片开启计时器
            if (materialsList.get(position).getType() == 0) {
                startCountdown(materialsList.get(position).getTime());
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        public void setPageSelect(int position) {
            this.onPageSelected(position);
        }

    }

    private void startCountdown(long duration) {
        mRunnable = new Runnable() {
            @Override
            public void run() {
//                mHandler.sendEmptyMessage(SWITCH_PAGE_NEXT);
            }
        };
//        mHandler.postDelayed(mRunnable, duration);
    }

    /**
     * 下载
     *
     * @param materials
     */
    public void download(List<CarouselData.Data.Materials> materials) {
        final FileDownloadQueueSet queueSet = new FileDownloadQueueSet(new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                Log.d(TAG, "progress: " + soFarBytes);
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                Log.d(TAG, "completed: ");
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {

            }

            @Override
            protected void warn(BaseDownloadTask task) {

            }
        });

        final List<BaseDownloadTask> tasks = new ArrayList<>();
        BaseDownloadTask task;
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "vote" + File.separator + "advs";
        for (int i = 0; i < materials.size(); i++) {
            CarouselData.Data.Materials material = materials.get(i);
            task = FileDownloader.getImpl().create(material.getSrc()).setTag(i + 1);
            if (material.getType() == 2) {
                task.setPath(dirPath + File.separator + material.getName() + ".ogg");
            } else if (material.getType() == 0) {
                task.setPath(dirPath + File.separator + material.getName() + ".png");
            }
            tasks.add(task);
        }
        queueSet.disableCallbackProgressTimes();
        queueSet.setAutoRetryTimes(1);
        // 并行执行该任务队列
        queueSet.downloadTogether(tasks);
        queueSet.start();
    }
}

package com.esioner.votecenter.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.esioner.votecenter.entity.CarouselData;
import com.esioner.votecenter.view.EmptyVidePlayer;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import java.io.File;
import java.util.List;

/**
 * @author Esioner
 * @date 2018/1/2
 */

public class ViewPagerAdapter extends PagerAdapter {
    private static final String TAG = "ViewPagerAdapter";
    private List<CarouselData.Data.Materials> materials;
    private Context mContext;
    private PlayListener playListener;
    private GSYVideoPlayer player;

    public ViewPagerAdapter(List<CarouselData.Data.Materials> materials, Context context) {
        this.materials = materials;
        this.mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = getView(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = getView(position);
        container.removeView(view);
    }

    @Override
    public int getCount() {
        return materials.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void finishUpdate(@NonNull ViewGroup container) {
        super.finishUpdate(container);
    }

    /**
     * 通过传入实体类来判断类型，并将该类型的 view 传出
     *
     * @param position
     * @return
     */
    public View getView(final int position) {
        CarouselData.Data.Materials material = materials.get(position);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout linearLayout;
        linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setLayoutParams(params);
        linearLayout.setBackgroundColor(Color.WHITE);
        if (material.getType() == 0) {
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(params);
            Glide.with(mContext).load(material.getSrc()).into(imageView);
            linearLayout.addView(imageView);
        } else if (material.getType() == 2) {
            String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "vote" + File.separator + "advs";
            File file = new File(dirPath);
            player = new EmptyVidePlayer(mContext);
            player.initUIState();
            player.setUp(material.getSrc(), true, file, "");
            player.setGSYVideoProgressListener(new GSYVideoProgressListener() {
                @Override
                public void onProgress(int progress, int secProgress, int currentPosition, int duration) {
                    if (currentPosition / 1000 == duration / 1000) {
                        playListener.playComplete(position);
//                        player.release();
                    }
                }
            });
            linearLayout.addView(player);
        }
        return linearLayout;
    }

    public void startPlay() {
        player.startPlayLogic();
    }

    public void setPlayListener(PlayListener playListener) {
        this.playListener = playListener;
    }

    public interface PlayListener {
        /**
         * 播放完成调用接口
         */
        void playComplete(int position);

    }
}

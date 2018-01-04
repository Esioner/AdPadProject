package com.esioner.votecenter.adapter;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.esioner.votecenter.Bean;

import java.io.IOException;
import java.util.List;

import cn.jzvd.JZVideoPlayerStandard;

/**
 * @author Esioner
 * @date 2018/1/2
 */

public class ViewPagerAdapter extends PagerAdapter {
    private List<Bean> beans;
    private Context mContext;
    private PlayListener playListener;

    public ViewPagerAdapter(List<Bean> beans, Context context) {
        this.beans = beans;
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
        return beans.size();
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
        Bean bean = beans.get(position);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout linearLayout;
        linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setLayoutParams(params);
        linearLayout.setBackgroundColor(Color.WHITE);
        if (bean.getType() == 0) {
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(params);
            Glide.with(mContext).load(bean.getPath()).into(imageView);
            linearLayout.addView(imageView);
        } else if (bean.getType() == 1) {
            JZVideoPlayerStandard playerStandard = new JZVideoPlayerStandard(mContext);
            playerStandard.setUp(bean.getPath(), JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL);
//            playerStandard
            linearLayout.addView(playerStandard);
        }
        return linearLayout;
    }

    public void setPlayListener(PlayListener playListener) {
        this.playListener = playListener;
    }

    public interface PlayListener {
        /**
         * 播放完成调用接口
         *
         * @param position
         */
        void playComplete(int position);
    }
}

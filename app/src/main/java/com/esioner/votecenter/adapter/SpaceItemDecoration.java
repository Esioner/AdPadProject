package com.esioner.votecenter.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author Esioner
 * @date 2018/1/10
 * 设置RecyclerView 子控件间距
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    int mSpace;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = outRect.top = mSpace;
    }

    public SpaceItemDecoration(int space) {
        this.mSpace = space;
    }
}
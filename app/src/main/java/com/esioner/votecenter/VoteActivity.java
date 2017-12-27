package com.esioner.votecenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Esioner
 * @date 2017/12/27
 * 宽度: 1024
 * 高度: 600
 * dp = 实际像素数*160/像素密度
 * 像素密度PPI = √（长度像素数² + 宽度像素数²） / 屏幕对角线英寸数 = 1025.75/屏幕对角线英寸数
 */

public class VoteActivity extends Activity {

    private RecyclerView rvPersonInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote_activity_layout);
        initView();
    }

    private void initView() {
        List list = new ArrayList();
        for (int i = 0; i < 6; i++) {
            list.add(i);
        }
        rvPersonInfo = findViewById(R.id.rv_user_info);
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(list);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        rvPersonInfo.setLayoutManager(manager);
        rvPersonInfo.setAdapter(adapter);
    }

    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
        List list = new ArrayList();

        MyRecyclerViewAdapter(List list) {
            this.list = list;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View itemView) {
                super(itemView);

            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vote_user_info_layout, null);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}

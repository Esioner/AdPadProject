package com.esioner.votecenter.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esioner.votecenter.R;
import com.esioner.votecenter.entity.VoteData;
import com.esioner.votecenter.utils.OkHttpUtils;
import com.esioner.votecenter.utils._URL;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author Esioner
 * @date 2017/12/29
 */

/**
 * @author Esioner
 * @date 2017/12/27
 * 宽度: 1024
 * 高度: 600
 * dp = 实际像素数*160/像素密度
 * 像素密度PPI = √（长度像素数² + 宽度像素数²） / 屏幕对角线英寸数 = 1025.75/屏幕对角线英寸数
 */


public class VoteFragment extends Fragment {
    private RecyclerView rvPersonInfo;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.vote_fragment_layout, null, false);
        initView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initView() {
        List list = new ArrayList();
        for (int i = 0; i < 6; i++) {
            list.add(i);
        }
        rvPersonInfo = view.findViewById(R.id.rv_user_info);
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(list);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        rvPersonInfo.addItemDecoration(new SpaceItemDecoration(15));
        rvPersonInfo.setLayoutManager(manager);
        rvPersonInfo.setAdapter(adapter);
    }

    public void initData() {
        /**
         * 获取投票列表
         */
        OkHttpUtils.getInstance().getData(_URL.VOTE_DATA_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonBody = response.body().string();
                Log.d("vote_jsonBody", "onResponse: " + jsonBody);
                VoteData voteData = new Gson().fromJson(jsonBody, VoteData.class);
                Log.d("voteData", voteData.toString());
            }
        });
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

    class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        int mSpace;

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = mSpace;
            outRect.right = mSpace;
            outRect.bottom = mSpace;
            outRect.top = mSpace;
        }

        public SpaceItemDecoration(int space) {
            this.mSpace = space;
        }
    }

}

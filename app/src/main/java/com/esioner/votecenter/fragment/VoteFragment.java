package com.esioner.votecenter.fragment;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.esioner.votecenter.MainActivity;
import com.esioner.votecenter.MyApplication;
import com.esioner.votecenter.R;
import com.esioner.votecenter.adapter.MyRecyclerViewAdapter;
import com.esioner.votecenter.entity.VoteData;
import com.esioner.votecenter.entity.VoteDetailData;
import com.esioner.votecenter.service.DownloadService;
import com.esioner.votecenter.utils.OkHttpUtils;
import com.esioner.votecenter.utils._URL;
import com.google.gson.Gson;

import org.w3c.dom.Text;

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
    private static final String TAG = "VoteFragment";

    private RecyclerView rvPersonInfo;
    private View view;

    private int projectId;
    private List<VoteDetailData.Data.VoteItems> voteItems;
    private TextView tvAllVoteNum;
    private TextView tvEachVoteNum;
    private TextView tvVote;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        projectId = ((MainActivity) getActivity()).getProjectId();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.vote_fragment_layout, null, false);
        rvPersonInfo = view.findViewById(R.id.rv_user_info);
        tvEachVoteNum = view.findViewById(R.id.tv_each_vote_num);
        tvAllVoteNum = view.findViewById(R.id.tv_total_vote_num);
        tvVote = view.findViewById(R.id.btn_vote);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initView(VoteDetailData detailData) {
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(voteItems);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        rvPersonInfo.addItemDecoration(new SpaceItemDecoration(15));
        rvPersonInfo.setLayoutManager(manager);
        rvPersonInfo.setAdapter(adapter);
        tvAllVoteNum.setText(detailData.getData().getAllCanVoteNumber() + "");
        tvEachVoteNum.setText(detailData.getData().getEachItemCanVoteNumber() + "");
        tvVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    public void initData() {
        /**
         * 获取投票列表
         */
        OkHttpUtils.getInstance().getData(_URL.VOTE_DETAIL_DATA_URL + projectId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonBody = response.body().string();
                Log.d(TAG, jsonBody);

                final VoteDetailData detailData = new Gson().fromJson(jsonBody, VoteDetailData.class);
                if (detailData.getData() != null) {
                    voteItems = detailData.getData().getVoteItemsList();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initView(detailData);
                        }
                    });
                }
            }
        });
    }

    /**
     * 设置RecyclerView 子控件间距
     */
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

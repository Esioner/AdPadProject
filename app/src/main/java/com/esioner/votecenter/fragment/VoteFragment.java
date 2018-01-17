package com.esioner.votecenter.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.esioner.votecenter.MainActivity;
import com.esioner.votecenter.R;
import com.esioner.votecenter.adapter.VoteRecyclerViewAdapter;
import com.esioner.votecenter.entity.VoteDetailData;
import com.esioner.votecenter.utils.OkHttpUtils;
import com.esioner.votecenter.utils.Utility;
import com.esioner.votecenter.utils._URL;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    private Context mContext;
    private int voteId;
    private Dialog dialog;
    private MainActivity mActivity;
    private MaterialDialog mConfigDialog;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) getActivity();
        projectId = mActivity.getProjectId();
        mContext = mActivity.getContext();
        Log.d(TAG, "onAttach: " + projectId);
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
        int eachCanVoteNum = detailData.getData().getEachItemCanVoteNumber();
        final int allCanVoteNum = detailData.getData().getAllCanVoteNumber();
        final VoteRecyclerViewAdapter adapter = new VoteRecyclerViewAdapter(voteItems, mContext, eachCanVoteNum, allCanVoteNum);
        GridLayoutManager manager = new GridLayoutManager(mContext, 3);
        rvPersonInfo.addItemDecoration(new SpaceItemDecoration());
        rvPersonInfo.setLayoutManager(manager);
        rvPersonInfo.setAdapter(adapter);
        tvAllVoteNum.setText(detailData.getData().getAllCanVoteNumber() + "");
        tvEachVoteNum.setText(eachCanVoteNum + "");
        //投票按钮的点击事件
        tvVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + voteId);
                final int currentVoteNum = adapter.getCurrentVoteNum();
                MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext);
                builder.title("确认提示")
                        .content("您是否确定投票，您当前已投" + currentVoteNum + "票，是否投票")
                        .negativeText("确定")
                        .positiveText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                mConfigDialog.dismiss();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                if (currentVoteNum == 0) {
                                    //投票成功
                                    showDialog("投票成功");
                                } else if (currentVoteNum > allCanVoteNum) {
                                    showToast("你当前投票数为" + currentVoteNum + "票,您当前可投票总数为" + allCanVoteNum + "票\n超过" + (currentVoteNum - allCanVoteNum) + "票");
                                } else {
                                    final String jsonData = adapter.getVoteData();
                                    if (jsonData != null) {
                                        Log.d(TAG, "onClick: " + jsonData);
                                        OkHttpUtils.getInstance().postVoteData("http://116.62.228.3:8089/adv/api/vote/" + voteId + "/voteItem/vote?mac=" + Utility.getMacAdress(), jsonData, new Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                                Log.e(TAG, "onFailure: " + e.toString());
                                            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                String result = response.body().string();
                                                Log.d(TAG, "onResponse: " + result);
                                                try {
                                                    JSONObject jsonObject = new JSONObject(result);
                                                    int status = jsonObject.getInt("status");
                                                    if (status == 0) {
                                                        //投票成功
                                                        showDialog("投票成功");
                                                    } else if (status == 1) {
//                                    BaseData data = new Gson().fromJson(result, BaseData.class);
//                                    //投票失败
//                                    showDialog(data.getData());
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        adapter.clearResult();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }
                            }
                        });
                mConfigDialog = builder.build();
                mConfigDialog.getTitleView().setTextSize(TypedValue.COMPLEX_UNIT_PX, 36);
                mConfigDialog.getContentView().setTextSize(TypedValue.COMPLEX_UNIT_PX, 24);
                mConfigDialog.setCancelable(false);
                mConfigDialog.show();
            }
        });
    }

    private void showToast(final String text) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void showDialog(final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                View view = LayoutInflater.from(mContext).inflate(R.layout.vote_success_dialog_layout, null);
                TextView tvResult = view.findViewById(R.id.tv_result);
                tvResult.setText(text);
                builder.setView(view);
                dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (mConfigDialog != null && mConfigDialog.isShowing()) {
            mConfigDialog.dismiss();
            mConfigDialog = null;
        }
    }

    public void initData() {
        /**
         * 获取投票列表
         */
        OkHttpUtils.getInstance().getDataAsyn(_URL.VOTE_DETAIL_DATA_URL + projectId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonBody = response.body().string();
                Log.d(TAG, jsonBody);

                final VoteDetailData detailData = new Gson().fromJson(jsonBody, VoteDetailData.class);
                if (detailData.getData() != null) {
                    voteId = detailData.getData().getId();
                    Log.d(TAG, "voteId: " + voteId);
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
        private int mSpace = 10;

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int position = parent.getChildLayoutPosition(view);
            if (position != 0 && position != 3) {
                outRect.left = mSpace;
            } else {

            }
            if (position != 2 && position != 5) {
                outRect.right = mSpace;
            }
            outRect.bottom = outRect.top = 8;
        }
    }

}

package com.esioner.votecenter.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.esioner.votecenter.MyApplication;
import com.esioner.votecenter.R;
import com.esioner.votecenter.entity.VoteDetailData;
import com.esioner.votecenter.entity.VoteItem;
import com.esioner.votecenter.utils.DensityUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Esioner
 * @date 2018/1/8
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {
    private final static String TAG = "MyRecyclerViewAdapter";
    private List<VoteDetailData.Data.VoteItems> voteItems;
    private int mAmount;
    private Context mContext;
    private int voteId;

    public MyRecyclerViewAdapter(List<VoteDetailData.Data.VoteItems> voteItems, Context context) {
        this.voteItems = voteItems;
        mContext = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvVoteItemId;
        private final ImageView ivVoteHeaderImage;
        private final TextView tvVoteItemName;
        private final View rootView;
        private final TextView tvAmount;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.ll_root_view);
            tvVoteItemId = itemView.findViewById(R.id.tv_vote_item_id);
            ivVoteHeaderImage = itemView.findViewById(R.id.iv_vote_item_header_image);
            tvVoteItemName = itemView.findViewById(R.id.tv_vote_item_name);
            tvAmount = itemView.findViewById(R.id.tv_amount);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vote_user_info_layout, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final VoteDetailData.Data.VoteItems item = voteItems.get(position);
        Glide.with(MyApplication.getContext()).load(item.getImgSrc()).into(holder.ivVoteHeaderImage);
        holder.tvVoteItemId.setText(item.getId() + "");
        holder.tvVoteItemName.setText(item.getName());
        mAmount = 0;
        holder.tvAmount.setText(mAmount + "");
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(item, holder.tvAmount);
            }
        });
    }

    @Override
    public int getItemCount() {
        return voteItems.size();
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    private void showDialog(final VoteDetailData.Data.VoteItems item, final TextView tv) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.vote_detail_dialog_layout, null);
        TextView tv1 = view.findViewById(R.id.tv_1);
        TextView tv2 = view.findViewById(R.id.tv_2);
        TextView tv3 = view.findViewById(R.id.tv_3);
        TextView tv4 = view.findViewById(R.id.tv_4);
        TextView tv5 = view.findViewById(R.id.tv_5);
        TextView tv6 = view.findViewById(R.id.tv_6);
        TextView tv7 = view.findViewById(R.id.tv_7);
        TextView tv8 = view.findViewById(R.id.tv_8);
        TextView tv9 = view.findViewById(R.id.tv_9);
        TextView tv10 = view.findViewById(R.id.tv_10);
        builder.setView(view);
        final Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        //宽高可设置具体大小
        lp.width = 418;
        lp.height = 319;
        dialog.getWindow().setAttributes(lp);

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setResult(1);
                tv.setText("1");
                dialog.dismiss();

            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setResult(2);
                tv.setText("2");
                dialog.dismiss();
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setResult(3);
                tv.setText("3");
                dialog.dismiss();
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setResult(4);
                tv.setText("4");
                dialog.dismiss();
            }
        });
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setResult(5);
                tv.setText("5");
                dialog.dismiss();
            }
        });
        tv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setResult(6);
                tv.setText("6");
                dialog.dismiss();
            }
        });
        tv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setResult(7);
                tv.setText("7");
                dialog.dismiss();
            }
        });
        tv8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setResult(8);
                tv.setText("8");
                dialog.dismiss();
            }
        });
        tv9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setResult(9);
                tv.setText("9");
                dialog.dismiss();
            }
        });
        tv10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setResult(10);
                tv.setText("10");
                dialog.dismiss();
            }
        });
    }

    public String getVoteData() {
        List<VoteItem> items = new ArrayList<>();
        VoteItem item;
        for (VoteDetailData.Data.VoteItems voteItem : voteItems) {
            item = new VoteItem();
            item.setVoteItemId(voteItem.getId());
            item.setVoteNumber(voteItem.getResult());
            items.add(item);
        }
        String jsonData = new Gson().toJson(items);
        Log.d(TAG, jsonData);
        return jsonData;
    }
}

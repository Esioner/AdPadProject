package com.esioner.votecenter.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.esioner.votecenter.MyApplication;
import com.esioner.votecenter.R;
import com.esioner.votecenter.entity.VoteDetailData;

import java.util.List;

/**
 * @author Esioner
 * @date 2018/1/8
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private List<VoteDetailData.Data.VoteItems> voteItems;

    public MyRecyclerViewAdapter(List<VoteDetailData.Data.VoteItems> voteItems) {
        this.voteItems = voteItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvVoteItemId;
        private final ImageView ivVoteHeaderImage;
        private final TextView tvVoteItemName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvVoteItemId = itemView.findViewById(R.id.tv_vote_item_id);
            ivVoteHeaderImage = itemView.findViewById(R.id.iv_vote_item_header_image);
            tvVoteItemName = itemView.findViewById(R.id.tv_vote_item_name);
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
        VoteDetailData.Data.VoteItems item = voteItems.get(position);
        Glide.with(MyApplication.getContext()).load(item.getImgSrc()).into(holder.ivVoteHeaderImage);
        holder.tvVoteItemId.setText(item.getId()+"");
        holder.tvVoteItemName.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return voteItems.size();
    }
}

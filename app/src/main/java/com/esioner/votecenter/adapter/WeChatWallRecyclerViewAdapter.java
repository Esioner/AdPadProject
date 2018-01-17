package com.esioner.votecenter.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esioner.votecenter.R;
import com.esioner.votecenter.entity.WeChatDetailData;

import java.util.List;

/**
 * @author Esioner
 * @date 2018/1/10
 */

public class WeChatWallRecyclerViewAdapter extends RecyclerView.Adapter<WeChatWallRecyclerViewAdapter.ViewHolder> {
    private List<WeChatDetailData> weChatResultDataList;

    public WeChatWallRecyclerViewAdapter(List<WeChatDetailData> list) {
        this.weChatResultDataList = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvName;
        private final TextView tvContent;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_we_chat_item_name);
            tvContent = itemView.findViewById(R.id.tv_we_chat_item_content);
        }
    }

    @Override
    public WeChatWallRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wechat_wall_recyclerview_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(WeChatWallRecyclerViewAdapter.ViewHolder holder, int position) {
        WeChatDetailData weChatDetailData = weChatResultDataList.get(position);
        holder.tvContent.setText(weChatDetailData.getContent());
        holder.tvName.setText(weChatDetailData.getTerminalName());
    }

    @Override
    public int getItemCount() {
        return weChatResultDataList.size();
    }
}
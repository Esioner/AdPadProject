package com.esioner.votecenter.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esioner.votecenter.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Esioner
 * @date 2017/12/29
 */

public class WeChatWallFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wechat_wall_fragment_layout, null);
        RecyclerView rvWeChatWall = view.findViewById(R.id.rv_wechat_wall);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("");
        }
        rvWeChatWall.addItemDecoration(new SpaceItemDecoration(10));
        WeChatWallRecyclerViewAdapter adapter = new WeChatWallRecyclerViewAdapter(list);
        rvWeChatWall.setLayoutManager(manager);
        rvWeChatWall.setAdapter(adapter);
        return view;
    }


    public class WeChatWallRecyclerViewAdapter extends RecyclerView.Adapter<WeChatWallRecyclerViewAdapter.ViewHolder> {
        private List<String> list;

        public WeChatWallRecyclerViewAdapter(List<String> list) {
            this.list = list;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View itemView) {
                super(itemView);
            }
        }

        @Override
        public WeChatWallRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wechat_wall_recyclerview_item_layout, parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(WeChatWallRecyclerViewAdapter.ViewHolder holder, int position) {

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
            outRect.bottom = mSpace;
            outRect.top = mSpace;
        }

        public SpaceItemDecoration(int space) {
            this.mSpace = space;
        }
    }

}

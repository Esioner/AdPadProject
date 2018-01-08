package com.esioner.votecenter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.esioner.votecenter.MainActivity;
import com.esioner.votecenter.R;

/**
 * @author Esioner
 * @date 2017/12/29
 * 抢答页面
 */

public class ResponderFragment extends Fragment {
    private Button btnResponder;
    private int projectId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        projectId = ((MainActivity) getActivity()).getProjectId();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.responsder_fragment_layout, null);
        btnResponder = view.findViewById(R.id.btn_responder);
        setUnClick();
        return view;
    }

    /**
     * 设置按钮不可被点击
     */
    public void setUnClick() {
        btnResponder.setEnabled(false);
    }

    /**
     * 设置按钮可以被点击
     */
    public void setCanClick() {
        btnResponder.setEnabled(true);
    }
}

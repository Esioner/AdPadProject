package com.esioner.votecenter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esioner.votecenter.R;

/**
 * @author Esioner
 * @date 2017/12/29
 * 抢答页面
 */

public class ResponderResultFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.responsder_result_fragment_layout, null);
        TextView tvResult = view.findViewById(R.id.tv_result);
        Bundle bundle = getArguments();
        if (bundle != null) {
            int result = bundle.getInt("result", 0);
            tvResult.setText(result + "");
        }
        return view;
    }
}

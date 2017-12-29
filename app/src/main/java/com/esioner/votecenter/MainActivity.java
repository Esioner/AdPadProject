package com.esioner.votecenter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.esioner.votecenter.frame.ResponderFragment;
import com.esioner.votecenter.frame.ResponderResultFragment;
import com.esioner.votecenter.frame.VoteFragment;
import com.esioner.votecenter.frame.WeChatWallFragment;

/**
 * @author Esioner
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RESPONDER_RESULT_FRAGMENT_ID = 0;
    private static final int RESPONDER_FRAGMENT_ID = 1;
    private static final int VOTE_FRAGMENT_ID = 2;
    private static final int WE_CHAT_WALL_FRAGMENT_ID = 3;


    private ResponderResultFragment resultFragment;
    private ResponderFragment responderFragment;
    private VoteFragment voteFragment;
    private WeChatWallFragment weChatWallFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        switchFragment(WE_CHAT_WALL_FRAGMENT_ID);
        findViewById(R.id.btn_0).setOnClickListener(this);
        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        findViewById(R.id.btn_3).setOnClickListener(this);
        findViewById(R.id.btn_4).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_0:
                switchFragment(0);
                break;
            case R.id.btn_1:
                switchFragment(1);
                break;
            case R.id.btn_2:
                switchFragment(2);
                break;
            case R.id.btn_3:
                switchFragment(3);
                break;
            case R.id.btn_4:
//                switchFragment(4);
                break;
            default:
        }
    }

    /**
     * 根据id切换fragment
     *
     * @param id
     */
    public void switchFragment(int id) {
        switch (id) {
            case RESPONDER_FRAGMENT_ID:
                if (responderFragment != null) {
                    responderFragment = null;
                }
                responderFragment = new ResponderFragment();
                transactionFragment(responderFragment);
                break;
            case RESPONDER_RESULT_FRAGMENT_ID:
                if (resultFragment != null) {
                    resultFragment = null;
                }
                resultFragment = new ResponderResultFragment();
                transactionFragment(resultFragment);
                break;
            case VOTE_FRAGMENT_ID:
                if (voteFragment != null) {
                    voteFragment = null;
                }
                voteFragment = new VoteFragment();
                transactionFragment(voteFragment);
                break;
            case WE_CHAT_WALL_FRAGMENT_ID:
                if (weChatWallFragment != null) {
                    weChatWallFragment = null;
                }
                weChatWallFragment = new WeChatWallFragment();
                transactionFragment(weChatWallFragment);
                break;
            default:
        }
    }

    /**
     * 切换 fragment 的工具类
     *
     * @param fragment
     */
    private void transactionFragment(Fragment fragment) {
            //fragment 管理器
            FragmentManager manager = getSupportFragmentManager();
            //fragment 事务
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.frame_layout, fragment);
            transaction.commit();
    }

    /**
     * 开始抢答
     */
    public void startResponder() {
        if (responderFragment != null) {
            responderFragment.setCanClick();
        }
    }
}

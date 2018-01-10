package com.esioner.votecenter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.esioner.votecenter.entity.CurrentPageData;
import com.esioner.votecenter.entity.WeChatData;
import com.esioner.votecenter.entity.WeChatDetailData;
import com.esioner.votecenter.entity.WeChatResultData;
import com.esioner.votecenter.entity.WebSocketData;
import com.esioner.votecenter.fragment.CarouselFragment;
import com.esioner.votecenter.fragment.ResponderFragment;
import com.esioner.votecenter.fragment.ResponderResultFragment;
import com.esioner.votecenter.fragment.ShowPictureFragment;
import com.esioner.votecenter.fragment.VoteFragment;
import com.esioner.votecenter.fragment.WeChatWallFragment;
import com.esioner.votecenter.utils.Utility;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * @author Esioner
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private static final int RESPONDER_RESULT_FRAGMENT_ID = 0;
    private static final int RESPONDER_FRAGMENT_ID = 1;
    private static final int VOTE_FRAGMENT_ID = 2;
    private static final int WE_CHAT_WALL_FRAGMENT_ID = 3;
    private static final int CAROUSEL_FRAGMENT_ID = 4;
    private static final int SHOW_PICTURE_ID = 5;


    private int projectId;

    private ResponderResultFragment resultFragment;
    private ResponderFragment responderFragment;
    private VoteFragment voteFragment;
    private WeChatWallFragment weChatWallFragment;
    private CarouselFragment carouselFragment;
    private WebSocket webSocket;
    private Context mContext = this;
    private ShowPictureFragment showPictureFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化控件
        initView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        //初始化 websocket
        initWebSocket();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(LayoutInflater.from(mContext).inflate(R.layout.vote_detail_dialog_layout, null));
        Dialog dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        //宽高可设置具体大小
        lp.width = 418;
        lp.height = 319;
        dialog.getWindow().setAttributes(lp);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        findViewById(R.id.btn_0).setOnClickListener(this);
        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        findViewById(R.id.btn_3).setOnClickListener(this);
        findViewById(R.id.btn_4).setOnClickListener(this);
    }

    private void initWebSocket() {
        final String macAddress = Utility.getMacAdress();
        Log.d(TAG, "macAddress: " + macAddress);

        WebSocketListener listener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                try {
                    Log.d(TAG, "onOpen: " + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, final String text) {
                try {
                    Log.d(TAG, "onMessage: " + text);
                    //获取 code
                    JSONObject object = new JSONObject(text);
                    int code = object.getInt("code");
                    Log.d(TAG, "projectId = " + projectId);
                    WebSocketData data = null;
                    if (code != 9) {
                        data = new Gson().fromJson(text, WebSocketData.class);
                        if (data.getData() != null) {
                            if (data.getData().getPage() == 1 || data.getData().getPage() == 2) {
                                projectId = data.getData().getProjectId();
                                Log.d(TAG, "onMessage: " + projectId);
                            }
                        }
                    }
                    switch (code) {
                        //切换页面
                        case 2:
                            int pageCode = data.getData().getPage();
                            switch (pageCode) {
                                //未知页面
                                case 0:
                                    break;
                                //轮播页面
                                case 1:
                                    if (projectId != -1) {
                                        switchFragment(CAROUSEL_FRAGMENT_ID);
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(mContext, "ProjectId:" + projectId, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    break;
                                //投票页面
                                case 2:
                                    switchFragment(VOTE_FRAGMENT_ID);
                                    break;
                                //抢答页面
                                case 3:
                                    switchFragment(RESPONDER_FRAGMENT_ID);
                                    break;
                                //微信墙页面
                                case 4:
                                    switchFragment(WE_CHAT_WALL_FRAGMENT_ID);
                                    break;
                                case 5:
                                    switchFragment(SHOW_PICTURE_ID);
                                    break;
                                default:
                                    break;
                            }
                            //将当前页面信息封装成 json
                            CurrentPageData currentPageData = new CurrentPageData();
                            currentPageData.setCode(1);
                            currentPageData.setData(currentPageData.new Data(macAddress, pageCode));
                            String sendJson = new Gson().toJson(currentPageData);
                            //跳转之后需要向服务器发送当前页面数据
                            webSocket.send(sendJson);
                            break;
                        //开始抢答,使抢答按钮变得可点击
                        case 5:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startResponder();
                                }
                            });
                            break;
                        //停止抢答，使按钮变得不可点击
                        case 6:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    stopResponder();
                                }
                            });
                            break;
                        //抢答结果广播
                        case 8:
                            switchFragment(RESPONDER_RESULT_FRAGMENT_ID);
                            /**
                             * 设置抢答结果
                             */
                            resultFragment.setResult(data.getData().getName());
                            break;
                        //刷新弹幕消息
                        case 9:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    refreshWeChatData(text);
                                }
                            });
                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                webSocket.close(code, reason);
                Log.d(TAG, "onClosing: 已关闭");
                Log.d(TAG, "onClosing: " + reason + code);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                Log.d(TAG, "onClosed: " + reason);
                super.onClosed(webSocket, code, reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                Log.e(TAG, t.toString());
                t.printStackTrace();
                super.onFailure(webSocket, t, response);
            }
        };
        Request request = new Request.Builder()
                .url("ws://116.62.228.3:8089/adv/terminal?mac=" + macAddress)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .writeTimeout(10000, TimeUnit.MILLISECONDS)
                .build();
        webSocket = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
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
                switchFragment(5);
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
            case CAROUSEL_FRAGMENT_ID:
                if (carouselFragment != null) {
                    carouselFragment = null;
                }
                carouselFragment = new CarouselFragment();
                transactionFragment(carouselFragment);
                break;
            case SHOW_PICTURE_ID:
                if (showPictureFragment != null) {
                    showPictureFragment = null;
                }
                showPictureFragment = new ShowPictureFragment();
                transactionFragment(showPictureFragment);
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
//        transaction.addToBackStack(null);
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }

    /**
     * 刷新微信墙数据
     */
    public void refreshWeChatData(String json) {
        Gson gson = new Gson();
        WeChatData chatData = gson.fromJson(json, WeChatData.class);
        List<WeChatDetailData> weChatDataList = chatData.getDatas();
        if (weChatWallFragment != null) {
            weChatWallFragment.initData(weChatDataList);
        }
    }

    /**
     * 传递 projectId
     * 只有投票和轮播需要用到
     *
     * @return
     */
    public int getProjectId() {
        return projectId;
    }

    /**
     * 开始抢答
     */
    public void startResponder() {
        if (responderFragment != null) {
            responderFragment.setCanClick();
        }
    }

    /**
     * 停止抢答
     */
    public void stopResponder() {
        if (responderFragment != null) {
            responderFragment.setUnClick();
        }
    }

    /**
     * 给 Fragment 传递上下文环境
     *
     * @return
     */
    public Context getContext() {
        return this;
    }

    @Override
    protected void onPause() {
        webSocket.close(1000, "软甲退出");
        Log.d(TAG, "onStop: 已关闭");
        super.onPause();
    }
}

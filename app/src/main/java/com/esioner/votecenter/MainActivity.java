package com.esioner.votecenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.esioner.votecenter.entity.CurrentPageData;
import com.esioner.votecenter.entity.UpdateData;
import com.esioner.votecenter.entity.WeChatData;
import com.esioner.votecenter.entity.WeChatDetailData;
import com.esioner.votecenter.entity.WebSocketData;
import com.esioner.votecenter.fragment.CarouselFragment;
import com.esioner.votecenter.fragment.ResponderFragment;
import com.esioner.votecenter.fragment.ResponderResultFragment;
import com.esioner.votecenter.fragment.ShowPictureFragment;
import com.esioner.votecenter.fragment.VoteFragment;
import com.esioner.votecenter.fragment.WeChatWallFragment;
import com.esioner.votecenter.utils.OkHttpUtils;
import com.esioner.votecenter.utils.SPUtils;
import com.esioner.votecenter.utils.Utility;
import com.esioner.votecenter.utils._URL;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * @author Esioner
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int RESPONDER_RESULT_FRAGMENT_ID = 0;
    private static final int RESPONDER_FRAGMENT_ID = 1;
    private static final int VOTE_FRAGMENT_ID = 2;
    private static final int WE_CHAT_WALL_FRAGMENT_ID = 3;
    private static final int CAROUSEL_FRAGMENT_ID = 4;
    private static final int SHOW_PICTURE_ID = 5;
    private static final int STATUS_SUCCESS = 100;
    private static final int STATUS_ERROR = 101;

    /**
     * 重试次数
     */
    private int retryAllTime = 5;
    /**
     * 当前重试的次数
     */
    private int retryCurrentTime = 0;

    /**
     * mac 地址
     */
    public static final String macAddress = Utility.getMacAdress();
    /**
     * 是否是点击按键进入
     */
    private boolean isOnKeyIn = false;


    private int versionId;
    /**
     * 项目 id
     */
    private int projectId;

    /**
     * 抢答结果页面
     */
    private ResponderResultFragment resultFragment;
    /**
     * 抢答页面
     */
    private ResponderFragment responderFragment;
    private VoteFragment voteFragment;
    private static WeChatWallFragment weChatWallFragment;
    private CarouselFragment carouselFragment;

    private Context mContext = this;
    private ShowPictureFragment showPictureFragment;
    private String responderResult;
    private ProgressDialog progressDialog;
    private OkHttpClient mOkClient;
    private Request mRequest;
    private static WebSocket mWebSocket;
    /**
     * 微信墙数据
     */
    private List<WeChatDetailData> allWeChatDatas = new ArrayList<>();
    /**
     * 进入微信墙成功
     */
    private boolean isInWeChatWall = false;

    /**
     * 退出按钮按下的时间
     */
    private long pressTime;
    /**
     * 松开按钮的时间
     */
    private long releaseTime;
    private TimerTask mTask;
    private Timer exitTimer;
    private MyWebSocketListener webSocketListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();

        //初始化 websocket
        initWebSocket();
        //检查更新
        checkUpdate();
    }

    /**
     * 左上角长按十秒退出
     */
    private void initView() {
        TextView tvExit = findViewById(R.id.tv_exit);
        //长按左上角十秒退出
        tvExit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        pressTime = System.currentTimeMillis();
                        Log.d(TAG, "onTouch: " + pressTime);
                        //开始计时
                        startTimer();
                        break;
                    case MotionEvent.ACTION_UP:
                        releaseTime = System.currentTimeMillis();
                        Log.d(TAG, "onTouch: " + releaseTime);
                        if ((releaseTime - pressTime) / 1000 < 10) {
                            cancelTimer();
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 开始计时
     */
    private void startTimer() {
        if (mTask != null || exitTimer != null) {
            cancelTimer();
        }
        mTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        removeWebsocket();
                        finish();
                    }
                });
            }
        };
        exitTimer = new Timer();
        exitTimer.schedule(mTask, 10000);
        Log.d(TAG, "startTimer: ");
    }

    /**
     * 取消定时器
     */
    private void cancelTimer() {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        if (exitTimer != null) {
            exitTimer.cancel();
            exitTimer = null;
        }
    }


    /**
     * 初始化websocket
     */
    private void initWebSocket() {
        try {
            webSocketListener = new MyWebSocketListener(macAddress);
            if (mWebSocket != null) {
                mRequest = mWebSocket.request();
            } else {
                mRequest = new Request.Builder()
                        .url(_URL.WEBSOCKET_URL + macAddress)
                        .build();
            }
            mOkClient = new OkHttpClient.Builder()
                    .connectTimeout(10000, TimeUnit.MILLISECONDS)
                    .readTimeout(10000, TimeUnit.MILLISECONDS)
                    .writeTimeout(10000, TimeUnit.MILLISECONDS)
                    .build();
            mWebSocket = mOkClient.newWebSocket(mRequest, webSocketListener);
            mOkClient.dispatcher().executorService().shutdown();
        } catch (Exception e) {
            removeWebsocket();
            Log.e(TAG, "initWebSocket: " + e.toString());
        }
    }

    /**
     * 重新链接 websocket
     */
    public synchronized void restartConnectWebSocket() {
        if (retryCurrentTime <= retryAllTime) {
            //初始化 websocket 链接
            initWebSocket();
            retryCurrentTime++;
        } else {
            retryCurrentTime = 0;
            //手动断开 websocket
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response response = OkHttpUtils.getInstance().getDataSync(_URL.BREAK_DEVICE + macAddress);
                        String str = response.body().string();
                        Log.d(TAG, "restartConnectWebSocket:" + str);
                        final JSONObject object = new JSONObject(str);
                        int status = object.getInt("status");
                        if (status == 0) {
                            //断开成功，重新链接
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initWebSocket();
                                }
                            });
                        } else if (status == 1) {
                            //断开失败报错
                            final String errStr = object.getString("data");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext, errStr, Toast.LENGTH_LONG).show();
                                    restartApplication();
                                }
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    /**
     * 软件重启
     */
    private void restartApplication() {
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * 检查软件更新
     */
    private void checkUpdate() {
        OkHttpUtils.getInstance().getDataAsyn(_URL.UPDATE_VERSION_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.d(TAG, "onResponse: " + json);
                UpdateData data = new Gson().fromJson(json, UpdateData.class);
                int newestVersion = data.getData().getId();
                versionId = newestVersion;
                int currentVersion = getCurrentVersion();
                final String appName = data.getData().getAppName();
                Log.d(TAG, "checkUpdate: newestVersion" + newestVersion);
                Log.d(TAG, "checkUpdate: currentVersion" + currentVersion);
                if (newestVersion > currentVersion) {
                    String downloadUrl = data.getData().getSrc();
                    Log.d(TAG, "checkUpdate:downloadUrl" + downloadUrl);

                    OkHttpUtils.getInstance().downloadApk(downloadUrl, appName, new OkHttpUtils.DownloadListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Log.e(TAG, "onFailure:下载失败 " + e.toString());
                            dismissProgressDialog("", STATUS_ERROR);
                        }

                        @Override
                        public void onSuccess(String path) {
                            Log.d(TAG, "onSuccess: " + path);
                            dismissProgressDialog(path, STATUS_SUCCESS);
                        }

                        @Override
                        public void onProgress(final int progress) {
                            showProgressDialog(progress);
                        }
                    });
                }
            }
        });
    }

    /**
     * 显示 下载进度条
     *
     * @param progress
     */
    public void showProgressDialog(final int progress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(mContext);
                    progressDialog.setTitle("正在下载");
//                    progressDialog.setCancelable(false);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setMax(100);
                    progressDialog.show();
                }
                progressDialog.setProgress(progress);
            }
        });
    }

    /**
     * 消失下载进度条
     *
     * @param path
     * @param status
     */
    public void dismissProgressDialog(final String path, final int status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (status == STATUS_SUCCESS) {
                    Toast.makeText(mContext, "下载成功，即将安装", Toast.LENGTH_SHORT).show();
                    installApp(path);
                } else if (status == STATUS_ERROR) {
                    Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 安装 App
     *
     * @param path
     */
    public void installApp(String path) {
        File file = new File(path);
        if (file.exists()) {
            //将version_id存到本地
            SPUtils.getInstance().putInt(SPUtils.VERSION_ID, versionId);
            //1. 创建 Intent 并设置 action
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //2. 设置 category
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            //添加 flag ,不记得在哪里看到的，说是解决：有些机器上不能成功跳转的问题
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //3. 设置 data 和 type
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            //3. 设置 data 和 type (效果和上面一样)
            //intent.setDataAndType(Uri.parse("file://" + targetFile.getPath()),"application/vnd.android.package-archive");
            //4. 启动 activity
            startActivity(intent);
        } else {
            Toast.makeText(mContext, "文件不存在", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取当前版本信息
     *
     * @return
     */
    public int getCurrentVersion() {
        int currentCode = 0;
        currentCode = SPUtils.getInstance().getInt(SPUtils.VERSION_ID);
        return currentCode;
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
                if (weChatWallFragment == null) {
                    weChatWallFragment = new WeChatWallFragment();
                }
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
        allWeChatDatas.addAll(weChatDataList);
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
     * 抢答调用此方法
     */
    public void responder() {
        WeChatData data = new WeChatData();
        data.setDatas(null);
        data.setCode(7);
        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonData = gson.toJson(data);
        Log.d(TAG, "responder: " + jsonData);
        mWebSocket.send(jsonData);
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

    /**
     * 关闭 websocket
     */
    public static void removeWebsocket() {
        if (mWebSocket != null) {
            mWebSocket.close(1000, "软件退出");
        }
    }

    /**
     * 传递抢答结果
     */
    public String getResponderResult() {
        return responderResult;
    }

    @Override
    protected void onStop() {
        removeWebsocket();
        Log.d(TAG, "onStop: 已关闭");
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * 按键监听
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyUp: " + keyCode);
        //当点击 home 键的时候，进入留言板块页面, 留言板块页面从连接终端以后数据一直记录，哪怕页面没有弹起
        if (keyCode == 122) {
            switchFragment(WE_CHAT_WALL_FRAGMENT_ID);
        }
        return super.onKeyUp(keyCode, event);
    }

    private class MyWebSocketListener extends WebSocketListener {
        private String macAddress;

        public MyWebSocketListener(String macAddress) {
            this.macAddress = macAddress;
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            Log.d(TAG, "onOpen: ");
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
                //页面id
                int pageCode = 0;
                //只有当 code == 2 切换页面 或则是 code == 8 抢答服务器广播的时候需要
                if (code == 2 || code == 8) {
                    data = new Gson().fromJson(text, WebSocketData.class);
                    if (code == 2) {
                        pageCode = data.getData().getPage();
                    }
                    if (data.getData() != null) {
                        if (pageCode == 1 || pageCode == 2) {
                            projectId = data.getData().getProjectId();
                            Log.d(TAG, "onMessage: " + projectId);
                        }
                    }
                }
                switch (code) {
                    //切换页面
                    case 2:
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
                        //设置抢答结果
                        responderResult = data.getData().getName();
                        //切换抢答结果页面
                        switchFragment(RESPONDER_RESULT_FRAGMENT_ID);
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
                removeWebsocket();
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            this.onClosed(webSocket, code, reason);
            Log.d(TAG, "onClosing: " + reason + code);
            //重新链接 websocket
            restartConnectWebSocket();
        }

        /**
         * 正常关闭
         */
        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            Log.d(TAG, "onClosed: " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(mWebSocket, t, response);
            t.printStackTrace();
            Log.e(TAG, "onFailure: Throwable " + t.toString());
            restartConnectWebSocket();
        }
    }
}

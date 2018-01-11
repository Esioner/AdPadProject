package com.esioner.votecenter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import java.util.List;
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
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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
    private static int downloadRetryTime = 0;

    private int versionId;
    /**
     * 项目 id
     */
    private int projectId;

    private ResponderResultFragment resultFragment;
    private ResponderFragment responderFragment;
    private VoteFragment voteFragment;
    private WeChatWallFragment weChatWallFragment;
    private CarouselFragment carouselFragment;
    private WebSocket webSocket;
    private Context mContext = this;
    private ShowPictureFragment showPictureFragment;
    private String responderResult;
    private ProgressDialog progressDialog;
    ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        initView();
        //检查更新
        checkUpdate();
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
//                    String downloadUrl = "http://p1.exmmw.cn/p1/pp/xgsp.apk";
                    Log.d(TAG, "checkUpdate:downloadUrl" + downloadUrl);

                    downloadRetryTime++;

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
//                            Log.d(TAG, "onProgress: " + progress);
                            showProgressDialog(progress);
                        }
                    });
                }
            }
        });
    }

    /**
     * @param progress
     */
    public void showProgressDialog(final int progress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(mContext);
                    progressDialog.setTitle("正在下载");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setMax(100);
                    progressDialog.show();
                }
                progressDialog.setProgress(progress);
            }
        });
    }

    /**
     * 消失对话框
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
                            responderResult = data.getData().getName();
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
     * 抢答调用此方法
     */
    public void responder() {
        WeChatData data = new WeChatData();
        data.setDatas(null);
        data.setCode(7);
        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonData = gson.toJson(data);
        Log.d(TAG, "responder: " + jsonData);
        webSocket.send(jsonData);
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
     * 传递 抢答结果
     */
    public String getResponderResult() {
        return responderResult;
    }

    @Override
    protected void onStop() {
        webSocket.close(1000, "软件退出");
        Log.d(TAG, "onStop: 已关闭");
        super.onStop();
    }
}

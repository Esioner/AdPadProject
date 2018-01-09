package com.esioner.votecenter.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.shuyu.gsyvideoplayer.listener.GSYVideoShotListener;
import com.shuyu.gsyvideoplayer.listener.GSYVideoShotSaveListener;
import com.shuyu.gsyvideoplayer.listener.StandardVideoAllCallBack;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;

import java.io.File;

/**
 * @author Esioner
 * @date 2018/1/9
 */

public class MyVideoPlayer extends StandardGSYVideoPlayer {
    public MyVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public MyVideoPlayer(Context context) {
        super(context);
    }

    public MyVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
    }

    /**
     * 继承后重写可替换为你需要的布局
     *
     * @return
     */
    @Override
    public int getLayoutId() {
        return super.getLayoutId();
    }

    /**
     * 显示wifi确定框
     */
    @Override
    public void startPlayLogic() {
        super.startPlayLogic();
    }

    /**
     * 显示wifi确定框，如需要自定义继承重写即可
     */
    @Override
    protected void showWifiDialog() {
        super.showWifiDialog();
    }

    /**
     * 触摸显示滑动进度dialog，如需要自定义继承重写即可，记得重写dismissProgressDialog
     *
     * @param deltaX
     * @param seekTime
     * @param seekTimePosition
     * @param totalTime
     * @param totalTimeDuration
     */
    @Override
    protected void showProgressDialog(float deltaX, String seekTime, int seekTimePosition, String totalTime, int totalTimeDuration) {

    }

    @Override
    protected void dismissProgressDialog() {

    }

    /**
     * 触摸音量dialog，如需要自定义继承重写即可，记得重写dismissVolumeDialog
     *
     * @param deltaY
     * @param volumePercent
     */
    @Override
    protected void showVolumeDialog(float deltaY, int volumePercent) {

    }

    @Override
    protected void dismissVolumeDialog() {

    }

    /**
     * 触摸亮度dialog，如需要自定义继承重写即可，记得重写dismissBrightnessDialog
     *
     * @param percent
     */
    @Override
    protected void showBrightnessDialog(float percent) {

    }

    @Override
    protected void dismissBrightnessDialog() {
    }

    @Override
    protected void cloneParams(GSYBaseVideoPlayer from, GSYBaseVideoPlayer to) {
        super.cloneParams(from, to);
    }

    /**
     * 将自定义的效果也设置到全屏
     *
     * @param context
     * @param actionBar 是否有actionBar，有的话需要隐藏
     * @param statusBar 是否有状态bar，有的话需要隐藏
     * @return
     */
    @Override
    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
        return super.startWindowFullscreen(context, actionBar, statusBar);
    }

    /**
     * 点击触摸显示和隐藏逻辑
     */
    @Override
    protected void onClickUiToggle() {
        super.onClickUiToggle();
        hideAllWidget();
    }

    @Override
    protected void hideAllWidget() {
        super.hideAllWidget();
    }

    @Override
    protected void changeUiToNormal() {
        super.changeUiToNormal();
    }

    @Override
    protected void changeUiToPreparingShow() {
        super.changeUiToPreparingShow();
    }

    @Override
    protected void changeUiToPlayingShow() {
        super.changeUiToPlayingShow();
    }

    @Override
    protected void changeUiToPauseShow() {
        super.changeUiToPauseShow();
    }

    @Override
    protected void changeUiToPlayingBufferingShow() {
        super.changeUiToPlayingBufferingShow();
    }

    @Override
    protected void changeUiToCompleteShow() {
        super.changeUiToCompleteShow();
    }

    @Override
    protected void changeUiToError() {
        super.changeUiToError();
    }

    @Override
    protected void changeUiToPrepareingClear() {
        super.changeUiToPrepareingClear();
    }

    @Override
    protected void changeUiToPlayingClear() {
        super.changeUiToPlayingClear();
    }

    @Override
    protected void changeUiToPauseClear() {
        super.changeUiToPauseClear();
    }

    @Override
    protected void changeUiToPlayingBufferingClear() {
        super.changeUiToPlayingBufferingClear();
    }

    @Override
    protected void changeUiToClear() {
        super.changeUiToClear();
    }

    @Override
    protected void changeUiToCompleteClear() {
        super.changeUiToCompleteClear();
    }

    /**
     * 定义开始按键显示
     */
    @Override
    protected void updateStartImage() {
        super.updateStartImage();
    }

    /**
     * 底部进度条-弹出的
     *
     * @param drawable
     * @param thumb
     */
    @Override
    public void setBottomShowProgressBarDrawable(Drawable drawable, Drawable thumb) {
    }

    /**
     * 底部进度条-非弹出
     *
     * @param drawable
     */
    @Override
    public void setBottomProgressBarDrawable(Drawable drawable) {
        super.setBottomProgressBarDrawable(drawable);
    }

    /**
     * 声音进度条
     *
     * @param drawable
     */
    @Override
    public void setDialogVolumeProgressBar(Drawable drawable) {
        super.setDialogVolumeProgressBar(drawable);
    }

    /**
     * 中间进度条
     *
     * @param drawable
     */
    @Override
    public void setDialogProgressBar(Drawable drawable) {
        super.setDialogProgressBar(drawable);
    }

    /**
     * 中间进度条字体颜色
     *
     * @param highLightColor
     * @param normalColor
     */
    @Override
    public void setDialogProgressColor(int highLightColor, int normalColor) {
        super.setDialogProgressColor(highLightColor, normalColor);
    }

    @Override
    public void setStandardVideoAllCallBack(StandardVideoAllCallBack standardVideoAllCallBack) {
        super.setStandardVideoAllCallBack(standardVideoAllCallBack);
    }

    /**
     * 获取截图
     *
     * @param gsyVideoShotListener
     */
    @Override
    public void taskShotPic(GSYVideoShotListener gsyVideoShotListener) {
        super.taskShotPic(gsyVideoShotListener);
    }

    /**
     * 获取截图
     *
     * @param gsyVideoShotListener
     * @param high                 是否需要高清的
     */
    @Override
    public void taskShotPic(GSYVideoShotListener gsyVideoShotListener, boolean high) {
        super.taskShotPic(gsyVideoShotListener, high);
    }

    /**
     * 保存截图
     *
     * @param file
     * @param gsyVideoShotSaveListener
     */
    @Override
    public void saveFrame(File file, GSYVideoShotSaveListener gsyVideoShotSaveListener) {
        super.saveFrame(file, gsyVideoShotSaveListener);
    }

    /**
     * 保存截图
     *
     * @param file
     * @param high                     是否需要高清的
     * @param gsyVideoShotSaveListener
     */
    @Override
    public void saveFrame(File file, boolean high, GSYVideoShotSaveListener gsyVideoShotSaveListener) {
        super.saveFrame(file, high, gsyVideoShotSaveListener);
    }
}

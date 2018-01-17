package com.esioner.votecenter.utils;

/**
 * @author Esioner
 * @date 2018/1/4
 */

public class _URL {
    /**
     * Base Url
     */
    public static final String BASE_IP = "116.62.228.3:8089";
    /**
     * 强制断开设备
     */
    public static final String BREAK_DEVICE = "http://" + BASE_IP + "/adv/api/ws/break?mac=";
    /**
     * websocket 地址
     */
    public static final String WEBSOCKET_URL = "ws://" + BASE_IP + "/adv/terminal?mac=";
    /**
     * 轮播数据地址
     */
    public static final String CAROUSEL_DATA_URL = "http://" + BASE_IP + "/adv/api/project/getById?projectId=";

    /**
     * 投票列表地址
     */
    public static final String VOTE_DATA_URL = "http://" + BASE_IP + "/adv/api/vote/getVoteList";

    /**
     * 获取某个某个投票项目
     * voteId=投票项目id（必须）
     */
    public static final String VOTE_DETAIL_DATA_URL = "http://" + BASE_IP + "/adv/api/vote/getById?voteId=";

    /**
     * 获取抢答结果
     */
    public static final String RESPONDER_RESULT_URL = "http://" + BASE_IP + ":8089/adv/api/answer/getAnswerResult";
    /**
     * 获取微信墙背景图片
     */
    public static String WE_CHAT_WALL_BACKGROUND_URL = "http://" + BASE_IP + "/adv/api/wxwall/getBGImage";
    /**
     * 获取终端信息
     */
    public static String TERMINAL_INFO = "http://" + BASE_IP + "/adv/api/terminal/getByMac?mac=";
    /**
     * 抢答背景
     */
    public static String RESPONDER_BACKGROUND_URL = "http://" + BASE_IP + "/adv/api/answer/getBGImage";

    /**
     * 版本更新 url
     */
    public static String UPDATE_VERSION_URL = "http://" + BASE_IP + "/adv/api/version/getNewestVersion";

}

package com.esioner.votecenter.utils;

/**
 * @author Esioner
 * @date 2018/1/4
 */

public class _URL {
    /**
     * 轮播数据地址
     */
    public static final String CAROUSEL_DATA_URL = "http://116.62.228.3:8089/adv/api/project/getById?projectId=";

    /**
     * 投票列表地址
     */
    public static final String VOTE_DATA_URL = "http://116.62.228.3:8089/adv/api/vote/getVoteList";

    /**
     * 获取某个某个投票项目
     * voteId=投票项目id（必须）
     */
    public static final String VOTE_DETAIL_DATA_URL = "http://116.62.228.3:8089/adv/api/vote/getById?voteId=";

    /**
     * 获取抢答结果
     */
    public static final String RESPONDER_RESULT_URL = "http://116.62.228.3:8089/adv/api/answer/getAnswerResult";
    /**
     * 获取微信墙背景图片
     */
    public static String WE_CHAT_WALL_BACKGROUND_URL = "http://116.62.228.3:8089/adv/api/wxwall/getBGImage";
    /**
     * 获取终端信息
     */
    public static String TERMINAL_INFO = "http://116.62.228.3:8089/adv/api/terminal/getByMac?mac=";
    /**
     * 抢答背景
     */
    public static String RESPONDER_BACKGROUND_URL = "http://116.62.228.3:8089/adv/api/answer/getBGImage";

}

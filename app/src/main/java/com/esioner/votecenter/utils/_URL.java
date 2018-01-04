package com.esioner.votecenter.utils;

/**
 * @author Esioner
 * @date 2018/1/4
 */

public class _URL {
    /**
     * 轮播数据地址
     */
    public static final String CAROUSEL_DATA_URL = "http://10.60.45.44:8089/adv/api/project/getProjectList";

    /**
     * 投票列表地址
     */
    public static final String VOTE_DATA_URL = "http://10.60.45.44:8089/adv/api/vote/getVoteList";

    /**
     * 获取某个某个投票项目
     * voteId=投票项目id（必须）
     */
    public static final String VOTE_DETAIL_DATA_URL = "http://10.60.45.44:8089/adv/api/vote/getById?voteId=";

}

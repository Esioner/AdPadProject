package com.esioner.votecenter.entity;

/**
 * @author Esioner
 * @date 2018/1/8
 */

import com.google.gson.annotations.SerializedName;

/**
 * {
 * "code": 9,
 * "data": [{
 * "id": 1,
 * "mac": "abc123",
 * "terminalName": "111",
 * "state": 1,
 * "content": "哈哈"
 * }]
 * }
 */
public class WeChatResultData {

    /**
     * 成功为 0
     * 失败为 1
     */
    private int status;
    @SerializedName("numberPerPage")
    private int numberPerPage;
    @SerializedName("currentPage")
    private int currentPage;
    @SerializedName("data")
    private WeChatDetailData data;
    @SerializedName("totalNumber")
    private int totalNumber;
    @SerializedName("totalPage")
    private int totalPage;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNumberPerPage() {
        return numberPerPage;
    }

    public void setNumberPerPage(int numberPerPage) {
        this.numberPerPage = numberPerPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public WeChatDetailData getData() {
        return data;
    }

    public void setData(WeChatDetailData data) {
        this.data = data;
    }
}

package com.esioner.votecenter.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author Esioner
 * @date 2018/1/10
 * 微信背景
 */

/**
 * {
 * "status": 0,
 * 成功为 0
 * 失败为 1
 * "numberPerPage": 0,
 * "currentPage": 0,
 * "totalNumber": 0,
 * "totalPage": 0
 * }
 */
public class WeChatBackgroundData {
    private int status;
    @SerializedName("data")
    private Data data;
    @SerializedName("numberPerPage")
    private int numberPerPage;
    @SerializedName("currentPage")
    private int currentPage;
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
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

    public class Data {
        /**
         * "data": {
         * "id": 1,
         * "name": "微信墙背景图片",
         * "type": 0,  //请根据type来分
         * "imgSrc": "l2333"
         * },
         */

        private int id;
        private String name;
        private int type;
        private String imgSrc;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getImgSrc() {
            return imgSrc;
        }

        public void setImgSrc(String imgSrc) {
            this.imgSrc = imgSrc;
        }
    }
}

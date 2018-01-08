package com.esioner.votecenter.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Esioner
 * @date 2018/1/8
 */

public class WebSocketData {
    /**
     * 0-未知页面，
     * 1-轮播页面，
     * 2-投票页面，
     * 3-抢答页面，
     * 4-微信墙页面
     */
    @SerializedName("code")
    private int code;
    @SerializedName("data")
    private Data data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private List<String> macs;
        private String mac;
        private String name;
        private int page;
        /**
         * 如果是轮播页面，则是轮播项目的id；如果是投票页面，则是投票项目的id；如果是其他页面，则忽略
         */
        @SerializedName("projectId")
        private int projectId;


        public int getProjectId() {
            return projectId;
        }

        public void setProjectId(int projectId) {
            this.projectId = projectId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }
    }
}

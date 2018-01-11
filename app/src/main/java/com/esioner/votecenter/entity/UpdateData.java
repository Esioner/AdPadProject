package com.esioner.votecenter.entity;

/**
 * @author Esioner
 * @date 2018/1/11
 */

import com.google.gson.annotations.SerializedName;

/**
 * {
 * "status": 0,
 * "data":{
 * "id": 1,
 * "vesionId": "2.0.0",
 * "src": "xxx",
 * "appName": "2.apk",
 * "description": "dnkjadan"
 * }
 * "numberPerPage": 0,
 * "currentPage": 0,
 * "totalNumber": 0,
 * "totalPage": 0
 * }
 */
public class UpdateData {
    /**
     * 成功为 0
     * 失败为 1
     */
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

    /**
     * "data":{
     * "id": 1,
     * "vesionId": "2.0.0",
     * "src": "xxx",
     * "appName": "2.apk",
     * "description": "dnkjadan"
     * }
     */
    public class Data {
        private int id;
        private String vesionId;
        private String src;
        private String appName;
        private String description;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getVesionId() {
            return vesionId;
        }

        public void setVesionId(String vesionId) {
            this.vesionId = vesionId;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}

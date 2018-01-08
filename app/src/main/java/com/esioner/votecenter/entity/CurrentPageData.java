package com.esioner.votecenter.entity;

/**
 * @author Esioner
 * @date 2018/1/8
 */

public class CurrentPageData {

    private int code;
    private Data data;

    public class Data {
        private String mac;
        private int page;

        public Data(String mac, int page) {
            setMac(mac);
            setPage(page);
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
}

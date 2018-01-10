package com.esioner.votecenter.entity;

/**
 * @author Esioner
 * @date 2018/1/10
 */

public class WeChatDetailData {
    /**
     * {
     * "id": 1,
     * "mac": "abc123",
     * "terminalName": "111",
     * "state": 1,
     * "content": "哈哈"
     * }
     */
    private int id;
    private int state;
    private String mac;
    private String terminalName;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

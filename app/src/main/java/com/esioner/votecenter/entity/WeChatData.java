package com.esioner.votecenter.entity;

/**
 * @author Esioner
 * @date 2018/1/8
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

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
public class WeChatData {
    private int code;
    @SerializedName("data")
    private List<WeChatDetailData> datas;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<WeChatDetailData> getDatas() {
        return datas;
    }

    public void setDatas(List<WeChatDetailData> datas) {
        this.datas = datas;
    }


}

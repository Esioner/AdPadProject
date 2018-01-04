package com.esioner.votecenter.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Esioner
 * @date 2018/1/4
 */

public class VoteDetailData {
    private int status;
    @SerializedName("data")
    private List<Data> dataList;
    @SerializedName("numberPerPage")
    private int numberPerPage;
    @SerializedName("currentPage")
    private int currentPage;
    @SerializedName("totalNumber")
    private int totalNumber;
    @SerializedName("totalPage")
    private int totalPage;

    public class Data {
        private int id;
        private String name;
        private int allCanVoteNumber;
        private int eachItemCanVoteNumber;
        @SerializedName("voteItems")
        private List<VoteItems> voteItemsList;

        public class VoteItems {
            private int id;
            private String name;
            private String imgSrc;
            private int result;
            private int voteId;
        }
    }
}

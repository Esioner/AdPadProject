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
        private int id;
        private String name;
        @SerializedName("allCanVoteNumber")
        private int allCanVoteNumber;
        @SerializedName("eachItemCanVoteNumber")
        private int eachItemCanVoteNumber;
        @SerializedName("voteItems")
        private List<VoteItems> voteItemsList;

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

        public int getAllCanVoteNumber() {
            return allCanVoteNumber;
        }

        public void setAllCanVoteNumber(int allCanVoteNumber) {
            this.allCanVoteNumber = allCanVoteNumber;
        }

        public int getEachItemCanVoteNumber() {
            return eachItemCanVoteNumber;
        }

        public void setEachItemCanVoteNumber(int eachItemCanVoteNumber) {
            this.eachItemCanVoteNumber = eachItemCanVoteNumber;
        }

        public List<VoteItems> getVoteItemsList() {
            return voteItemsList;
        }

        public void setVoteItemsList(List<VoteItems> voteItemsList) {
            this.voteItemsList = voteItemsList;
        }

        public class VoteItems {
            private int id;
            private String name;
            private String imgSrc;
            private int result;
            private int voteId;

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

            public String getImgSrc() {
                return imgSrc;
            }

            public void setImgSrc(String imgSrc) {
                this.imgSrc = imgSrc;
            }

            public int getResult() {
                return result;
            }

            public void setResult(int result) {
                this.result = result;
            }

            public int getVoteId() {
                return voteId;
            }

            public void setVoteId(int voteId) {
                this.voteId = voteId;
            }
        }
    }
}

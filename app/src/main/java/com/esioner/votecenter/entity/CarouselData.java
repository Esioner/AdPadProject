package com.esioner.votecenter.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Esioner
 * @date 2018/1/4
 */

public class CarouselData {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Data> getDataList() {
        return dataList;
    }

    public void setDataList(List<Data> dataList) {
        this.dataList = dataList;
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

    @Override
    public String toString() {
        return "CarouselData{" +
                "status=" + status +
                ", dataList=" + dataList +
                ", numberPerPage=" + numberPerPage +
                ", currentPage=" + currentPage +
                ", totalNumber=" + totalNumber +
                ", totalPage=" + totalPage +
                '}';
    }

    public class Data {
        private int id;
        private String name;
        @SerializedName("materials")
        private List<Materials> materials;

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

        public List<Materials> getMaterials() {
            return materials;
        }

        public void setMaterials(List<Materials> materials) {
            this.materials = materials;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", materials=" + materials +
                    '}';
        }

        public class Materials {
            @SerializedName("id")
            private int id;
            @SerializedName("name")
            private String name;
            @SerializedName("type")
            private int type;
            @SerializedName("time")
            private int time;
            @SerializedName("src")
            private String src;
            @SerializedName("projectId")
            private int projectId;

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

            public int getTime() {
                return time;
            }

            public void setTime(int time) {
                this.time = time;
            }

            public String getSrc() {
                return src;
            }

            public void setSrc(String src) {
                this.src = src;
            }

            public int getProjectId() {
                return projectId;
            }

            public void setProjectId(int projectId) {
                this.projectId = projectId;
            }

            @Override
            public String toString() {
                return "Materials{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        ", type=" + type +
                        ", time=" + time +
                        ", src='" + src + '\'' +
                        ", projectId=" + projectId +
                        '}';
            }
        }
    }
}

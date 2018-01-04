package com.esioner.votecenter;

/**
 * @author Esioner
 * @date 2018/1/4
 */

public class Bean {
    int type = -1;
    String path;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 判断是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        if (type != -1 && path != null) {
            return false;
        } else {
            return true;
        }
    }
}

package com.michel.pointscredit.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @Annotation <p>由於Transaction效率太低了，
 * 用組合Model</p>
 * @Auth Sunny
 * @date 2018/3/18
 * @Version V1.0.0
 */

public class TrascationItemBean implements Serializable {

    private boolean isOut;//是否是轉出
    private String userName;
    private String  updateTime;
    private String  sum;

    public boolean isOut() {
        return isOut;
    }

    public void setOut(boolean out) {
        isOut = out;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "TrascationItemBean{" + "isOut=" + isOut + ", userName='" + userName + '\'' + ", " +
                "updateTime='" + updateTime + '\'' + ", sum='" + sum + '\'' + '}';
    }
}

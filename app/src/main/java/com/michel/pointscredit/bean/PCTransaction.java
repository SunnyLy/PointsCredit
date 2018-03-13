package com.michel.pointscredit.bean;

import com.parse.ParseObject;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by 80010651 on 2018/3/13.
 * 用户的交易记录：
 * 包含转账记录，用户账户余额
 */

public class PCTransaction extends ParseObject {

    private JSONArray users;//包含两个用户的objectId
    private List<PCUser> to;//接收方
    private List<PCUser> from;//转账方
    private double amount;//

    public JSONArray getUsers() {
        return getJSONArray("users");
    }

    public PCUser getTo() {
        return (PCUser) getParseObject("to");
    }

    public PCUser getFrom() {
        return (PCUser) getParseObject("from");
    }

    public double getAmount() {
        return getDouble("amount");
    }

    public void setAmount(double amount) {
        put("amount", amount);
    }
}

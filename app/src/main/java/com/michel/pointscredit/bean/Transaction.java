package com.michel.pointscredit.bean;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by 80010651 on 2018/3/13.
 * 用户的交易记录：
 * 包含转账记录，用户账户余额
 */
@ParseClassName("Transaction")
public class Transaction extends ParseObject {

    public static final String TAG = Transaction.class.getSimpleName();
    private JSONArray users;//包含两个用户的objectId
    private List<User> to;//接收方
    private List<User> from;//转账方
    private double amount;//

    public JSONArray getUsers() {
        return getJSONArray("users");
    }

    public User getTo() {
        return (User) getParseObject("to");
    }

    public User getFrom() {
        return (User) getParseObject("from");
    }

    public double getAmount() {
        return getDouble("amount");
    }

    public void setAmount(double amount) {
        put("amount", amount);
    }
}

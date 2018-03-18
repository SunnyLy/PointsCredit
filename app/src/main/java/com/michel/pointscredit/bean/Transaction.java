package com.michel.pointscredit.bean;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

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
    private List<String> users;//包含两个用户的objectId
    private ParseObject to;//接收方
    private ParseObject from;//转账方
    private double amount;//

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public ParseObject getTo() {
        return to;
    }

    public void setTo(ParseObject to) {
        this.to = to;
    }

    public ParseObject getFrom() {
        return from;
    }

    public void setFrom(ParseObject from) {
        this.from = from;
    }
}

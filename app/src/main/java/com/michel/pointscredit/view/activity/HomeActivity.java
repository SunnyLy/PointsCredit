package com.michel.pointscredit.view.activity;

import android.os.Bundle;
import android.util.Log;

import com.michel.pointscredit.R;
import com.michel.pointscredit.base.PCBaseActivity;
import com.michel.pointscredit.bean.Transaction;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by 80010651 on 2018/3/14.
 */

public class HomeActivity extends PCBaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseQuery<Transaction> parseQuery = ParseQuery.getQuery(Transaction.TAG);
        if (ParseUser.getCurrentUser() == null) return;
        //objectId:EHWkHpMtD7
        Log.e("pc:home", "objectId=" + ParseUser.getCurrentUser().getObjectId());
        parseQuery.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
//        parseQuery.whereEqualTo("objectId", "4LoVmT5twu");
        parseQuery.setLimit(1000);//最多拉取1000条数据
        parseQuery.findInBackground(new FindCallback<Transaction>() {
            @Override
            public void done(List<Transaction> objects, ParseException e) {
                if (objects != null && objects.size() > 0) {
                    Log.e("pc:home", "transactions:" + objects.size());
                }
                Log.e("pc:home", e == null ? "e==null" : e.getMessage());
            }
        });
    }
}

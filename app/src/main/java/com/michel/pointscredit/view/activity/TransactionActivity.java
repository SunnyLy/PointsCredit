package com.michel.pointscredit.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.michel.pointscredit.R;
import com.michel.pointscredit.adapter.PCTransactionAdapter;
import com.michel.pointscredit.adapter.PCTransactionAdapter2;
import com.michel.pointscredit.base.PCBaseActivity;
import com.michel.pointscredit.bean.Transaction;
import com.michel.pointscredit.bean.TrascationItemBean;
import com.michel.pointscredit.view.widget.PCCommonTitleLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 80010651 on 2018/3/14.
 * 账单明细交易记录。。
 */

public class TransactionActivity extends PCBaseActivity {

    private static final String TAG = "transations";

    @BindView(R.id.transaction_title)
    PCCommonTitleLayout mTitleBar;

    @BindView(R.id.transaction_listview)
    RecyclerView mRecyclerView;
    private PCTransactionAdapter2 recycleAdapter;

    private ArrayList<TrascationItemBean> mTransactions = new ArrayList<>();

    public static void startTransactionActivity(@NonNull Context context, ArrayList<TrascationItemBean>
            parseObjects) {
        Intent intent = new Intent(context, TransactionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,parseObjects);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_transation;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewsOnClickListener(mTitleBar.getLeftBackView());
    }

    private void getTranstactions() {
        final ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(Transaction.TAG);
        showLoading();
        parseQuery.whereContains("users", "4LoVmT5twu");
//        parseQuery.whereContains("users", ParseUser.getCurrentUser().getObjectId());
        parseQuery.setLimit(1000);//最多拉取1000条数据
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                dismissLoading();
                if (objects != null && objects.size() > 0) {
                    Log.e("pc:home", "transactions:" + objects.size());
                    mTransactions.clear();
                    double sum=0;
                    for (ParseObject transaction:objects){
                        double amount = transaction.getDouble("amount");
                        Log.e("pc","amount:"+amount);
                        sum += amount;
                        TrascationItemBean itemBean = transferPO2Bean(transaction);
                        if (itemBean != null)
                        mTransactions.add(itemBean);
                    }
//                    freshUI();
                    recycleAdapter.setParseObjects(mTransactions);
                }
                Log.e("pc:home", e == null ? "e==null" : e.getMessage());
            }
        });
    }

    private TrascationItemBean transferPO2Bean(ParseObject object) {
        TrascationItemBean itemBean = null;
        if (object != null){
            itemBean = new TrascationItemBean();
            ParseUser fromUser = object.getParseUser("from");
            ParseUser toUser = object.getParseUser("to");
            String fromUserId = fromUser.getObjectId();

            String userName = "";
            String amount;
            boolean isOut = false;//是不是轉出去
            if (fromUserId.equals(ParseUser.getCurrentUser().getObjectId())){
                //我轉出去的
                try {
                    userName = toUser.fetchIfNeeded().getString("firstName");
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                isOut = true;
            }else{
                //別人轉給我的
                try {
                    userName = fromUser.fetchIfNeeded().getString("firstName");
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                isOut = false;
            }
            itemBean.setUserName(userName);
            itemBean.setOut(isOut);
            Double dbMoney = object.getDouble("amount");
            DecimalFormat df = new DecimalFormat("#.000");
            String  money = "€"+df.format(dbMoney);
            itemBean.setSum(money);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date = object.getUpdatedAt();
            itemBean.setUpdateTime(sdf.format(date));
        }
        return itemBean;
    }


    @Override
    public void onContentChanged() {
       }

    @Override
    public void initParams() {
        super.initParams();
        initAdapter();
        initIntentParams();
//        getTranstactions();
    }

    private void initAdapter() {
        recycleAdapter = new PCTransactionAdapter2(mContext,mTransactions);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this );
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper. VERTICAL);
        mRecyclerView.setAdapter( recycleAdapter);
        mRecyclerView.addItemDecoration( new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }

    private void initIntentParams() {
        Intent intent = getIntent();
        if (intent != null){
            Bundle bundle = intent.getExtras();
            if (bundle != null){
                ArrayList<TrascationItemBean> objects = (ArrayList<TrascationItemBean>) bundle.getSerializable(TAG);
                if (objects != null && objects.size() >0){
                    mTransactions.clear();
                    mTransactions.addAll(objects);
                    recycleAdapter.setParseObjects(mTransactions);
                }
            }
        }

        freshUI();
    }

    private void freshUI() {
    }

    @Override
    public void onClick(@Nullable View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}

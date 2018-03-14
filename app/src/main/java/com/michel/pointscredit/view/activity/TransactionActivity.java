package com.michel.pointscredit.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.michel.pointscredit.R;
import com.michel.pointscredit.base.PCBaseActivity;

/**
 * Created by 80010651 on 2018/3/14.
 * 账单明细交易记录。。
 */

public class TransactionActivity extends PCBaseActivity {

    public static void startTransactionActivity(@NonNull Context context) {
        Intent intent = new Intent(context, TransactionActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_transation;
    }
}

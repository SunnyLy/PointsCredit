package com.michel.pointscredit.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.michel.pointscredit.R;
import com.michel.pointscredit.base.PCBaseActivity;
import com.michel.pointscredit.view.widget.PCCommonTitleLayout;

import org.jetbrains.annotations.Nullable;

import butterknife.BindView;

/**
 * Created by 80010651 on 2018/3/14.
 * 账单明细交易记录。。
 */

public class TransactionActivity extends PCBaseActivity {

    @BindView(R.id.transaction_title)
    PCCommonTitleLayout mTitleBar;

    public static void startTransactionActivity(@NonNull Context context) {
        Intent intent = new Intent(context, TransactionActivity.class);
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

    @Override
    public void onClick(@Nullable View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}

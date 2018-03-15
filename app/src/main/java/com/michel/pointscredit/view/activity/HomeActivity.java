package com.michel.pointscredit.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.michel.pointscredit.R;
import com.michel.pointscredit.base.PCBaseActivity;
import com.michel.pointscredit.bean.Transaction;
import com.michel.pointscredit.google.zxing.activity.CaptureActivity;
import com.michel.pointscredit.utils.RouterUtils;
import com.michel.pointscredit.view.widget.PCCommonTitleLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.ruffian.library.RTextView;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import butterknife.BindView;

/**
 * Created by 80010651 on 2018/3/14.
 */

public class HomeActivity extends PCBaseActivity {
    @BindView(R.id.tv_transactions)
    TextView mTransaction;
    @BindView(R.id.btn_scan)
    RTextView mBtnScan;
    @BindView(R.id.btn_qrcode)
    RTextView mBtnQrcode;
    @BindView(R.id.btn_transfer)
    RTextView mBtnTransfer;
    @BindView(R.id.login_title)
    PCCommonTitleLayout mTitleBar;

    private static final int REQUEST_CODE = 0x100;

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewsOnClickListener(mTransaction, mBtnScan, mBtnQrcode, mBtnTransfer, mTitleBar.getLeftBackView());
        ParseQuery<Transaction> parseQuery = ParseQuery.getQuery(Transaction.TAG);
        if (ParseUser.getCurrentUser() == null) return;
        //objectId:EHWkHpMtD7
        Log.e("pc:home", "objectId=" + ParseUser.getCurrentUser().getObjectId());
//        parseQuery.whereEqualTo("objectId", "4LoVmT5twu");
        parseQuery.whereContains("users", ParseUser.getCurrentUser().getObjectId());
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

    @Override
    public void onClick(@Nullable View view) {
        switch (view.getId()) {
            case R.id.tv_transactions:
                TransactionActivity.startTransactionActivity(this);
                break;
            case R.id.btn_scan:
                RouterUtils.jump2TargetForResult(this, CaptureActivity.class, REQUEST_CODE);
                break;
            case R.id.btn_qrcode:
//                ParseUser user = ParseUser.getCurrentUser();
//                if (user != null) {
//                    String userId = user.getObjectId();
//                    Bundle params = new Bundle();
//                    params.putString(MyQrcodeActivity.USER_ID, userId);
//                    RouterUtils.jump2TargetWithBundle(this, MyQrcodeActivity.class, params);
//                }
                test();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void test() {
        Bundle params = new Bundle();
        params.putString(MyQrcodeActivity.USER_ID, "EHWkHpMtD7");
        RouterUtils.jump2TargetWithBundle(this, MyQrcodeActivity.class, params);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (resultCode == RESULT_OK) { //RESULT_OK = -1
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN);
            //将扫描出的信息显示出来
            Toast.makeText(HomeActivity.this, scanResult + "", Toast.LENGTH_SHORT).show();
        }
    }
}

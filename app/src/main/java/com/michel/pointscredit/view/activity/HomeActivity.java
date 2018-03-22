package com.michel.pointscredit.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.Result;
import com.michel.pointscredit.R;
import com.michel.pointscredit.base.PCBaseActivity;
import com.michel.pointscredit.bean.Transaction;
import com.michel.pointscredit.bean.TrascationItemBean;
import com.michel.pointscredit.callback.IPositiveClickListener;
import com.michel.pointscredit.google.zxing.activity.CaptureActivity;
import com.michel.pointscredit.utils.QrCodeUtils;
import com.michel.pointscredit.utils.RouterUtils;
import com.michel.pointscredit.view.widget.PCCommonTitleLayout;
import com.michel.pointscredit.view.widget.PCDialogManger;
import com.michel.pointscredit.view.widget.SimplexToast;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.ruffian.library.RTextView;

import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import bolts.Continuation;
import bolts.Task;
import butterknife.BindView;

/**
 * Created by 80010651 on 2018/3/14.
 */

public class HomeActivity extends PCBaseActivity {
    private static final int IMAGE_REQUEST_CODE = 0x101;
    //交易通道
    private static final String CHANNEL = "Transactions";

    @BindView(R.id.tv_transactions)
    TextView mTransaction;
    @BindView(R.id.tv_account_sum)
    TextView mAccountSum;//账户余额
    @BindView(R.id.btn_scan)
    RTextView mBtnScan;
    @BindView(R.id.btn_qrcode)
    RTextView mBtnQrcode;
    @BindView(R.id.btn_transfer)
    RTextView mBtnTransfer;
    @BindView(R.id.login_title)
    PCCommonTitleLayout mTitleBar;

    private static final int REQUEST_CODE = 0x100;

    private boolean isAdmin;
    private ArrayList<TrascationItemBean> mTransactions = new ArrayList<>();

    private ProgressDialog mProgressDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewsOnClickListener(mTransaction, mBtnScan, mBtnQrcode, mBtnTransfer, mTitleBar
                .getLeftBackView());
        //注册消息推送
        ParsePush.subscribeInBackground(CHANNEL, new SaveCallback() {
            @Override
            public void done(ParseException e) {

            }
        });

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
        }
        if (ParseUser.getCurrentUser() == null) return;
        isAdmin = ParseUser.getCurrentUser().getBoolean("isAdmin");

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.parse.push.intent.RECEIVE");
        intentFilter.addAction("com.parse.push.intent.DELETE");
        intentFilter.addAction("com.parse.push.intent.OPEN");
        registerReceiver(pushBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTransactions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销指定通道
        ParsePush.unsubscribeInBackground(CHANNEL, new SaveCallback() {
            @Override
            public void done(ParseException e) {

            }
        });

        //注销广播
        unregisterReceiver(pushBroadcastReceiver);
    }

    double sum = 0;

    private void getTransactions() {
        showLoading();
        final ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(Transaction.TAG);
        parseQuery.whereContains("users", ParseUser.getCurrentUser().getObjectId());
        parseQuery.setLimit(1000);//最多拉取1000条数据
        parseQuery.orderByDescending("updatedAt");
        parseQuery.findInBackground().onSuccess(new Continuation<List<ParseObject>, Object>() {
            @Override
            public Object then(Task<List<ParseObject>> task) throws Exception {
                dismissLoading();
                if (task != null) {
                    sum = 0;
                    List<ParseObject> objects = task.getResult();
                    if (objects != null && objects.size() > 0) {
                        mTransactions.clear();
                        for (ParseObject transaction : objects) {
                            double amount = transaction.getDouble("amount");
                            TrascationItemBean itemBean = transferPO2Bean(transaction);
                            if (itemBean != null) mTransactions.add(itemBean);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                freshUI(sum);
                            }
                        });

                    } /*else {
                        dismissLoading();
                    }*/
                }
                return null;
            }
        });
    }

    private TrascationItemBean transferPO2Bean(ParseObject object) {
        TrascationItemBean itemBean = null;
        if (object != null) {
            itemBean = new TrascationItemBean();
            ParseUser fromUser = object.getParseUser("from");
            ParseUser toUser = object.getParseUser("to");
            String fromUserId = fromUser.getObjectId();

            String userName = "";
            String amount;
            boolean isOut = false;//是不是轉出去
            if (fromUserId.equals(ParseUser.getCurrentUser().getObjectId())) {
                //我轉出去的
                try {
                    userName = toUser.fetchIfNeeded().getString("firstName");
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                isOut = true;
            } else {
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
            DecimalFormat df = new DecimalFormat("0.00");
            String money = "€" + df.format(dbMoney);
            itemBean.setSum(money);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date = object.getUpdatedAt();
            itemBean.setUpdateTime(sdf.format(date));
            sum += isOut ? (-dbMoney) : dbMoney;
        }
        return itemBean;
    }

    /**
     * 賬戶余額，刷新界面
     *
     * @param sum
     */
    private void freshUI(double sum) {
//        mAccountSum.setText(new DecimalFormat("0.00").format(sum >= 0 ? sum : 0.0));
        mAccountSum.setText(new DecimalFormat("0.00").format(sum));
    }

    @Override
    public void onClick(@Nullable View view) {
        switch (view.getId()) {
            case R.id.tv_transactions:
                TransactionActivity.startTransactionActivity(this, mTransactions);
                break;
            case R.id.btn_scan:
                RouterUtils.jump2TargetForResult(this, CaptureActivity.class, REQUEST_CODE);
                break;
            case R.id.btn_qrcode:
                ParseUser user = ParseUser.getCurrentUser();
                if (user != null) {
                    String userId = user.getObjectId();
                    Bundle params = new Bundle();
                    params.putString(MyQrcodeActivity.USER_ID, userId);
                    RouterUtils.jump2TargetWithBundle(this, MyQrcodeActivity.class, params);
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_transfer:
                openGallery();
                break;
        }
    }

    /**
     * 打开 相册
     */
    private void openGallery() {
        //在这里跳转到手机系统相册里面
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media
                .EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //在相册里面选择好相片之后调回到现在的这个activity中
        switch (requestCode) {
            case IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {//resultcode是setResult里面设置的code值
                    String path = getImagPath(data);
                    if (!TextUtils.isEmpty(path)) {
                        Result result = QrCodeUtils.scanningImage(path);
                        if (result != null && !TextUtils.isEmpty(result.getText())) {
                            if (result.getText().equals(ParseUser.getCurrentUser().getObjectId())) {
                                SimplexToast.show(mContext, mContext.getResources().getString(R
                                        .string.not_transfer_to_self));
                                return;
                            }
                            showLoading();
                            showTransferDialog(result.getText());
                        } else {
                            SimplexToast.show(HomeActivity.this, getResources().getString(R
                                    .string.scan_no_content));
                        }
                    }
                }
                break;

            case REQUEST_CODE:
                //二维码扫描结果
                if (data == null) return;
                Bundle bundle = data.getExtras();
                String scanResult = bundle.getString(CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN);
                if (!TextUtils.isEmpty(scanResult)) {
                    //将扫描出的信息显示出来
                    SimplexToast.show(HomeActivity.this, scanResult);
                    showLoading();
                    showTransferDialog(scanResult);
                }
                break;
        }
    }

    /**
     * 根据扫描到的ObjectId,读取UserName.并显示Dialog
     *
     * @param text
     */
    private void showTransferDialog(final String text) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        if (query != null) {
            query.getInBackground(text, new GetCallback<ParseUser>() {
                @Override
                public void done(final ParseUser object, ParseException e) {
                    dismissLoading();
                    if (object != null) {
                        try {
                            final String userName = object.getString("firstName");
                            PCDialogManger.showEditableDialog(mContext, mContext.getResources()
                                    .getString(R.string.Transfer_to) + ":" + userName, mContext
                                    .getResources().
                                            getString(R.string.Input_amount), InputType.TYPE_CLASS_NUMBER
                                    | InputType.TYPE_NUMBER_FLAG_DECIMAL, null, new
                                    QMUIDialogAction.ActionListener() {


                                        @Override
                                        public void onClick(QMUIDialog qmuiDialog, int i) {

                                            qmuiDialog.dismiss();
                                        }
                                    }, mContext.getResources().getString(R.string.Transfer), new
                                    IPositiveClickListener() {
                                        @Override
                                        public void onPositiveClick(final String msg, final QMUIDialog
                                                dialog) {
                                            if (!isAdmin) {
                                                if (TextUtils.isEmpty(msg)) {
                                                    SimplexToast.show(mContext, mContext.getResources().
                                                            getString(R.string.Input_amount));
                                                    return;
                                                }

                                                if (Double.valueOf(msg) > Double.valueOf(mAccountSum
                                                        .getText().toString())) {
                                                    SimplexToast.show(mContext, mContext.getResources().
                                                            getString(R.string.Balance_insufficient));
                                                    return;
                                                }
                                            }

                                            Transaction transaction = new Transaction();
                                            transaction.put("from", ParseUser.getCurrentUser());
                                            transaction.put("to", object);
                                            List<String> list = Arrays.asList(new String[]{ParseUser
                                                    .getCurrentUser().getObjectId(), text});
                                            transaction.put("users", list);
                                            transaction.put("amount", Double.valueOf(msg));
                                            transaction.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        String tips = mContext.getResources().getString(R
                                                                .string.Succeeded) + "," + mContext
                                                                .getResources().getString(R.string
                                                                        .You_transfered) + Double.valueOf
                                                                (msg);
                                                        SimplexToast.show(mContext, tips);
                                                        TrascationItemBean itemBean = new
                                                                TrascationItemBean();
                                                        itemBean.setOut(true);
                                                        itemBean.setSum(msg);
                                                        itemBean.setUserName(userName);
                                                        SimpleDateFormat format = new SimpleDateFormat
                                                                ("dd/MM/yyyy HH:mm");
                                                        itemBean.setUpdateTime(format.format(new Date()));
                                                        mTransactions.add(0, itemBean);
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                //转账成功，刷新界面
                                                                freshUI(Double.parseDouble(msg) + Double
                                                                        .parseDouble(mAccountSum.getText
                                                                                ().toString()));
                                                            }
                                                        });
                                                        dialog.dismiss();
                                                    } else {
                                                        SimplexToast.show(mContext, e.getMessage());
                                                    }
                                                }
                                            });

                                        }
                                    });
                        } catch (ClassCastException e1) {
                            SimplexToast.show(mContext, mContext.getResources().getString(R
                                    .string.query_error));
                        }
                    } else {
                        SimplexToast.show(mContext, mContext.getResources().getString(R.string
                                .query_error));
                    }
                }
            });
        }

    }

    /**
     * 獲取选择的照片的路径
     *
     * @param data
     * @return
     */
    private String getImagPath(Intent data) {
        Cursor cursor = null;
        String path = null;
        try {
            Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            //从系统表中查询指定Uri对应的照片
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            path = cursor.getString(columnIndex);  //获取照片路径
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return path;
    }

    /**
     * 收到推送广播通知
     */
    private ParsePushBroadcastReceiver pushBroadcastReceiver = new ParsePushBroadcastReceiver() {
        @Override
        protected void onPushReceive(Context context, Intent intent) {
            super.onPushReceive(context, intent);
            if (!HomeActivity.this.isFinishing()) {
                getTransactions();
            }
        }
    };
}

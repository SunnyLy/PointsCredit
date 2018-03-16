package com.michel.pointscredit.view.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.michel.pointscredit.R;
import com.michel.pointscredit.base.PCBaseActivity;
import com.michel.pointscredit.bean.Transaction;
import com.michel.pointscredit.google.zxing.activity.CaptureActivity;
import com.michel.pointscredit.utils.QrCodeUtils;
import com.michel.pointscredit.utils.RouterUtils;
import com.michel.pointscredit.view.widget.PCCommonTitleLayout;
import com.michel.pointscredit.view.widget.SimplexToast;
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
    private static final int IMAGE_REQUEST_CODE = 0x101;
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
        parseQuery.whereContains("users", "4LoVmT5twu");
//        parseQuery.whereContains("users", ParseUser.getCurrentUser().getObjectId());
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
                ParseUser user = ParseUser.getCurrentUser();
                if (user != null) {
                    String userId = user.getObjectId();
                    Bundle params = new Bundle();
                    params.putString(MyQrcodeActivity.USER_ID, userId);
                    RouterUtils.jump2TargetWithBundle(this, MyQrcodeActivity.class, params);
                }
//                test();
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
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    private void test() {
        Bundle params = new Bundle();
        params.putString(MyQrcodeActivity.USER_ID, "EHWkHpMtD7");
        RouterUtils.jump2TargetWithBundle(this, MyQrcodeActivity.class, params);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //在相册里面选择好相片之后调回到现在的这个activity中
        switch (requestCode) {
            case IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {//resultcode是setResult里面设置的code值
                    String path = getImagPath(data);
                    if (!TextUtils.isEmpty(path)){
                       Result result =  QrCodeUtils.scanningImage(path);
                       if (result != null && !TextUtils.isEmpty(result.getText())){
                           SimplexToast.show(HomeActivity.this,result.getText());
                       }else{
                           SimplexToast.show(HomeActivity.this,getResources().getString(R.string.scan_no_content));
                       }
                    }
                }
                break;

            case REQUEST_CODE:
                //二维码扫描结果
                if (data == null)return;
                Bundle bundle = data.getExtras();
                String scanResult = bundle.getString(CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN);
                //将扫描出的信息显示出来
                SimplexToast.show(HomeActivity.this,scanResult);
                break;
        }
    }

    /**
     * 獲取选择的照片的路径
     * @param data
     * @return
     */
    private String getImagPath(Intent data) {
        Cursor cursor=null;
        String  path=null;
        try {
            Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            path = cursor.getString(columnIndex);  //获取照片路径
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
            }
        }

        return path;
    }
}

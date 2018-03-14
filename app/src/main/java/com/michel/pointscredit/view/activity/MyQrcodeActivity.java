package com.michel.pointscredit.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import com.google.zxing.WriterException;
import com.michel.pointscredit.R;
import com.michel.pointscredit.base.PCBaseActivity;
import com.michel.pointscredit.utils.QrCodeUtils;

import org.jetbrains.annotations.Nullable;

import butterknife.BindView;

/**
 * Created by 80010651 on 2018/3/14.
 * 我的二维码界面
 */

public class MyQrcodeActivity extends PCBaseActivity {

    public static final String USER_ID = "userId";
    @BindView(R.id.iv_my_qrcode)
    ImageView mIvMyQrcode;

    private Bitmap bitmap;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_qrcode;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle params = intent.getExtras();
            if (params != null) {
                String userId = params.getString(USER_ID);
                if (!TextUtils.isEmpty(userId)) {
                    try {
                        bitmap = QrCodeUtils.Create2DCode(userId);
                        mIvMyQrcode.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

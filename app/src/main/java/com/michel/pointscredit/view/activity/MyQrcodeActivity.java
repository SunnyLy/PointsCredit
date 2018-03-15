package com.michel.pointscredit.view.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.michel.pointscredit.R;
import com.michel.pointscredit.base.PCBaseActivity;
import com.michel.pointscredit.google.zxing.utils.ImageUtils;
import com.michel.pointscredit.utils.QrCodeUtils;
import com.michel.pointscredit.view.widget.PCCommonTitleLayout;
import com.michel.pointscredit.view.widget.SimplexToast;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by 80010651 on 2018/3/14.
 * 我的二维码界面
 */

public class MyQrcodeActivity extends PCBaseActivity implements EasyPermissions.PermissionCallbacks {

    public static final String USER_ID = "userId";
    private static final String DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "iScan";
    private static final String FILE_NAME = "my_qrcode";
    private static final int WRITE_EXTERNAL = 0x1;
    private static final int READ_EXTERNAL = 0x2;
    @BindView(R.id.iv_my_qrcode)
    ImageView mIvMyQrcode;
    @BindView(R.id.my_qrcode_title)
    PCCommonTitleLayout mTitleBar;

    private Bitmap bitmap;
    private Uri uri;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_qrcode;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewsOnClickListener(mTitleBar.getLeftBackView(), mTitleBar.getRightView());
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
        saveBmTask();
        uri = saveBitmap();
    }

    @AfterPermissionGranted(WRITE_EXTERNAL)
    private void saveBmTask() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            uri = saveBitmap();
        } else {
            // Request one permission
            EasyPermissions.requestPermissions(this,
                    getResources().getString(R.string.str_request_sd_message),
                    WRITE_EXTERNAL, perms);
        }
    }

    @Override
    public void onClick(@Nullable View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.title_layout_share:
                String state = Environment.getExternalStorageState();
                //如果状态不是mounted，无法读写
                if (!state.equals(Environment.MEDIA_MOUNTED)) {
                    return;
                }

                if (uri != null)
                    startShare(uri);
                break;
        }
    }

    private Uri saveBitmap() {
        if (bitmap == null) return null;
        File appDir = new File(DIR);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = FILE_NAME + ".jpg";
        File file = new File(appDir, fileName);
        if (file.exists()) file.delete();
        FileOutputStream out = null;
        try {
            ImageUtils.saveImageToSD(MyQrcodeActivity.this, file.getAbsolutePath(), bitmap, 100);
            Uri uri = Uri.parse(file.getAbsolutePath());
            return uri;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void startShare(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("image/jpg");
        intent.setType("text/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.ErCode));
        intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.ErCode));
        startActivity(Intent.createChooser(intent, getResources().getString(R.string.app_name)));

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (perms != null && perms.size() > 0) {
            uri = saveBitmap();
        } else {
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        SimplexToast.show(this, R.string.permissions_external_error);
    }

    @Override
    public void onPermissionsDenied(int i, List<String> list) {
        displayFrameworkBugMessageAndExit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}

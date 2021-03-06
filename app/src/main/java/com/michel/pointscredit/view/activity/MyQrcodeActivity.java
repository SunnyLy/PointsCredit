package com.michel.pointscredit.view.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
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

public class MyQrcodeActivity extends PCBaseActivity implements EasyPermissions
        .PermissionCallbacks {

    public static final String USER_ID = "userId";
    private static final String DIR = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "iScan";
    private static final String FILE_NAME = "qrcode.png";
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
    }

    @AfterPermissionGranted(WRITE_EXTERNAL)
    private void saveBmTask() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            saveBitmap();
        } else {
            // Request one permission
            EasyPermissions.requestPermissions(this, getResources().getString(R.string
                    .str_request_sd_message), WRITE_EXTERNAL, perms);
        }
    }

    @Override
    public void onClick(@Nullable View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.title_layout_share:
                String state = Environment.getExternalStorageState();
                //如果状态不是mounted，无法读写
                if (!state.equals(Environment.MEDIA_MOUNTED)) {
                    return;
                }

                if (EasyPermissions.hasPermissions(mContext, Manifest.permission
                        .READ_EXTERNAL_STORAGE)) {
                    startShare();
                } else {
                    EasyPermissions.requestPermissions(this, getResources().getString(R.string
                            .str_request_sd_message), WRITE_EXTERNAL, Manifest.permission
                            .READ_EXTERNAL_STORAGE);
                }
                break;
        }
    }

    /**
     * 保存二维码至SD卡
     *
     * @return
     */
    private void saveBitmap() {
        if (bitmap == null) return;
        File appDir = new File(DIR);
        if (!appDir.exists()) {
            appDir.mkdir();
        }

        File file = new File(DIR);
        if (!file.exists()) file.mkdirs();
        file = new File(file.getAbsoluteFile(), FILE_NAME);

        try {
            ImageUtils.saveImageToSD(MyQrcodeActivity.this, file.getAbsolutePath(), bitmap, 100);
//            Uri uri = FileProvider.getUriForFile(MyQrcodeActivity.this, "com.michel
// .pointscredit", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始调用 系统的分享
     *
     * @param
     */
    @AfterPermissionGranted(READ_EXTERNAL)
    private void startShare() {
        String imagePath = DIR + File.separator + FILE_NAME;

        //由文件得到uri
        File filePath = new File(imagePath);
        Uri imageUri /*= Uri.fromFile(filePath)*/;

        Intent shareIntent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            imageUri = FileProvider.getUriForFile(mContext, "com.michel.pointscredit.fileprovider",
                    filePath);
            shareIntent.setDataAndType(imageUri, "application/vnd.android.package-archive");
        } else {
            imageUri = Uri.fromFile(filePath);
            shareIntent.setDataAndType(imageUri, "application/vnd.android.package-archive");
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_qrcode)));

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (perms != null && perms.size() > 0) {
            saveBitmap();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}

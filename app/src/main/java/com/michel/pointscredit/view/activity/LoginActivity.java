package com.michel.pointscredit.view.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.michel.pointscredit.R;
import com.michel.pointscredit.base.PCBaseActivity;
import com.michel.pointscredit.bean.User;
import com.michel.pointscredit.utils.RouterUtils;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import org.jetbrains.annotations.Nullable;

import butterknife.BindView;

/**
 * Created by 80010651 on 2018/3/13.
 * 登录界面
 */

public class LoginActivity extends PCBaseActivity {

    private String mEmail;
    @BindView(R.id.et_login_email)
    EditText mEtEmail;
    @BindView(R.id.et_login_pwd)
    EditText mEtPwd;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.btn_login_reg)
    Button mBtnReg;
    @BindView(R.id.tv_login_forget)
    TextView mTvForget;

    private ProgressDialog mProgressDialog;

    public static void startLoginActy(@NonNull Context context, @NonNull String email) {
        Intent loginIntent = new Intent(context, LoginActivity.class);
        loginIntent.putExtra("email", email);
        context.startActivity(loginIntent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewsOnClickListener(mBtnLogin, mBtnReg,mTvForget);
        mProgressDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        if (intent != null) {
            mEmail = intent.getStringExtra("email");
        }
        if (!TextUtils.isEmpty(mEmail)) {
            mEtEmail.setText(mEmail);
        }
    }

    @Override
    public void onClick(@Nullable View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                mProgressDialog.setMessage("登录中，请稍候……");
                mProgressDialog.show();
                User.logInInBackground(mEtEmail.getText().toString(), mEtPwd.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        mProgressDialog.dismiss();
//                        if (user != null) {
//                            Log.e("pc", "login:" + user.toString());
//                        }
//                        if (e != null) {
//                            Log.e("pc", e.getMessage());
//                            Toast.makeText(LoginActivity.this, "登录失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//                            Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
//                            startActivity(homeIntent);
//                            finish();
//                        }

                        test();
                    }
                });
                break;
            case R.id.tv_login_forget:
                showEmailDialog();
                break;

            case R.id.btn_login_reg:
                RouterUtils.jump2Target(this,RegisterActivity.class);
                break;
        }
    }

    private void showEmailDialog() {
        final EditText etEmail = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.InputEmail))
                .setView(etEmail)
                .setPositiveButton(getResources().getString(R.string.Confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        //确认
                        String strEmail = etEmail.getText().toString();
                        if (TextUtils.isEmpty(strEmail)) {
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.InputEmail), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        User.requestPasswordResetInBackground(strEmail, new RequestPasswordResetCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.CheckMailbox), Toast.LENGTH_SHORT).show();
                                    dialogInterface.dismiss();
                                }
                            }
                        });

                    }
                })
                .setNegativeButton(getResources().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        //取消
                        if (dialog != null) dialog.dismiss();
                    }
                }).show();
    }

    private void test() {
        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
        Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(homeIntent);
        finish();
    }
}

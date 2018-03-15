package com.michel.pointscredit.view.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.michel.pointscredit.R;
import com.michel.pointscredit.base.PCBaseActivity;
import com.michel.pointscredit.bean.User;
import com.michel.pointscredit.utils.RouterUtils;
import com.michel.pointscredit.view.widget.PCCommonTitleLayout;
import com.michel.pointscredit.view.widget.SimplexToast;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

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
    @BindView(R.id.login_title)
    PCCommonTitleLayout mTitleBar;

    private QMUIDialog mInfoDialog;
    private QMUITipDialog mProgressDialog;

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
        setViewsOnClickListener(mBtnLogin, mBtnReg, mTvForget, mTitleBar.getLeftBackView());
        mProgressDialog =
                new QMUITipDialog.Builder(this)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                        .setTipWord("登录中，请稍候……")
                        .create();

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
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPwd.getText().toString();
                String tips = "";
                if (TextUtils.isEmpty(strEmail)) {
                    tips = getResources().getString(R.string.InputEmail);
                    mInfoDialog = new QMUIDialog.MessageDialogBuilder(this)
                            .setMessage(tips)
                            .addAction(getResources().getString(R.string.Confirm), new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                }
                            }).show();
                    return;
                }
                if (TextUtils.isEmpty(strPwd)) {
                    tips = getResources().getString(R.string.InputPWD);
                    mInfoDialog = new QMUIDialog.MessageDialogBuilder(this)
                            .setMessage(tips)
                            .addAction(getResources().getString(R.string.Confirm), new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                }
                            }).show();
                    return;
                }

                if (!TextUtils.isEmpty(strEmail) && !TextUtils.isEmpty(strPwd)) {
                    mProgressDialog.show();
                    User.logInInBackground(mEtEmail.getText().toString(), mEtPwd.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            mProgressDialog.dismiss();
                            if (user != null) {
                                Log.e("pc", "login:" + user.toString());
                            }
                            if (e != null) {
                                Log.e("pc", e.getMessage());
                                SimplexToast.show(LoginActivity.this, e.getMessage());
                            } else {
                                SimplexToast.show(LoginActivity.this, getResources().getString(R.string.Succeeded));
                                Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(homeIntent);
                                finish();
                            }
                        }
                    });
                }

                break;
            case R.id.tv_login_forget:
                showEmailDialog();
                break;

            case R.id.btn_login_reg:
                RouterUtils.jump2Target(this, RegisterActivity.class);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void showEmailDialog() {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(this);
        builder.setTitle(getResources().getString(R.string.InputEmail))
                .setPlaceholder(getResources().getString(R.string.Email))
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction(getResources().getString(R.string.Cancel), new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(getResources().getString(R.string.Confirm), new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(final QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            User.requestPasswordResetInBackground(text.toString(), new RequestPasswordResetCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        SimplexToast.show(LoginActivity.this, getResources().getString(R.string.CheckMailbox));
                                        dialog.dismiss();
                                    }
                                }
                            });

                        } else {
                            SimplexToast.show(LoginActivity.this, getResources().getString(R.string.InputEmail));
                        }
                    }
                })
                .show();
    }
}

package com.michel.pointscredit.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.michel.pointscredit.R;
import com.michel.pointscredit.base.PCBaseActivity;
import com.michel.pointscredit.bean.User;
import com.michel.pointscredit.utils.PCStringUtils;
import com.michel.pointscredit.view.widget.PCCommonTitleLayout;
import com.michel.pointscredit.view.widget.PCRegEditText;
import com.michel.pointscredit.view.widget.SimplexToast;
import com.parse.ParseException;
import com.parse.SignUpCallback;

import org.jetbrains.annotations.Nullable;

import butterknife.BindView;

/**
 * Created by 80010651 on 2018/3/13.
 * 登录界面
 */

public class RegisterActivity extends PCBaseActivity {

    @BindView(R.id.et_reg_email)
    PCRegEditText mEmailLayout;
    EditText mEtEmail;
    @BindView(R.id.et_reg_pwd)
    PCRegEditText mPwdLayout;
    EditText mEtPwd;
    @BindView(R.id.et_reg_phone)
    PCRegEditText mPhoneLayout;
    EditText mEtPhone;
    @BindView(R.id.et_reg_firstname)
    PCRegEditText mFirstNameLayout;
    EditText mEtFirstName;
    @BindView(R.id.et_reg_lastname)
    PCRegEditText mLastNameLayout;
    EditText mEtLastName;
    @BindView(R.id.cb_reg_agree)
    CheckBox mCBAgree;
    @BindView(R.id.btn_reg)
    Button mBtnReg;
    @BindView(R.id.reg_title)
    PCCommonTitleLayout mTitleBar;

    private User mUser;

    @Override
    public int getLayoutId() {
        return R.layout.activity_reg2;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEtEmail = mEmailLayout.getEdiText();
        mEtEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        mEtPwd = mPwdLayout.getEdiText();
        mEtPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mEtPhone = mPhoneLayout.getEdiText();
        mEtPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        mEtFirstName = mFirstNameLayout.getEdiText();
        mEtLastName = mLastNameLayout.getEdiText();
        setViewsOnClickListener(mCBAgree, mBtnReg, mTitleBar.getLeftBackView());
        mUser = new User();
    }

    @Override
    public void onContentChanged() {
    }

    @Override
    public void onClick(@Nullable View view) {
        switch (view.getId()) {
            case R.id.btn_reg:
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPwd.getText().toString();
                String strPhone = mEtPhone.getText().toString();
                String strFirstName = mEtFirstName.getText().toString();
                String strLastName = mEtLastName.getText().toString();

                String tips = checkSth(strEmail, strPwd, strPhone, strFirstName, strLastName);
                boolean isAgree = mCBAgree.isChecked();
                if (!TextUtils.isEmpty(tips)) {
                    SimplexToast.show(mContext, tips);
                    return;
                }

                if (!isAgree){
                    SimplexToast.show(mContext,getResString(R.string.agree_please));
                    return;
                }
                showLoading();
                mUser.setContactEmail(mEtEmail.getText().toString());
                mUser.setEmail(mEtEmail.getText().toString());
                mUser.setFirstName(mEtFirstName.getText().toString());
                mUser.setPhone(mEtPhone.getText().toString());
                mUser.setLastName(mEtLastName.getText().toString());
                mUser.setPassword(mEtPwd.getText().toString());
                mUser.setUsername(mEtEmail.getText().toString());//userName就是email
                mUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        dismissLoading();
                        if (e != null) {
                            SimplexToast.show(RegisterActivity.this, e.getMessage());
                            Log.e("PC:reg", e.getMessage());
                        } else {
                            SimplexToast.show(RegisterActivity.this, getResources().getString(R
                                    .string.reg_success));
                            Intent homeIntent = new Intent(RegisterActivity.this, HomeActivity
                                    .class);
                            startActivity(homeIntent);
                            finish();
                        }
                    }
                });

            case R.id.iv_back:
                finish();
                break;
        }
    }

    private String checkSth(String strEmail, String strPwd, String strPhone, String strFirstName,
                            String strLastName) {
        String tips = "";
        if (TextUtils.isEmpty(strEmail)) {
            tips = getResString(R.string.InputEmail);
        } else if (TextUtils.isEmpty(strPwd)) {
            if (!PCStringUtils.isEmail(strEmail)) {
                tips = getResString(R.string.InputValidEmail);
            } else tips = getResString(R.string.InputPWD);
        } else if (TextUtils.isEmpty(strPhone)) {
            if (!PCStringUtils.isRightPWDForm(strPwd)) {
                tips = getResString(R.string.PWDForm);
            } else tips = getResString(R.string.InputPhone);
        } else if (TextUtils.isEmpty(strFirstName)) {
            tips = getResString(R.string.InputFirstName);
        } else if (TextUtils.isEmpty(strLastName)) {
            tips = getResString(R.string.InputLastName);
        }

        return tips;
    }
}

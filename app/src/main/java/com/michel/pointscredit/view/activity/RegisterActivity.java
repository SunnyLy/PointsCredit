package com.michel.pointscredit.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.michel.pointscredit.R;
import com.michel.pointscredit.base.PCBaseActivity;
import com.michel.pointscredit.bean.User;
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
    EditText mEtEmail;
    @BindView(R.id.et_reg_pwd)
    EditText mEtPwd;
    @BindView(R.id.et_reg_phone)
    EditText mEtPhone;
    @BindView(R.id.et_reg_firstname)
    EditText mEtFirstName;
    @BindView(R.id.et_reg_lastname)
    EditText mEtLastName;
    @BindView(R.id.cb_reg_agree)
    CheckBox mCBAgree;
    @BindView(R.id.btn_reg)
    Button mBtnReg;

    //        private ParseObject mUser;
    private User mUser;

    private ProgressDialog mPD;

    @Override
    public int getLayoutId() {
        return R.layout.activity_reg;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewsOnClickListener(mCBAgree, mBtnReg);
        mUser = new User();
        mPD = new ProgressDialog(this);
    }

    @Override
    public void onClick(@Nullable View view) {
        switch (view.getId()) {
            case R.id.btn_reg:
                mPD.setMessage("注册中，请稍候……");
                mPD.show();
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
                        mPD.dismiss();
                        if (e != null) {
                            Toast.makeText(RegisterActivity.this, "code:" + e.getCause() + "\ntrace:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("PC:reg", e.getMessage());
                        } else {
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            Intent homeIntent = new Intent(RegisterActivity.this, HomeActivity.class);
                            startActivity(homeIntent);
                            finish();
                        }
                    }
                });

//                mUser.put(User.CONTACT_EMAIL, mEtEmail.getText().toString());
//                mUser.put(User.PASSWORD, mEtPwd.getText().toString());
//                mUser.put(User.PHONE, mEtPhone.getText().toString());
//                mUser.put(User.FIRST_NAME, mEtFirstName.getText().toString());
//                mUser.put(User.LAST_NAME, mEtLastName.getText().toString());
//                mUser.saveInBackground(new SaveCallback() {
//                    @Override
//                    public void done(ParseException e) {
//                        mPD.dismiss();
//                        if (e != null) {
//                            Toast.makeText(RegisterActivity.this, "code:"+e.getCause()+"\ntrace:"+e.getMessage(), Toast.LENGTH_SHORT).show();
//                            Log.e("PC:reg", e.getMessage());
//                        }
//                    }
//                });
//                break;
        }
    }
}

package com.michel.pointscredit.view.activity;

import android.content.Intent;
import android.os.Bundle;

import com.michel.pointscredit.R;
import com.michel.pointscredit.base.PCBaseActivity;
import com.parse.ParseUser;

import org.jetbrains.annotations.Nullable;

/**
 * Created by 80010651 on 2018/3/14.
 */

public class PCSplashActivity extends PCBaseActivity {

    private ParseUser mUser;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = ParseUser.getCurrentUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mUser != null) {
            //已经登录过，
            String email = mUser.getEmail();
            mUser.logOut();
            LoginActivity.startLoginActy(this, email);
        } else {
            Intent intent = new Intent(PCSplashActivity.this, WelcomeActivity.class);
            PCSplashActivity.this.startActivity(intent);
        }
        finish();
    }
}

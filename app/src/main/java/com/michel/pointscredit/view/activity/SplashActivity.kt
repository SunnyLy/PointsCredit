package com.michel.pointscredit.view.activity

import android.os.Bundle
import android.os.Handler
import com.michel.pointscredit.R
import com.michel.pointscredit.base.PCBaseActivity

/**
 * Created by 80010651 on 2018/3/13.
 * 闪屏界面
 */
class SplashActivity : PCBaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed(Runnable {
            MainActivity.startMainActivity(this)
            finish()
        }, 1000)
    }
}
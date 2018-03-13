package com.michel.pointscredit.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import butterknife.ButterKnife
import butterknife.Unbinder
import android.view.WindowManager


/**
 * Created by 80010651 on 2018/3/13.
 * Base界面：
 * 这里主要是提供一些模板操作
 */
abstract class PCBaseActivity : AppCompatActivity() {

    var unbinder: Unbinder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(getLayoutId())
        unbinder = ButterKnife.bind(this)
        val window = window
//        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
//        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.statusBarColor = Color.TRANSPARENT
//            window.navigationBarColor = Color.TRANSPARENT
//        }
    }

    abstract fun getLayoutId(): Int
}
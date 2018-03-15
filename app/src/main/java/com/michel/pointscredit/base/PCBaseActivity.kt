package com.michel.pointscredit.base

import android.content.Context
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
import com.michel.pointscredit.R
import com.michel.pointscredit.utils.MatrialStatusBarUtils
import com.qmuiteam.qmui.util.QMUIStatusBarHelper


/**
 * Created by 80010651 on 2018/3/13.
 * Base界面：
 * 这里主要是提供一些模板操作
 */
abstract class PCBaseActivity : AppCompatActivity(), View.OnClickListener {

    var unbinder: Unbinder? = null
    var mContext: Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        QMUIStatusBarHelper.translucent(this) // 沉浸式状态栏
        //当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //透明导航栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }

        mContext = this
        setContentView(getLayoutId())
        unbinder = ButterKnife.bind(this)
        initParams()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * 初始化参数
     */
    fun initParams() {

    }

    abstract fun getLayoutId(): Int

    /**
     * 設置點擊監聽
     */
    fun setViewsOnClickListener(vararg views: View) {
        if (views.isNotEmpty()) {
            for (view in views) {
                view.setOnClickListener(this)
            }
        }
    }

    override fun onClick(p0: View?) {

    }
}
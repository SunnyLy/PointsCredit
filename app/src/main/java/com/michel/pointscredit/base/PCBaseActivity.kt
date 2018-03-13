package com.michel.pointscredit.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by 80010651 on 2018/3/13.
 */
abstract class PCBaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
    }

    abstract fun getLayoutId(): Int
}
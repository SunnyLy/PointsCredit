package com.michel.pointscredit.view.activity

import android.content.Context
import android.content.Intent
import android.widget.TextView
import butterknife.BindView
import com.michel.pointscredit.R
import com.michel.pointscredit.base.PCBaseActivity

/**
 * Created by Sunny on 2018/3/13.
 * 主界面：
 * 1，如果用户首次进入或未曾登录过，则每次打开App，跳转至此界面
 * 2，如果用户已经登录过，则每次打开App时，先调接口清除登录信息，再跳转至登录界面
 */
class MainActivity : PCBaseActivity() {

    companion object {
        fun startMainActivity(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    @BindView(R.id.tv_info) lateinit var mTvInfo: TextView
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onResume() {
        super.onResume()
        mTvInfo.text = this.getString(R.string.Do_you_still_go_to_ATM)
    }

}

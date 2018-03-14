package com.michel.pointscredit

import android.app.Application
import com.michel.pointscredit.bean.Transaction
import com.michel.pointscredit.bean.User
import com.parse.Parse
import com.parse.ParseObject

/**
 * Created by 80010651 on 2018/3/13.
 */
class PCApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ParseObject.registerSubclass(User::class.java)
        ParseObject.registerSubclass(Transaction::class.java)
        Parse.initialize(Parse.Configuration.Builder(this)
                .applicationId("POINTS_CREDIT_PARSE_APP_ID")
                .server("https://points-credit.herokuapp.com/parse")
                .build()
        )
    }
}
package com.michel.pointscredit;

import android.app.Application;

import com.michel.pointscredit.bean.Transaction;
import com.michel.pointscredit.bean.User;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;

/**
 * @Annotation <p>描述</p>
 * @Auth Sunny
 * @date 2018/3/20
 * @Version V1.0.0
 */

public class PCApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Transaction.class);
        ParseObject.registerSubclass(User.class);
        Parse.initialize(new Parse.Configuration.Builder(this).applicationId("POINTS_CREDIT_PARSE_APP_ID")
                .server("https://points-credit.herokuapp.com/parse")
                .build());

        //注册当前设备至Parse消息推送
        ParseInstallation.getCurrentInstallation().saveInBackground();

    }
}

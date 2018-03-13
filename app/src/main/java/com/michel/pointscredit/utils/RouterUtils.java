package com.michel.pointscredit.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by 80010651 on 2018/3/13.
 * 页面路由跳转工具类
 */

public class RouterUtils {

    public static void jump2Target(Context context, @NonNull Class<?> target){
        Intent intent = new Intent(context,target.getClass());
        context.startActivity(intent);
    }
}

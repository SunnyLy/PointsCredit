package com.michel.pointscredit.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * Created by 80010651 on 2018/3/13.
 * 页面路由跳转工具类
 */

public class RouterUtils {

    public static void jump2Target(Context context, @NonNull Class<?> target) {
        Intent intent = new Intent(context, target);
        context.startActivity(intent);
    }

    public static void jump2TargetWithBundle(Context context, @NonNull Class<?> target, Bundle bundle) {
        Intent intent = new Intent(context, target);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void jump2TargetForResult(Activity context, @NonNull Class<?> target, int requestCode) {
        Intent intent = new Intent(context, target);
        context.startActivityForResult(intent, requestCode);
    }
}

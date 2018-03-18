package com.michel.pointscredit.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.michel.pointscredit.utils.MatrialStatusBarUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Annotation <p>描述</p>
 * @Auth Sunny
 * @date 2018/3/16
 * @Version V1.0.0
 */

public abstract class PCBaseActivity extends AppCompatActivity implements View.OnClickListener {

    public Context mContext;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = this;
        //沉浸式
        setContentView(getLayoutId());
        MatrialStatusBarUtils.compat(this);
        unbinder = ButterKnife.bind(this);
        initParams();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    public void initParams() {

    }

    protected abstract int getLayoutId();

    /**
     * 設置點擊監聽
     */
    public void setViewsOnClickListener(View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                view.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View view) {

    }
}

package com.michel.pointscredit.view.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.michel.pointscredit.R;

/**
 * @Annotation <p>描述</p>
 * @Auth Sunny
 * @date 2018/3/14
 * @Version V1.0.0
 */

public class PCCommonTitleLayout extends LinearLayout {
    public PCCommonTitleLayout(Context context) {
        this(context,null);
    }

    public PCCommonTitleLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public PCCommonTitleLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        View contentView = LayoutInflater.from(context).inflate(R.layout.widget_comm_title,null);
        removeAllViews();
        addView(contentView);
    }
}

package com.michel.pointscredit.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.michel.pointscredit.R;

/**
 * Created by 80010651 on 2018/3/16.
 * 註冊界面
 */

public class PCRegEditText extends RelativeLayout {

    private EditText mEdiText;
    private ImageView mETIcon;

    private Drawable mResIcon;
    private String mHint;
    private int mInputType = -1;
    private float mETTextSize = 13;
    private int mETTextColor;

    public EditText getEdiText() {
        return mEdiText;
    }

    public ImageView getIconView() {
        return mETIcon;
    }

    public PCRegEditText(Context context) {
        this(context, null);
    }

    public PCRegEditText(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public PCRegEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PCEditText);
        mResIcon = array.getDrawable(R.styleable.PCEditText_et_icon_src);
        mHint = array.getString(R.styleable.PCEditText_et_hint);
        mETTextColor = array.getColor(R.styleable.PCEditText_et_text_color, context.getResources().getColor(R.color.colorPrimary));
        mETTextSize = array.getFloat(R.styleable.PCEditText_et_text_size, 13);
        View contentView = LayoutInflater.from(context).inflate(R.layout.widget_reg_edittext, null);
        initView(contentView);
        removeAllViews();
        addView(contentView);
        array.recycle();
    }

    private void initView(View view) {
        mEdiText = view.findViewById(R.id.widget_reg_et_content);
        mETIcon = view.findViewById(R.id.widget_reg_et_ic);

        if (mResIcon != null) {
            mETIcon.setImageDrawable(mResIcon);
        }
        if (!TextUtils.isEmpty(mHint)) {
            mEdiText.setHint(mHint);
        }
        mEdiText.setTextSize(TypedValue.COMPLEX_UNIT_SP, mETTextSize);
        mEdiText.setTextColor(mETTextColor);
    }
}

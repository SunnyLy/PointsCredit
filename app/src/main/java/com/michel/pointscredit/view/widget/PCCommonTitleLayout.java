package com.michel.pointscredit.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.michel.pointscredit.R;

/**
 * @Annotation <p>描述</p>
 * @Auth Sunny
 * @date 2018/3/14
 * @Version V1.0.0
 */

public class PCCommonTitleLayout extends LinearLayout {

    private ImageView mIvBack;//返回按钮
    private TextView mTvTitle;//标题
    private ImageView mIvRight;//标题栏右侧按钮

    private Context mContext;

    private String mStrTitle;
    private boolean mRightIconVisible;
    private Drawable mRightIconDrawable;

    public ImageView getLeftBackView() {
        return mIvBack;
    }

    public TextView getTitleView() {
        return mTvTitle;
    }

    public ImageView getRightView() {
        return mIvRight;
    }

    public PCCommonTitleLayout(Context context) {
        this(context, null);
    }

    public PCCommonTitleLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public PCCommonTitleLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setOrientation(HORIZONTAL);
        View contentView = LayoutInflater.from(context).inflate(R.layout.widget_comm_title, null);
        initView(contentView, attrs, defStyleAttr);
        removeAllViews();
        addView(contentView);
    }

    private void initView(View view, AttributeSet attrs, int defStyleAttr) {
        mIvBack = view.findViewById(R.id.iv_back);
        mTvTitle = view.findViewById(R.id.title_layout_title);
        mIvRight = view.findViewById(R.id.title_layout_share);
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.PCCommonTitleLayout);
        mStrTitle = typedArray.getString(R.styleable.PCCommonTitleLayout_title_content);
        mRightIconDrawable = typedArray.getDrawable(R.styleable.PCCommonTitleLayout_right_icon_src);
        mRightIconVisible = typedArray.getBoolean(R.styleable.PCCommonTitleLayout_right_icon_visible, false);
        if (!TextUtils.isEmpty(mStrTitle)) {
            mTvTitle.setText(mStrTitle);
        }
        mIvRight.setVisibility(mRightIconVisible ? VISIBLE : INVISIBLE);
        mIvRight.setImageDrawable(mRightIconDrawable);
    }

}

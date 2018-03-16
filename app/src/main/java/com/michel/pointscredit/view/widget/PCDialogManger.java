package com.michel.pointscredit.view.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.michel.pointscredit.R;
import com.michel.pointscredit.callback.IPositiveClickListener;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

/**
 * Created by 80010651 on 2018/3/16.
 * 消息通知管理类
 */

public class PCDialogManger {

    //1.普通消息通知
    public static void showCommonDialog(@NonNull final Context context, String title, String msg,
                                        QMUIDialogAction.ActionListener cancelListener,
                                        QMUIDialogAction.ActionListener positiveListener) {
        QMUIDialog.MessageDialogBuilder builder = new QMUIDialog.MessageDialogBuilder(context);
        builder.setTitle(title)
                .setMessage(msg);
        if (cancelListener != null) {
            builder.addAction("取消", cancelListener);
        }
        if (positiveListener != null) {
            builder.addAction("确定", positiveListener);
        }
        getBaseDialogLayout(builder);
        QMUIDialog dialog = builder.create();
        dialog.show();
    }

    //1.可编辑消息的通知
    public static void showEditableDialog(@NonNull final Context context, String title, String hint,
                                          QMUIDialogAction.ActionListener cancelListener,
                                          final IPositiveClickListener clickListener) {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(context);
        builder.setTitle(title)
                .setPlaceholder(hint)
                .setInputType(InputType.TYPE_CLASS_TEXT);
        if (cancelListener != null) {
            builder.addAction("取消", cancelListener);
        }
        if (clickListener != null) {
            builder.addAction("确定", new QMUIDialogAction.ActionListener() {
                @Override
                public void onClick(QMUIDialog qmuiDialog, int i) {
                    String msg = builder.getEditText().getText() == null ? "" : builder.getEditText().getText().toString();
                    clickListener.onPositiveClick(msg, qmuiDialog);
                }
            });
        }
        QMUIDialog dialog = builder.create();
        getEditDialogTitleLayout(builder);
        dialog.show();
    }

    /**
     * 获取基本布局
     *
     * @param builder
     */
    private static void getBaseDialogLayout(QMUIDialog.MessageDialogBuilder builder) {
        //修改title的布局
        TextView titleView = builder.getTitleView();
        if (titleView != null) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) titleView.getLayoutParams();
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            titleView.setLayoutParams(lp);
            lp.gravity = Gravity.CENTER;
            titleView.setGravity(Gravity.CENTER);
        }
        //修改内容布局
        TextView contentView = builder.getTextView();
        if (contentView != null) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) contentView.getLayoutParams();
            contentView.setLayoutParams(lp);
            lp.gravity = Gravity.CENTER;
            contentView.setGravity(Gravity.CENTER);
        }
    }

    /**
     * 获取基本布局
     *
     * @param builder
     */
    private static void getEditDialogTitleLayout(QMUIDialog.EditTextDialogBuilder builder) {
        //修改title的布局
        TextView titleView = builder.getTitleView();
        if (titleView != null) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) titleView.getLayoutParams();
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            titleView.setLayoutParams(lp);
            lp.gravity = Gravity.CENTER;
            titleView.setGravity(Gravity.CENTER);
        }
    }

    /**
     * 2.无标题，只有内容+ 确定键
     */
    public static void showContentPositiveDialog(Context context) {

    }
}

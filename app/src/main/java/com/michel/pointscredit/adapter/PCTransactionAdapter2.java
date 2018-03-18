package com.michel.pointscredit.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.michel.pointscredit.R;
import com.michel.pointscredit.bean.TrascationItemBean;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @Annotation <p>账单明细适配器</p>
 * @Auth Sunny
 * @date 2018/3/18
 * @Version V1.0.0
 */

public class PCTransactionAdapter2 extends RecyclerView.Adapter<PCTransactionAdapter2.PCTransactionViewHolder> {

    private ArrayList<TrascationItemBean> parseObjects = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mLayoutInfalter;


    public PCTransactionAdapter2(Context context, ArrayList<TrascationItemBean> parseObjects) {
        this.mContext = context;
        if (parseObjects != null && parseObjects.size() > 0){
            this.parseObjects.clear();
            this.parseObjects.addAll(parseObjects);
        }
        mLayoutInfalter = LayoutInflater.from(context);
    }

    public void setParseObjects(ArrayList<TrascationItemBean> parseObjects) {
        if (parseObjects != null && parseObjects.size() > 0){
            this.parseObjects.clear();
            this.parseObjects.addAll(parseObjects);
        }
        notifyDataSetChanged();
    }

    @Override
    public PCTransactionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
       View contentView = mLayoutInfalter.inflate(R.layout.item_tansaction,viewGroup,false);
       contentView.setTag(i);
       PCTransactionViewHolder viewHolder = new PCTransactionViewHolder(contentView);
        return viewHolder;
    }

    /**
     * 注意：这里有个fetchIfNeeded()的用法：
     * 当需要把一个ParseObject向下转型为需要的类型是，
     * 就用这个方法：
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final PCTransactionViewHolder holder, final int position) {

        TrascationItemBean itemBean = parseObjects.get(position);
        if (itemBean == null)return;
        String userName = itemBean.getUserName();
        boolean isOut = itemBean.isOut();
        String  date = itemBean.getUpdateTime();
        String money = itemBean.getSum();
                    holder.mTvName.setText(userName);
                    holder.mAmount.setText(isOut?"-"+money:money);
                    holder.mAmount.setTextColor(isOut?mContext.getResources().getColor(R.color.colorPrimary): Color.RED);
                    if (!TextUtils.isEmpty(date))
                        holder.mTvDate.setText(date);

    }

    @Override
    public int getItemCount() {
        return parseObjects.size();
    }

    public class PCTransactionViewHolder extends RecyclerView.ViewHolder{

        private TextView mTvName;
        private TextView mTvDate;
        private TextView mAmount;

        public PCTransactionViewHolder(View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.widget_transaction_name);
            mTvDate = itemView.findViewById(R.id.widget_transaction_date);
            mAmount = itemView.findViewById(R.id.widget_transaction_amount);
        }
    }
}

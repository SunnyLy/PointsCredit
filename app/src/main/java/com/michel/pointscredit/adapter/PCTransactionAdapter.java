package com.michel.pointscredit.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.michel.pointscredit.R;
import com.michel.pointscredit.bean.User;
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

public class PCTransactionAdapter extends RecyclerView.Adapter<PCTransactionAdapter.PCTransactionViewHolder> {

    private ArrayList<ParseObject> parseObjects = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mLayoutInfalter;


    public PCTransactionAdapter(Context context,ArrayList<ParseObject> parseObjects) {
        this.mContext = context;
        if (parseObjects != null && parseObjects.size() > 0){
            this.parseObjects.clear();
            this.parseObjects.addAll(parseObjects);
        }
        mLayoutInfalter = LayoutInflater.from(context);
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

        parseObjects.get(position).fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (object != null){
                    PCTransactionViewHolder viewHolder = holder;
                    ParseUser fromUser = object.getParseUser("from");
                    ParseUser toUser = object.getParseUser("to");
                    String fromUserId = fromUser.getObjectId();
                    String toUserId = toUser.getObjectId();

                    String userName = "";
                    String amount;
                    boolean isOut = false;//是不是轉出去
                    if (fromUserId.equals(ParseUser.getCurrentUser().getObjectId())){
                        //我轉出去的
                        try {
                            userName = toUser.fetchIfNeeded().getString("firstName");
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        isOut = true;
                    }else{
                        //別人轉給我的
                        try {
                            userName = fromUser.fetchIfNeeded().getString("firstName");
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        isOut = false;
                    }
                    holder.mTvName.setText(userName);
                    Double dbMoney = parseObjects.get(position).getDouble("amount");
                    DecimalFormat df = new DecimalFormat("#.000");
                    String  money = "€"+df.format(dbMoney);
                    holder.mAmount.setText(isOut?"-"+money:money);
                    holder.mAmount.setTextColor(isOut?mContext.getResources().getColor(R.color.colorPrimary): Color.RED);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    Date date = parseObjects.get(position).getUpdatedAt();
                    if (date != null)
                        holder.mTvDate.setText(sdf.format(date));
                }
            }
        });

//        ParseObject fromObj =  parseObjects.get(position).getParseObject("from");
//        ParseObject toObj = parseObjects.get(position).getParseObject("to");
//        try {
//            ParseUser fromUser = parseObjects.get(position).fetchIfNeeded().getParseUser("from");
//            ParseUser toUser = parseObjects.get(position).fetchIfNeeded().getParseUser("to");
//
//            String formObjectId = fromUser.getObjectId();
//            String toObjectId = toUser.getObjectId();
//            String currentObjectId = ParseUser.getCurrentUser().getObjectId();
//
//            String userName = "";
//            String amount;
//            boolean isOut = false;//是不是轉出去
//            if (formObjectId.equals(currentObjectId)){
//                //我轉出去的
//                userName = toObj.fetchIfNeeded().getString("firstName");
//                isOut = true;
//            }else{
//                //別人轉給我的
//                userName = fromObj.fetchIfNeeded().getString("firstName");
//                isOut = false;
//            }
//            holder.mTvName.setText(userName);
//            Double dbMoney = parseObjects.get(position).getDouble("amount");
//            DecimalFormat df = new DecimalFormat("#.000");
//            String  money = "€"+df.format(dbMoney);
//            holder.mAmount.setText(isOut?"-"+money:money);
//            holder.mAmount.setTextColor(isOut?mContext.getResources().getColor(R.color.colorPrimary): Color.RED);
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
//            Date date = parseObjects.get(position).getUpdatedAt();
//            if (date != null)
//                holder.mTvDate.setText(sdf.format(date));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

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

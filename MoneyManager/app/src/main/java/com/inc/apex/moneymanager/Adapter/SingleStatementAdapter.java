package com.inc.apex.moneymanager.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.inc.apex.moneymanager.Object.ActivityInforMoney;
import com.inc.apex.moneymanager.R;
import com.inc.apex.moneymanager.Unit.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dat.Pham on 8/16/2017.
 */

public class SingleStatementAdapter extends ArrayAdapter<ActivityInforMoney> {
    private Context myContext;
    private int layoutId;
    private List<ActivityInforMoney> list = new ArrayList<ActivityInforMoney>();

    public SingleStatementAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ActivityInforMoney> objects){
        super(context,resource,objects);

        this.myContext = context;
        this.layoutId = resource;
        this.list = objects;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater in = ((Activity)myContext).getLayoutInflater();
        convertView = in.inflate(layoutId,null);

        TextView tvAccount = (TextView)convertView.findViewById(R.id.tv_single_list_account);
        TextView tvContent = (TextView)convertView.findViewById(R.id.tv_single_list_contents);
        TextView tvAmount = (TextView)convertView.findViewById(R.id.tv_single_list_amount);
        TextView tvCategory = (TextView)convertView.findViewById(R.id.tv_single_list_category);

        tvAccount.setText(list.get(position).getAccount());
        tvContent.setText(list.get(position).getContents());
        tvCategory.setText(list.get(position).getCategory());

        int type = list.get(position).getType();

        if(type == 1){
            tvAmount.setText("+"+list.get(position).getAmount());
            tvAmount.setTextColor(Color.parseColor(Constant.COLOR_LIMEGREEN));

        }else{
            tvAmount.setText("-"+list.get(position).getAmount());
            tvAmount.setTextColor(Color.parseColor(Constant.COLOR_RED));

        }
        return convertView;
    }

    @Override
    public int getPosition(@Nullable ActivityInforMoney item) {
        return super.getPosition(item);
    }

    @Nullable
    @Override
    public ActivityInforMoney getItem(int position) {
        return list.get(position);
    }
}

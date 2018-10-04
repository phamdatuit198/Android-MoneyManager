package com.inc.apex.moneymanager.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.inc.apex.moneymanager.Object.Account;
import com.inc.apex.moneymanager.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dat.Pham on 10/9/2017.
 */

public class AccountAdapter extends ArrayAdapter<Account>{

    private Context         mContext;
    private int             layoutID;
    private List<Account>   list = new ArrayList<Account>();

    public AccountAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Account> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.layoutID = resource;
        this.list     = objects;
    }



    @Override
    public int getCount() {
        return list.size();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater in = ((Activity)mContext).getLayoutInflater();
        convertView       = in.inflate(layoutID,null);

        TextView tvAccount = (TextView)convertView.findViewById(R.id.tv_account_item);
        tvAccount.setText(list.get(position).getNameBank().toString());
        return convertView;
    }


    @Override
    public int getPosition(@Nullable Account item) {
        return super.getPosition(item);
    }


    @Nullable
    @Override
    public Account getItem(int position) {
        return list.get(position);
    }
}

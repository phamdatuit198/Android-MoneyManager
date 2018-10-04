package com.inc.apex.moneymanager.AsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.inc.apex.moneymanager.Object.ActivityInforMoney;
import com.inc.apex.moneymanager.Unit.DatabaseHelper;

/**
 * Created by Dat.Pham on 8/16/2017.
 */

public class AsyncTaskHomeDelete extends AsyncTask<Void, Boolean, Boolean> {

    private ProgressDialog mProgressDialog;
    private Context mContext;
    private ActivityInforMoney mActivityInforMoney;
    private final DatabaseHelper data = DatabaseHelper.getInstance(mContext);
    private boolean result = false;
    public AsyncTaskHomeDelete(Context context, ActivityInforMoney infor){

        this.mContext = context;
        this.mActivityInforMoney = infor;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = ProgressDialog.show(mContext,"Deleting data","Please Wait",false,false);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return data.deleteStatement(mActivityInforMoney);

    }

    @Override
    protected void onPostExecute(Boolean aVoid) {
        super.onPostExecute(aVoid);

        mProgressDialog.dismiss();
        if(aVoid == true)
            Toast.makeText(mContext,"Delete successful!",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(mContext,"Delete fail!",Toast.LENGTH_LONG).show();



    }


}


package com.inc.apex.moneymanager.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.inc.apex.moneymanager.Camera.TessEngine;
import com.inc.apex.moneymanager.Camera.Tools;
import com.inc.apex.moneymanager.Dialog.DetectDialog;
import com.inc.apex.moneymanager.Unit.Constant;

/**
 * Created by Dat.Pham on 9/28/2017.
 */

public class AsyncTaskEngine extends AsyncTask<Object, Void, String> {

    private ProgressDialog mProgressDialog;
    private Activity mContext;
    private Bitmap bitmap;

    public AsyncTaskEngine(Activity context, Bitmap bmp){
        mContext = context;
        bitmap   = bmp;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = ProgressDialog.show(mContext,"Deleting data","Please Wait",false,false);
    }

    @Override
    protected String doInBackground(Object... params) {

        try{

            if(!(mContext instanceof Activity) || !(bitmap instanceof Bitmap)){
                Log.e(Constant.TAG,"Error passing parameter to execute(context,bitmap)");
                return null;
            }


            if(mContext == null || bitmap == null){
                Log.e(Constant.TAG,"Error passed null parameter to execute");
            }


            TessEngine tessEngine = TessEngine.Gnerate(mContext);
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
            String result = tessEngine.detectText(bitmap);
            return result;


        }catch (Exception ex){
            Log.e(Constant.TAG,"Error: "+ex.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        mProgressDialog.dismiss();
       // Toast.makeText(mContext,"Result: "+s,Toast.LENGTH_LONG).show();

        DetectDialog detectDialog = new DetectDialog(mContext,s);
        detectDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        detectDialog.show();


    }
}

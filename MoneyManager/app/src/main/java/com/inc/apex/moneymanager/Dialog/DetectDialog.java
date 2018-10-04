package com.inc.apex.moneymanager.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.inc.apex.moneymanager.AddDetectActivity;
import com.inc.apex.moneymanager.R;
import com.inc.apex.moneymanager.Unit.Constant;

import org.w3c.dom.Text;

/**
 * Created by Dat.Pham on 10/2/2017.
 */

public class DetectDialog extends Dialog implements View.OnClickListener {

    private Button      mYesButton;
    private Button      mNoButton;
    private Context     mContext;
    private TextView    mTvDetect;
    private TextView    mTvMessage;
    private String      mDetectString;

    public DetectDialog(@NonNull Context context,String detect) {
        super(context);

        mContext      = context;
        mDetectString = detect;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_detect_number);
        mYesButton = (Button)findViewById(R.id.btn_yes);
        mNoButton  = (Button)findViewById(R.id.btn_no);
        mTvDetect  = (TextView)findViewById(R.id.tv_detect);
        mTvMessage = (TextView)findViewById(R.id.txt_dia);
        mYesButton.setOnClickListener(this);
        mNoButton.setOnClickListener(this);

        Log.d(Constant.TAG,"Detect number: "+mDetectString);
        if(mDetectString != null && !mDetectString.equals(""))
            mTvDetect.setText(mDetectString);
        else{
            mTvMessage.setText("Sorry! No return detect");
            mYesButton.setVisibility(View.GONE);
            mNoButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_yes:
                Intent intentDetect = new Intent(mContext, AddDetectActivity.class);
                intentDetect.putExtra("Text_Detect",mDetectString);
                mContext.startActivity(intentDetect);
                dismiss();
                break;
            case R.id.btn_no:
                dismiss();
                break;
        }
    }
}

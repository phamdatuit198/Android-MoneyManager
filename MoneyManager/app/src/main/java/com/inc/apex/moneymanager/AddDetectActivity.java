package com.inc.apex.moneymanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.inc.apex.moneymanager.Fragment.MoneyAddFragment;
import com.inc.apex.moneymanager.Unit.Common;
import com.inc.apex.moneymanager.Unit.Constant;
import com.inc.apex.moneymanager.Unit.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddDetectActivity extends AppCompatActivity implements View.OnClickListener,Spinner.OnItemSelectedListener {

    private String           mDetectNumber;
    private Button           mbtnSaving;
    private Button           mbtnCancel;
    private TextView         mtvAccount;
    private TextView         mtvCategory;
    private static TextView  mtvDate;
    private EditText         mEditAmount;
    private EditText         mEditContent;
    private Spinner          mSpinner;
    private boolean          flgChangeSpinner = false;
    private int              mTYPE= 0;
    private int              mSelectSpinner=0;
    private int              mID = 0;
    private static Calendar  mCalendar;
    private DatabaseHelper   myCom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_detect);

        mDetectNumber = getIntent().getExtras().getString("Text_Detect");
        mCalendar     = Calendar.getInstance();
        myCom         = DatabaseHelper.getInstance(this);

        mbtnSaving = (Button)findViewById(R.id.bt_save);
        mbtnCancel = (Button)findViewById(R.id.bt_cancel);
        TableRow tableRowDate = (TableRow)findViewById(R.id.tableRowDate);
        TableRow tableRowAcc  = (TableRow)findViewById(R.id.tableRowAccount);
        TableRow tableRowCat  = (TableRow)findViewById(R.id.tableRowCategory);
        TableRow tableRowAmo  = (TableRow)findViewById(R.id.tableRowAmount);
        TableRow tableRowCon  = (TableRow)findViewById(R.id.tableRowContent);


        mtvAccount   = (TextView)findViewById(R.id.tv_account);
        mtvCategory  = (TextView)findViewById(R.id.tv_category);
        mtvDate      = (TextView)findViewById(R.id.tv_date);
        mEditAmount  = (EditText)findViewById(R.id.edit_amount);
        mEditContent = (EditText)findViewById(R.id.edit_contents);
        mSpinner     = (Spinner)findViewById(R.id.list_type);

        //Create data for spinner
        String[] arraySpinner = {"Income", "Expense"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_item,R.id.tv_item_spinner, arraySpinner);
        mSpinner.setAdapter(adapter);

        //set default value of Spinner is Income
        mSpinner.setSelection(0);
        mSelectSpinner = Constant.TYPE_INCOME;
        mSpinner.setOnItemSelectedListener(this);

        tableRowAcc.setOnClickListener(this);
        tableRowCat.setOnClickListener(this);
        tableRowAmo.setOnClickListener(this);
        tableRowCon.setOnClickListener(this);
        tableRowDate.setOnClickListener(this);

        mbtnSaving.setOnClickListener(this);
        mbtnCancel.setOnClickListener(this);

        mEditAmount.setText(mDetectNumber);

        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        mtvDate.setText(date);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case  R.id.tableRowDate:
                showDatePickerDialog(v);
                break;

            case R.id.tableRowAccount:
                showAlerDialogAccount();

                break;
            case R.id.tableRowCategory:
                showAlerDialogCategory();
                break;
            case R.id.bt_save:
                saveAddDetect();
                break;
            case R.id.bt_cancel:
                defaultvalue();
                break;
        }
    }


    private void saveAddDetect(){

        String strAmount = mEditAmount.getText().toString();
        String strContent = mEditContent.getText().toString();

        //Convert Date to format YYYY-MM-DD : 2017-10-15
        String dateTime = Common.convertDatetoString(Constant.dayFormat_nine, Common.convertStringtoDate(Constant.dayFormat_one,mtvDate.getText().toString()));

        if(mtvAccount.getText().toString().trim().equals("")){

            Toast.makeText(this, "Please Enter Your Account", Toast.LENGTH_LONG).show();
        } if (mtvCategory.getText().toString().trim().equals("")){

            Toast.makeText(this, "Please Enter Your Category", Toast.LENGTH_LONG).show();
        } if(TextUtils.isEmpty(strAmount)) {
            mEditAmount.setError(Constant.ERROR_EMPTY);
        }if(TextUtils.isEmpty(strContent)){
            mEditContent.setError(Constant.ERROR_EMPTY);
        }else {
            String type = String.valueOf(mSelectSpinner);
            boolean isInserted = myCom.insertData(type,
                    dateTime,
                   // mtvDate.getText().toString(),
                    mtvAccount.getText().toString(),
                    mtvCategory.getText().toString(),
                    mEditAmount.getText().toString(),
                    mEditContent.getText().toString());

            if (isInserted == true) {
                Toast.makeText(this, "Data Inserted", Toast.LENGTH_LONG).show();
                //defaultvalue();
                finish();
            }else
                Toast.makeText(this, "Data not Inserted", Toast.LENGTH_LONG).show();
        }


    }


    private void showDatePickerDialog(View v){

        DatePickerFragment dataPickFragment = new AddDetectActivity.DatePickerFragment();
        android.app.FragmentManager fm = getFragmentManager();
        dataPickFragment.show(fm,"dataPicker");
    }


    private void showAlerDialogAccount(){
        final View dialogLayout;

        //get xml layour
        LayoutInflater li = LayoutInflater.from(this);

        if(mSelectSpinner == Constant.TYPE_INCOME) {
            dialogLayout = li.inflate(R.layout.dialog_add_account, null);
        }else{
            dialogLayout = li.inflate(R.layout.dialog_sub_account,null);
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        //set xml to alertdialogbuider
        alertDialogBuilder.setView(dialogLayout);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                RadioGroup rgGroup = (RadioGroup) dialogLayout.findViewById(R.id.rg_aler_account);
                RadioButton rbSelected = (RadioButton)dialogLayout.findViewById(rgGroup.getCheckedRadioButtonId());
                mtvAccount.setText(rbSelected.getText().toString());

            }
        });

        //create AlerDialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    private void showAlerDialogCategory(){

        final View dialogLayout;
        final RadioGroup rgGroup;

        //get xml layour
        LayoutInflater li = LayoutInflater.from(this);
        if(mSelectSpinner == Constant.TYPE_INCOME)
            dialogLayout = li.inflate(R.layout.dialog_add_catelogy,null);
        else
            dialogLayout = li.inflate(R.layout.dialog_sub_catelogy,null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        //set xml to alertdialogbuider
        alertDialogBuilder.setView(dialogLayout);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                RadioGroup rgGroup       = (RadioGroup)dialogLayout.findViewById(R.id.rg_aler_category);
                RadioButton rbSelected   = (RadioButton)dialogLayout.findViewById(rgGroup.getCheckedRadioButtonId());
                mtvCategory.setText(rbSelected.getText().toString());

            }
        });

        //create AlerDialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(position == 0) {
            mSelectSpinner = Constant.TYPE_INCOME;
            mEditAmount.setTextColor(Color.parseColor(Constant.COLOR_MONEY_GREEN));
        }else {
            mSelectSpinner = Constant.TYPE_EXPENSES;
            mEditAmount.setTextColor(Color.parseColor(Constant.COLOR_BLOOD_RED));
        }

        if(flgChangeSpinner)
            defaultvalue();
        else
            flgChangeSpinner = true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void defaultvalue(){

        mtvAccount.setText(null);
        mtvCategory.setText(null);
        mEditContent.setText(null);
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(),this,year,month,day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            //Do something with the date chosen by the user
            mCalendar.set(Calendar.YEAR,year);
            mCalendar.set(Calendar.MONTH,month);
            mCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

            updateLabel();
        }



        private void updateLabel(){

            String datetime = Constant.dayFormat_one.format(mCalendar.getTime());
            mtvDate.setText(datetime);
        }
    }
}

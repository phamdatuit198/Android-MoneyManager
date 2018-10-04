package com.inc.apex.moneymanager.Adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.inc.apex.moneymanager.AsyncTask.AsyncTaskHomeDelete;
import com.inc.apex.moneymanager.Fragment.HomeFragment;
import com.inc.apex.moneymanager.Fragment.MoneyAddFragment;
import com.inc.apex.moneymanager.Object.ActivityInforMoney;
import com.inc.apex.moneymanager.Object.DateEvent;
import com.inc.apex.moneymanager.R;
import com.inc.apex.moneymanager.Unit.Common;
import com.inc.apex.moneymanager.Unit.Constant;
import com.inc.apex.moneymanager.Unit.DatabaseHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Dat.Pham on 8/16/2017.
 */

public class MoneyAdapter extends ArrayAdapter<DateEvent> implements ListView.OnItemLongClickListener,ListView.OnItemClickListener,View.OnClickListener,Spinner.OnItemSelectedListener{

    private Context mContext;
    private int layoutId;
    private List<DateEvent> list = new ArrayList<DateEvent>();
    private List<ActivityInforMoney> listStatement = new ArrayList<ActivityInforMoney>();
    private DatabaseHelper myCom;
    private TextView tvTotalIncomeOnDay;
    private TextView tvTotalExpenseOnDay;
    private static DatabaseHelper mydb;
    private ListView lvSingle;
    private SingleStatementAdapter adapterSingle;
    private OnUpdateSummary mListenUpdateSummary;
    private static Calendar mCalendar;

    private TextView         mtvAccount;
    private TextView         mtvCategory;
    private static TextView  mtvDate;
    private EditText         mEditAmount;
    private EditText         mEditContent;
    private Button           btnSave;
    private Button           btnCancel;
    private AlertDialog      mAlertDialog;
    private Spinner          mSpinner;
    private boolean          flgChangeSpinner = false;
    private int              mTYPE= 0;
    private int              mSelectSpinner=0;
    private int              mID = 0;

    public MoneyAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<DateEvent> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.layoutId = resource;
        this.list = objects;

        mCalendar = Calendar.getInstance();

    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        myCom = DatabaseHelper.getInstance(mContext);
        //get Layout
        LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
        convertView = inflater.inflate(layoutId,null);

        lvSingle = (ListView) convertView.findViewById(R.id.lv_list_all_statement);
        TextView tvDate = (TextView)convertView.findViewById(R.id.tv_list_all_statement_day);
        TextView tvMonth = (TextView)convertView.findViewById(R.id.tv_list_all_statement_month);
        TextView tvDayWeek = (TextView)convertView.findViewById(R.id.tv_list_all_statement_dayweek);
        tvTotalIncomeOnDay = (TextView)convertView.findViewById(R.id.tv_list_all_statement_total);
        tvTotalExpenseOnDay = (TextView)convertView.findViewById(R.id.tv_list_all_statement_expenses);

        Date datetime = null;
        try {
           // datetime = Constant.dayFormat_one.parse(list.get(position).getDate());
            datetime = Constant.dayFormat_nine.parse(list.get(position).getDate());
            tvDate.setText(Common.convertDatetoString(Constant.dayFormat_five,datetime));
            tvMonth.setText(Common.convertDatetoString(Constant.dayFormat_three,datetime));
            tvDayWeek.setText(Common.convertDatetoString(Constant.dayFormat_four,datetime));


        } catch (ParseException e) {
            e.printStackTrace();
        }


        //show total income and expenses
        showIncomeTotal(list.get(position).getDate());

        //get List Daily from SQL
        mydb = DatabaseHelper.getInstance(mContext);
        listStatement =  mydb.getAllStatementByDate(list.get(position).getDate());

        adapterSingle = new SingleStatementAdapter(mContext, R.layout.single_statement,listStatement);
        lvSingle.setAdapter(adapterSingle);
        setListViewHeightBasedOnChildren(lvSingle);
        lvSingle.setOnItemLongClickListener(this);
        lvSingle.setOnItemClickListener(this);
        return convertView;
    }


    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    private void showIncomeTotal(String dateTime){

        String income;
        String expenses;

        expenses = Common.sumIncomeByDate(Constant.TYPE_EXPENSES,dateTime);
        income = Common.sumIncomeByDate(Constant.TYPE_INCOME,dateTime);

        tvTotalIncomeOnDay.setText("+"+income);
        tvTotalIncomeOnDay.setTextColor(Color.parseColor(Constant.COLOR_LIMEGREEN));
        tvTotalExpenseOnDay.setText("-"+expenses);
        tvTotalExpenseOnDay.setTextColor(Color.parseColor(Constant.COLOR_RED));
    }



    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {

        final ActivityInforMoney stateSelect = (ActivityInforMoney) parent.getItemAtPosition(position);
        //final Statement stateSelect = (Statement)lvSingle.getAdapter().getItem(position);
        // Toast.makeText(getContext(), "long clicked, "+"pos: " + stateSelect.getContents(), Toast.LENGTH_LONG).show();
        final DatabaseHelper data = DatabaseHelper.getInstance(mContext);
        final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        final AsyncTaskHomeDelete deleteStatement = new AsyncTaskHomeDelete(mContext,stateSelect);

        String message_one   = "Do you want to delete this statement!";

        String message_two   = "Category: "+stateSelect.getCategory();
        String message_three = "Contents: "+stateSelect.getContents();
        String message_four  = "Amount: ";

        if(stateSelect.getType() == Constant.TYPE_INCOME)
            message_four = ""+message_four+"+"+stateSelect.getAmount();
        else
            message_four = ""+message_four+"-"+stateSelect.getAmount();

        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message_one+"\n"+message_two+"\n"+message_three+"\n"+message_four);

        alertDialog.setButton(alertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                }
        );

        alertDialog.setButton(alertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int posEleDeleteList = -1;
                        if(parent.getCount() == 1){
                            posEleDeleteList = getDateOfParentAdapter(stateSelect.getDate());
                        }

                        deleteStatement.execute();
                        listStatement.remove(position);
                        adapterSingle.notifyDataSetChanged();

                        Log.d(Constant.TAG,"posEleDeleteList: "+posEleDeleteList);
                        if(posEleDeleteList == -1)
                            ((MoneyAdapter) HomeFragment.mListActivity.getAdapter()).notifyDataSetChanged();
                        else{
                            Log.d(Constant.TAG,"posEleDeleteList: "+posEleDeleteList);
                            ((List<DateEvent>)HomeFragment.listDateEvent).remove(posEleDeleteList);
                            ((MoneyAdapter)HomeFragment.mListActivity.getAdapter()).notifyDataSetChanged();
                        }

                       updateSummaryTable();

                    }
                }
        );

        alertDialog.setCancelable(true);
        alertDialog.show();
        return true;
    }




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Start Animation
        Common.AnimationClick(view);

        final ActivityInforMoney stateSelect = (ActivityInforMoney)parent.getItemAtPosition(position);
        final DatabaseHelper data = DatabaseHelper.getInstance(mContext);
        flgChangeSpinner = false;
        //get xml layout
        LayoutInflater li = LayoutInflater.from(mContext);
        final View dialogLayout = li.inflate(R.layout.dialog_edit_activity,null);

        TableRow tableRowDate = (TableRow)dialogLayout.findViewById(R.id.tableRowDate);
        TableRow tableRowAcc  = (TableRow)dialogLayout.findViewById(R.id.tableRowAccount);
        TableRow tableRowCat  = (TableRow)dialogLayout.findViewById(R.id.tableRowCategory);
        TableRow tableRowAmo  = (TableRow)dialogLayout.findViewById(R.id.tableRowAmount);
        TableRow tableRowCon  = (TableRow)dialogLayout.findViewById(R.id.tableRowContent);


        mtvAccount   = (TextView)dialogLayout.findViewById(R.id.tv_account);
        mtvCategory  = (TextView)dialogLayout.findViewById(R.id.tv_category);
        mtvDate      = (TextView)dialogLayout.findViewById(R.id.tv_date);
        mEditAmount  = (EditText)dialogLayout.findViewById(R.id.edit_amount);
        mEditContent = (EditText)dialogLayout.findViewById(R.id.edit_contents);
        mSpinner     = (Spinner)dialogLayout.findViewById(R.id.list_type);
        btnSave      = (Button)dialogLayout.findViewById(R.id.bt_save);
        btnCancel    = (Button)dialogLayout.findViewById(R.id.bt_cancel);

        //show information
        mtvDate.setText(stateSelect.getDate());
        mtvAccount.setText(stateSelect.getAccount());
        mtvCategory.setText(stateSelect.getCategory());
        mEditAmount.setText(stateSelect.getAmount());
        mEditContent.setText(stateSelect.getContents());
        mTYPE= stateSelect.getType();
        mID = stateSelect.getID();

        //Create data for spinner
        String[] arraySpinner = {"Income", "Expense"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,R.layout.spinner_item,R.id.tv_item_spinner, arraySpinner);
        mSpinner.setAdapter(adapter);



        //set select of spinner
        if(mTYPE == Constant.TYPE_INCOME) {
            mSpinner.setSelection(0);
            mSelectSpinner = Constant.TYPE_INCOME;
        }else if(mTYPE == Constant.TYPE_EXPENSES) {
            mSpinner.setSelection(1);
            mSelectSpinner = Constant.TYPE_EXPENSES;
        }

        mSpinner.setOnItemSelectedListener(this);


        tableRowAcc.setOnClickListener(this);
        tableRowCat.setOnClickListener(this);
        tableRowAmo.setOnClickListener(this);
        tableRowCon.setOnClickListener(this);
        tableRowDate.setOnClickListener(this);


        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        AlertDialog.Builder alerDialogBuider = new AlertDialog.Builder(mContext);

        alerDialogBuider.setView(dialogLayout);

        mAlertDialog = alerDialogBuider.create();
        //set Title Dialog
        String titleDialog = ""+Constant.TITLE_EDIT_DIALOG;
        mAlertDialog.setTitle(titleDialog);
        mAlertDialog.show();
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
                saveEditInformation();
                break;
            case R.id.bt_cancel:
                mAlertDialog.dismiss();
                break;
        }
    }


    private void showDatePickerDialog(View v){

        DialogFragment dataPickFragment = new MoneyAdapter.DatePickerMoneyAdapter();
        FragmentActivity activity = (FragmentActivity)v.getContext();
        dataPickFragment.show(activity.getSupportFragmentManager(),"dataPicker");

    }



    private void showAlerDialogAccount(){
        final View dialogLayout;

        //get xml layour
        LayoutInflater li = LayoutInflater.from(mContext);

        if(mSelectSpinner == Constant.TYPE_INCOME) {
            dialogLayout = li.inflate(R.layout.dialog_add_account, null);
            //rgGroup = (RadioGroup) dialogLayout.findViewById(R.id.rg_aler_account);
        }else{
            dialogLayout = li.inflate(R.layout.dialog_sub_account,null);
           // rgGroup = (RadioGroup)dialogLayout.findViewById(R.id.rg_aler_account);
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

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
        LayoutInflater li = LayoutInflater.from(mContext);
        if(mSelectSpinner == Constant.TYPE_INCOME)
            dialogLayout = li.inflate(R.layout.dialog_add_catelogy,null);
        else
            dialogLayout = li.inflate(R.layout.dialog_sub_catelogy,null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

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


    private void saveEditInformation(){

        String strAmount  = mEditAmount.getText().toString();
        String strContent = mEditContent.getText().toString();
        //Convert Date to format YYYY-MM-DD : 2017-10-15
        String dateTime = Common.convertDatetoString(Constant.dayFormat_nine, Common.convertStringtoDate(Constant.dayFormat_one,mtvDate.getText().toString()));


        if(mtvAccount.getText().toString().trim().equals("")){

            Toast.makeText(mContext, "Please Enter Your Account", Toast.LENGTH_LONG).show();
        } if (mtvCategory.getText().toString().trim().equals("")){

            Toast.makeText(mContext, "Please Enter Your Category", Toast.LENGTH_LONG).show();
        } if(TextUtils.isEmpty(strAmount)) {
            mEditAmount.setError(Constant.ERROR_EMPTY);
        }if(TextUtils.isEmpty(strContent)){
            mEditContent.setError(Constant.ERROR_EMPTY);
        }else {


             boolean isUpdated = mydb.updateData(mID, mSelectSpinner,
                      //mtvDate.getText().toString(),
                      dateTime,
                      mtvAccount.getText().toString(),
                      mtvCategory.getText().toString(),
                      mEditAmount.getText().toString(),
                      mEditContent.getText().toString());

            if (isUpdated == true) {
                Toast.makeText(mContext, "Data Inserted", Toast.LENGTH_LONG).show();
            }else
                Toast.makeText(mContext, "Data not Inserted", Toast.LENGTH_LONG).show();
        }
        mAlertDialog.dismiss();
        updateSummaryTable();
    }



    private void updateSummaryTable(){

        if(mListenUpdateSummary == null) {
            if (mContext instanceof MoneyAdapter.OnUpdateSummary) {
                mListenUpdateSummary = (MoneyAdapter.OnUpdateSummary) mContext;
                mListenUpdateSummary.onUpdate();
            } else {
                throw new RuntimeException(mContext.toString()
                        + " must implement onUpdateSummary");
            }
        }else
            mListenUpdateSummary.onUpdate();

    }

    public void setOnUpdateListner(OnUpdateSummary listener){
        mListenUpdateSummary = listener;
    }


    private int getDateOfParentAdapter(String date){
        int position = -1;
        for(int i = 0;i<list.size();i++){

            if(list.get(i).getDate().equals(date))
                position = i;
        }

        return position;
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

    private void defaultvalue(){

        mtvAccount.setText(null);
        mtvCategory.setText(null);
        mEditAmount.setText(null);
        mEditContent.setText(null);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface OnUpdateSummary{
        void onUpdate();
    }

    public static class DatePickerMoneyAdapter extends DialogFragment implements DatePickerDialog.OnDateSetListener{

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


    //public class CustomOnItemSelectedListener implements On



}

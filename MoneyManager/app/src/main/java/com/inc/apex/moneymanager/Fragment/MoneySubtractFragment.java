package com.inc.apex.moneymanager.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.inc.apex.moneymanager.Adapter.AccountAdapter;
import com.inc.apex.moneymanager.Object.Account;
import com.inc.apex.moneymanager.R;
import com.inc.apex.moneymanager.Unit.Common;
import com.inc.apex.moneymanager.Unit.Constant;
import com.inc.apex.moneymanager.Unit.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class MoneySubtractFragment extends Fragment implements View.OnClickListener,ListView.OnItemClickListener {
;
    private static final String TYPE_INCOME = "2";
    public static Calendar myCalendar;

    private OnFragmentInteractionListener mListener;

    private Context         mContext;
    private DatabaseHelper  mydb;

    private TextView        tvCategory;
    private static TextView tvDate;
    private TextView        tvAccount;

    private EditText        editContent;
    private EditText        editAmount;

    private RadioGroup      rgGroup;
    private RadioButton     rbSelected;

    private Button          btnSave;
    private Button          btnCancel;
    private String          ERROR_EMPTY =   "The Item name can't be empty";
    private AlertDialog     dialogChooseAccount;
    private EditText        mEditAddmore;
    private ListView        mlvAccount;
    public MoneySubtractFragment() {
        // Required empty public constructor
    }



    public MoneySubtractFragment(Context context) {
        // Required empty public constructor
        this.mContext = context;
        mydb = DatabaseHelper.getInstance(context);
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myCalendar = Calendar.getInstance();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root;
        root = inflater.inflate(R.layout.fragment_money_subtract, container, false);
        TableRow tableRowDate = (TableRow)root.findViewById(R.id.tableRowOne_expense);
        TableRow tableRowAcc = (TableRow)root.findViewById(R.id.tableRowAccount_expense);
        TableRow tableRowCat = (TableRow)root.findViewById(R.id.tableRowCategory_expense);
        TableRow tableRowAmo = (TableRow)root.findViewById(R.id.tableRowAmount_expense);
        TableRow tableRowCon = (TableRow)root.findViewById(R.id.tableRowContent_expense);

        tvAccount = (TextView)root.findViewById(R.id.tv_expense_account);
        tvCategory = (TextView)root.findViewById(R.id.tv_expense_category);
        tvDate  = (TextView)root.findViewById(R.id.tv_expense_date);
        editAmount = (EditText)root.findViewById(R.id.edit_expense_amount);
        editContent = (EditText)root.findViewById(R.id.edit_expense_contents);
        btnSave = (Button)root.findViewById(R.id.bt_expense_save);
        btnCancel = (Button)root.findViewById(R.id.bt_expense_cancel);

        tableRowDate.setOnClickListener(this);
        tableRowAcc.setOnClickListener(this);
        tableRowCat.setOnClickListener(this);
        tableRowAmo.setOnClickListener(this);
        tableRowCon.setOnClickListener(this);

        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        getCurrentDate();

        //set limit decimal edittext
        limitDecimalEditText();

        return root;
    }


    private void limitDecimalEditText(){

        editAmount.setFilters(new InputFilter[]{
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE){
                    //int beforeDecimal = 5;
                    int afterDecimal = 2;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                        String temp = editAmount.getText()+source.toString();
                        if(temp.equals("."))
                            return "0.";
                        else if(temp.toString().indexOf(".")==-1) {
                            //no decimal point placet yet
//                        if(temp.length()>beforeDecimal){
//
//                            return "";
//                        }
                        }else{
                            //has decimal point
                            temp = temp.substring(temp.indexOf(".")+1);
                            if(temp.length() > afterDecimal)
                                return"";
                        }

                        return super.filter(source, start, end, dest, dstart, dend);
                    }
                }
        });
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String name) {
        if (mListener != null) {
            mListener.onFragmentInteraction(name);
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tableRowOne_expense:
                showDatePickerDialog(v);
                break;
            case R.id.tableRowAccount_expense:
                showAlerDialogAccount();
                break;
            case R.id.tableRowCategory_expense:
                showAlerDialogCategory();
                break;
            case R.id.bt_expense_save:
                saveNewIncome();
                break;
            case R.id.bt_expense_cancel:
                defaultvalue();
                break;
            case R.id.imgbutton_add:
                addNewAccount(mEditAddmore.getText().toString());
                Toast.makeText(mContext,"add more: "+mEditAddmore.getText().toString(),Toast.LENGTH_LONG).show();
                break;
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String name);
    }



    private void getCurrentDate(){

        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        tvDate.setText(date);
    }



    private void defaultvalue(){

        tvAccount.setText(null);
        tvCategory.setText(null);
        editAmount.setText(null);
        editContent.setText(null);

    }




    private void showDatePickerDialog(View v){

        DialogFragment dataPickFragment = new DatePickerFragment();
        dataPickFragment.show(getActivity().getSupportFragmentManager(),"dataPicker");

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
            myCalendar.set(Calendar.YEAR,year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

            updateLabel();
        }

        private void updateLabel(){

            String datetime = Constant.dayFormat_one.format(myCalendar.getTime());
            Log.e("DAT","updateLabel: "+datetime);
            tvDate.setText(datetime);
        }
    }




    private void showAlerDialogAccount(){

        //get xml layour
        LayoutInflater li = LayoutInflater.from(mContext);
        final View dialogLayout = li.inflate(R.layout.dialog_sub_account,null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        //set xml to alertdialogbuider
        alertDialogBuilder.setView(dialogLayout);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                rgGroup = (RadioGroup)dialogLayout.findViewById(R.id.rg_aler_account);
                rbSelected = (RadioButton)dialogLayout.findViewById(rgGroup.getCheckedRadioButtonId());

                if(rbSelected.getText().toString().equals(Constant.ACCOUNT)){
                    showDialogDetailAccount();
                }else
                     tvAccount.setText(rbSelected.getText().toString());

            }
        });

        //create AlerDialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }





    private void showDialogDetailAccount(){

        LayoutInflater li = LayoutInflater.from(mContext);
        final View   dialogLayout   = li.inflate(R.layout.dialog_detail_account,null);
        mEditAddmore                = (EditText)dialogLayout.findViewById(R.id.et_addmore);
        mlvAccount                  = (ListView)dialogLayout.findViewById(R.id.lv_account);
        ImageButton buttonAdd       = (ImageButton)dialogLayout.findViewById(R.id.imgbutton_add);

        showListAccount();
        mlvAccount.setOnItemClickListener(this);

        buttonAdd.setOnClickListener(this);
        AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(mContext);
        alerDialogBuilder.setView(dialogLayout);
        alerDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialogChooseAccount = alerDialogBuilder.create();
        dialogChooseAccount.show();

    }



    private void addNewAccount(String name){

        boolean result = false;
        result = mydb.insertNewAccount(name);


        if(result)
            Toast.makeText(mContext,"Add new Account successful!",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(mContext,"Add new Account unsuccessful!",Toast.LENGTH_LONG).show();

        showListAccount();
        mEditAddmore.setText(null);
    }



    private void showListAccount(){

        //Call show List
        List<Account> listAccount = mydb.getAllAccount();
        if(listAccount.size()>0){

            AccountAdapter adapter = new AccountAdapter(mContext,R.layout.list_view_row_account_item,listAccount);
            mlvAccount.setAdapter(adapter);
            mlvAccount.setVisibility(View.VISIBLE);
        }

    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Common.AnimationClick(view);

        final Account select = (Account)parent.getItemAtPosition(position);
        tvAccount.setText(select.getNameBank().toString());

        if(dialogChooseAccount!=null)
            dialogChooseAccount.dismiss();
    }



    private void showAlerDialogCategory(){

        //get xml layour
        LayoutInflater li = LayoutInflater.from(mContext);
        final View dialogLayout = li.inflate(R.layout.dialog_sub_catelogy,null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        //set xml to alertdialogbuider
        alertDialogBuilder.setView(dialogLayout);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                rgGroup = (RadioGroup)dialogLayout.findViewById(R.id.rg_aler_category);
                rbSelected = (RadioButton)dialogLayout.findViewById(rgGroup.getCheckedRadioButtonId());
                tvCategory.setText(rbSelected.getText().toString());

            }
        });

        //create AlerDialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }




    private void saveNewIncome(){

        String strAmount = editAmount.getText().toString();
        String strContent = editContent.getText().toString();

        //Convert Date to format YYYY-MM-DD : 2017-10-15
        String dateTime = Common.convertDatetoString(Constant.dayFormat_nine, Common.convertStringtoDate(Constant.dayFormat_one,tvDate.getText().toString()));

        if(tvAccount.getText().toString().trim().equals("")){

            Toast.makeText(mContext, "Please Enter Your Account", Toast.LENGTH_LONG).show();
        } if (tvCategory.getText().toString().trim().equals("")){

            Toast.makeText(mContext, "Please Enter Your Category", Toast.LENGTH_LONG).show();
        } if(TextUtils.isEmpty(strAmount)) {
            editAmount.setError(ERROR_EMPTY);
        }if(TextUtils.isEmpty(strContent)){
            editContent.setError(ERROR_EMPTY);
        }else {

            boolean isInserted = mydb.insertData(TYPE_INCOME,
                   // tvDate.getText().toString(),
                    dateTime,
                    tvAccount.getText().toString(),
                    tvCategory.getText().toString(),
                    editAmount.getText().toString(),
                    editContent.getText().toString());

            if (isInserted == true) {
                Toast.makeText(mContext, "Data Inserted", Toast.LENGTH_LONG).show();
                defaultvalue();
            }else
                Toast.makeText(mContext, "Data not Inserted", Toast.LENGTH_LONG).show();
        }

        onButtonPressed(Constant.SUB);

    }
}

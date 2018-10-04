package com.inc.apex.moneymanager.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MoneyAddFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MoneyAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoneyAddFragment extends Fragment implements View.OnClickListener,ListView.OnItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1  = "param1";
    private static final String ARG_PARAM2  = "param2";
    private static final String TYPE_INCOME = "1";

    private Context         mContext;
    private TextView        mtvCategory;
    private static TextView mtvDate;
    private TextView        mtvAccount;

    private EditText        mEditContent;
    private EditText        mEditAmount;
    private EditText        mEditAddmore;
    private RadioGroup      rgGroup;
    private RadioButton     rbSelected;
    private ListView        mlvAccount;
    private Button          btnSave;
    private Button          btnCancel;
    private AlertDialog     dialogChooseAccount;
    private DatabaseHelper  mydb;

    public static Calendar  myCalendar;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MoneyAddFragment() {
        // Required empty public constructor
    }

    public MoneyAddFragment(Context context){
        this.mContext = context;
        mydb = DatabaseHelper.getInstance(context);
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoneyAddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoneyAddFragment newInstance(String param1, String param2, Context context) {
        MoneyAddFragment fragment = new MoneyAddFragment(context);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        myCalendar = Calendar.getInstance();
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment-
        View root;
        root= inflater.inflate(R.layout.fragment_money_add, container, false);
        TableRow tableRowDate = (TableRow)root.findViewById(R.id.tableRowDate);
        TableRow tableRowAcc = (TableRow)root.findViewById(R.id.tableRowAccount);
        TableRow tableRowCat = (TableRow)root.findViewById(R.id.tableRowCategory);
        TableRow tableRowAmo = (TableRow)root.findViewById(R.id.tableRowAmount);
        TableRow tableRowCon = (TableRow)root.findViewById(R.id.tableRowContent);


        mtvAccount = (TextView)root.findViewById(R.id.tv_income_account);
        mtvCategory = (TextView)root.findViewById(R.id.tv_income_category);
        mtvDate  = (TextView)root.findViewById(R.id.tv_income_date);
        mEditAmount = (EditText)root.findViewById(R.id.edit_income_amount);
        mEditContent = (EditText)root.findViewById(R.id.edit_income_contents);
        btnSave = (Button)root.findViewById(R.id.bt_income_save);
        btnCancel = (Button)root.findViewById(R.id.bt_income_cancel);


        tableRowAcc.setOnClickListener(this);
        tableRowCat.setOnClickListener(this);
        tableRowAmo.setOnClickListener(this);
        tableRowCon.setOnClickListener(this);
        tableRowDate.setOnClickListener(this);

        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);



        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        getCurrentDate();

        //set edit amount after Decimal = 2
        limitDecimalEditText();

        defaultvalue();
    }

    private void limitDecimalEditText(){

        mEditAmount.setFilters(new InputFilter[]{
            new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE){
                //int beforeDecimal = 5;
                int afterDecimal = 2;

                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                    String temp = mEditAmount.getText()+source.toString();
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
            case R.id.bt_income_save:
                saveNewIncome();
                break;
            case R.id.bt_income_cancel:
                defaultvalue();
                break;

            case R.id.imgbutton_add:
                addNewAccount(mEditAddmore.getText().toString());
                Toast.makeText(mContext,"add more: "+mEditAddmore.getText().toString(),Toast.LENGTH_LONG).show();
                break;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String name) {
        if (mListener != null) {
            mListener.onFragmentInteraction(name);
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

       // SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d yyyy");
       // String date = df.format(Calendar.getInstance().getTime());
        String date = Constant.dayFormat_one.format(Calendar.getInstance().getTime());
        mtvDate.setText(date);
    }



    private void showDatePickerDialog(View v){

        DialogFragment dataPickFragment = new DatePickerFragment();
        dataPickFragment.show(getActivity().getSupportFragmentManager(),"dataPicker");

    }



    private void showAlerDialogAccount(){

        //get xml layour
        LayoutInflater li = LayoutInflater.from(mContext);
        final View dialogLayout = li.inflate(R.layout.dialog_add_account,null);

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
                }else{
                    mtvAccount.setText(rbSelected.getText().toString());
                }

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
        ImageButton  buttonAdd      = (ImageButton)dialogLayout.findViewById(R.id.imgbutton_add);

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
        mtvAccount.setText(select.getNameBank().toString());

        if(dialogChooseAccount!=null)
            dialogChooseAccount.dismiss();
    }



    private void showAlerDialogCategory(){

        //get xml layour
        LayoutInflater li = LayoutInflater.from(mContext);
        final View dialogLayout = li.inflate(R.layout.dialog_add_catelogy,null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        //set xml to alertdialogbuider
        alertDialogBuilder.setView(dialogLayout);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                rgGroup = (RadioGroup)dialogLayout.findViewById(R.id.rg_aler_category);
                rbSelected = (RadioButton)dialogLayout.findViewById(rgGroup.getCheckedRadioButtonId());
                mtvCategory.setText(rbSelected.getText().toString());

            }
        });

        //create AlerDialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }




    private void saveNewIncome(){

        String strAmount = mEditAmount.getText().toString();
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

            boolean isInserted = mydb.insertData(TYPE_INCOME,
                   // mtvDate.getText().toString(),
                    dateTime,
                    mtvAccount.getText().toString(),
                    mtvCategory.getText().toString(),
                    mEditAmount.getText().toString(),
                    mEditContent.getText().toString());

            if (isInserted == true) {
                Toast.makeText(mContext, "Data Inserted", Toast.LENGTH_LONG).show();
                defaultvalue();
            }else
                Toast.makeText(mContext, "Data not Inserted", Toast.LENGTH_LONG).show();
        }

        onButtonPressed(Constant.ADD);
    }




    private void defaultvalue(){

        mtvAccount.setText(null);
        mtvCategory.setText(null);
        mEditAmount.setText(null);
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
            myCalendar.set(Calendar.YEAR,year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

            updateLabel();
        }



        private void updateLabel(){

            String datetime = Constant.dayFormat_one.format(myCalendar.getTime());
            mtvDate.setText(datetime);
        }
    }


}

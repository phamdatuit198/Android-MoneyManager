package com.inc.apex.moneymanager.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.inc.apex.moneymanager.Adapter.CalendarAdapter;
import com.inc.apex.moneymanager.Adapter.SingleStatementAdapter;
import com.inc.apex.moneymanager.Object.ActivityInforMoney;
import com.inc.apex.moneymanager.R;
import com.inc.apex.moneymanager.Unit.Common;
import com.inc.apex.moneymanager.Unit.Constant;
import com.inc.apex.moneymanager.Unit.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalendarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalendarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment implements View.OnClickListener,GridView.OnItemClickListener  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private Context     mContext;
    private ImageView previousButton;
    private ImageView   nextButton;
    private TextView currentDate;
    private TextView    tvTotalIncomeOnDay;
    private TextView    tvTotalExpenseOnDay;
    private GridView    calendarGridView;
    private DatabaseHelper mydb;

    private static final int MAX_CALENDAR_COLUMN = 42;

    private DatabaseHelper mQuery;

    private CalendarAdapter mAdapter;
    private List<ActivityInforMoney> listStatement= new ArrayList<ActivityInforMoney>();
    private SingleStatementAdapter adapter;
    private List<Date>                  dayValueInCells;


    private SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private Calendar cal = Calendar.getInstance(Locale.ENGLISH);

    private OnFragmentInteractionListener mListener;



    public CalendarFragment() {
        // Required empty public constructor
    }

    public CalendarFragment(Context context){
        mContext = context;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
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




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root;
        root= inflater.inflate(R.layout.fragment_calendar, container, false);

        previousButton = (ImageView)root.findViewById(R.id.previous_month);
        nextButton = (ImageView)root.findViewById(R.id.next_month);
        currentDate = (TextView)root.findViewById(R.id.display_current_date);
        calendarGridView = (GridView)root.findViewById(R.id.calendar_grid);

        previousButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        calendarGridView.setOnItemClickListener(this);

        setUpCalendarAdapter();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.previous_month:
                cal.add(Calendar.MONTH, -1);
                setUpCalendarAdapter();
                break;
            case R.id.next_month:
                cal.add(Calendar.MONTH, +1);
                setUpCalendarAdapter();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showAlerDialogList(dayValueInCells.get(position));
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


    private void setUpCalendarAdapter(){

        dayValueInCells = new ArrayList<Date>();

        mQuery = DatabaseHelper.getInstance(mContext);

        List<ActivityInforMoney> mEvent = mQuery.getAllStatement();

        Calendar mCal = (Calendar)cal.clone();
        mCal.set(Calendar.DAY_OF_MONTH,1);

        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1;
        mCal.add(Calendar.DAY_OF_MONTH,-firstDayOfTheMonth);

        while(dayValueInCells.size() < MAX_CALENDAR_COLUMN){
            dayValueInCells.add(mCal.getTime());
            mCal.add(Calendar.DAY_OF_MONTH,1);
        }
        String sDate = formatter.format(cal.getTime());
        currentDate.setText(sDate);
        mAdapter = new CalendarAdapter(mContext,dayValueInCells,cal,mEvent);
        calendarGridView.setAdapter(mAdapter);
    }


    private void showAlerDialogList(Date datetime){

        //get xml layout
        LayoutInflater li = LayoutInflater.from(mContext);
        final View dialogLayout = li.inflate(R.layout.dialog_calendar,null);

        ListView lvSingle   = (ListView) dialogLayout.findViewById(R.id.lv_aler_calendar_list);
        TextView tvDate     = (TextView)dialogLayout.findViewById(R.id.tv_aler_calendar_day);
        TextView tvMonth    = (TextView)dialogLayout.findViewById(R.id.tv_aler_calendar_month);
        TextView tvDayWeek  = (TextView)dialogLayout.findViewById(R.id.tv_aler_calendar_dayweek);
        tvTotalIncomeOnDay  = (TextView)dialogLayout.findViewById(R.id.tv_aler_calendar_total);
        tvTotalExpenseOnDay = (TextView)dialogLayout.findViewById(R.id.tv_aler_calendar_expenses);


        tvDate.setText(Common.convertDatetoString(Constant.dayFormat_five,datetime));
        tvMonth.setText(Common.convertDatetoString(Constant.dayFormat_three,datetime));
        tvDayWeek.setText(Common.convertDatetoString(Constant.dayFormat_four,datetime));

        //set up date
       // String dayclick = Common.convertDatetoString(Constant.dayFormat_one,datetime);
        String dayclick = Common.convertDatetoString(Constant.dayFormat_nine,datetime);
        //show total income and expenses
        showIncomeTotal(dayclick);

        //get List Daily from SQL
        mydb = DatabaseHelper.getInstance(mContext);
        listStatement =  mydb.getAllStatementByDate(dayclick);
        mydb.close();

        adapter = new SingleStatementAdapter(mContext,R.layout.single_statement,listStatement);
        lvSingle.setAdapter(adapter);

        AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(mContext);

        //set xml to alerdialobuider
        alerDialogBuilder.setView(dialogLayout);
        AlertDialog alertDialog = alerDialogBuilder.create();
        alertDialog.show();

    }


    private void showIncomeTotal(String dateTime){
        String income;
        String expenses;

        expenses = Common.sumIncomeByDate(Constant.TYPE_EXPENSES,dateTime);
        income = Common.sumIncomeByDate(Constant.TYPE_INCOME,dateTime);

        tvTotalIncomeOnDay.setText(income);
        tvTotalIncomeOnDay.setTextColor(Color.parseColor(Constant.COLOR_LIMEGREEN));
        tvTotalExpenseOnDay.setText(expenses);
        tvTotalExpenseOnDay.setTextColor(Color.parseColor(Constant.COLOR_RED));
    }



}

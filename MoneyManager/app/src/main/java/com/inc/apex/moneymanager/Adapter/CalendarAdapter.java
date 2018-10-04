package com.inc.apex.moneymanager.Adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.inc.apex.moneymanager.Unit.DatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Dat.Pham on 8/16/2017.
 */

public class CalendarAdapter extends ArrayAdapter {
    private LayoutInflater mInflater;
    private List<Date> monthlyDates;
    private Calendar currentDate;
    private List<ActivityInforMoney> allEvents;
    private DatabaseHelper mydb;
    private Context con;
    private TextView eventIncome;
    private TextView eventExpense;
    private TextView eventTotal;

    public CalendarAdapter(Context context, List<Date> monthlyDates, Calendar currentDate, List<ActivityInforMoney> allEvents){
        super(context, R.layout.single_cell_layout);
        this.monthlyDates = monthlyDates;
        this.currentDate = currentDate;
        this.allEvents = allEvents;
        mInflater = LayoutInflater.from(context);
        con = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        Date mDate = monthlyDates.get(position);
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(mDate);

        int dayValue = dateCal.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCal.get(Calendar.MONTH)+1;
        int displayYear = dateCal.get(Calendar.YEAR);

        int currentMonth = currentDate.get(Calendar.MONTH)+1;
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentday = currentDate.get(Calendar.DAY_OF_MONTH);

        if(view == null)
            view = mInflater.inflate(R.layout.single_cell_layout,parent,false);

        if(displayMonth == currentMonth && displayYear == currentYear)
            view.setBackgroundColor(Color.parseColor(Constant.COLOR_WHITE));
        else
            view.setBackgroundColor(Color.parseColor(Constant.COLOR_GRAY));

        if(displayMonth == currentMonth && displayYear == currentYear && dayValue == currentday)
            view.setBackgroundColor(Color.parseColor(Constant.COLOR_LIGHT_BLUE));

        // Add day to calendar
        TextView cellNumber = (TextView)view.findViewById(R.id.calendar_date_id);
        cellNumber.setText(String.valueOf(dayValue));

        eventIncome  = (TextView)view.findViewById(R.id.tv_event_income);
        eventExpense = (TextView)view.findViewById(R.id.tv_event_expenses);
        eventTotal   = (TextView)view.findViewById(R.id.tv_event_total);

        Calendar eventCalendar = Calendar.getInstance();

       // SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d yyyy");

        for(int i=0;i<allEvents.size();i++){

            try {
               // Date datetime = df.parse(allEvents.get(i).getDate());
                Date datetime = Constant.dayFormat_nine.parse(allEvents.get(i).getDate());
                eventCalendar.setTime(datetime);

                if(dayValue == eventCalendar.get(Calendar.DAY_OF_MONTH) && displayMonth == (eventCalendar.get(Calendar.MONTH) + 1)
                        && displayYear == eventCalendar.get(Calendar.YEAR)) {
                    showIncomeTotal(allEvents.get(i).getDate());

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
        return view;
    }

    @Override
    public int getCount() {
        return monthlyDates.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return monthlyDates.get(position);
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return monthlyDates.indexOf(item);
    }




    private void showIncomeTotal(String dateTime){
        String income;
        String expenses;
        String total = "0";

        double totalInc=0;
        double totalExp=0;
        double totalMon=0;

        mydb = DatabaseHelper.getInstance(con);
        income= mydb.sumStatementByDate(Constant.TYPE_INCOME,dateTime);
        eventIncome.setText(income);
        eventIncome.setTextColor(Color.parseColor(Constant.COLOR_LIMEGREEN));

        mydb = DatabaseHelper.getInstance(con);
        expenses= mydb.sumStatementByDate(Constant.TYPE_EXPENSES,dateTime);
        eventExpense.setText(expenses);
        eventExpense.setTextColor(Color.parseColor(Constant.COLOR_RED));

        if(income != null)
            totalInc = Double.parseDouble(income);

        if(expenses != null)
            totalExp = Double.parseDouble(expenses);

        totalMon = totalInc - totalExp;
        String formattedTotal = String.format("%.2f",totalMon);
        total = ""+formattedTotal;
        if(totalMon>0) {
           // total = ""+ Double.toString(totalMon);
            eventTotal.setTextColor(Color.parseColor(Constant.COLOR_MAGENTA));
        }else{
           // total = ""+ Double.toString(totalMon);
            eventTotal.setTextColor(Color.parseColor(Constant.COLOR_RED));
        }
        eventTotal.setText(total);


        mydb.close();
    }
}

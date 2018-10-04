package com.inc.apex.moneymanager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.inc.apex.moneymanager.Adapter.MoneyAdapter;
import com.inc.apex.moneymanager.Fragment.CalendarFragment;
import com.inc.apex.moneymanager.Fragment.HomeFragment;
import com.inc.apex.moneymanager.Fragment.MoneyAddFragment;
import com.inc.apex.moneymanager.Fragment.MoneySubtractFragment;
import com.inc.apex.moneymanager.Fragment.SortFragment;
import com.inc.apex.moneymanager.Unit.Constant;
import com.inc.apex.moneymanager.Unit.DatabaseHelper;

public class MoneyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener
        ,HomeFragment.OnFragmentInteractionListener
        ,MoneyAddFragment.OnFragmentInteractionListener
        ,MoneySubtractFragment.OnFragmentInteractionListener
        ,CalendarFragment.OnFragmentInteractionListener
        ,SortFragment.OnFragmentInteractionListener
        ,MoneyAdapter.OnUpdateSummary {

    private AdView                  mAdView;
    private FragmentManager         manager;
    private Toolbar                 toolbar;
    private HomeFragment            mHomeFragment;
    private MoneyAddFragment        mMoneyAddFragment;
    private MoneySubtractFragment   mMoneySubtractFragment;
    private CalendarFragment        mCalendarFragment;
    private SortFragment            mSortFragment;
    private FloatingActionButton    mFabAdd;
    private FloatingActionButton    mFabSub;
    private TextView                tvIncome;
    private TextView                tvExpenses;
    private TextView                tvTotal;
    private DatabaseHelper          mydb;
    private InterstitialAd          mInterstitial;
    private NavigationView          navigationView;
    private Bundle                  mSavedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_money);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        mFabSub = (FloatingActionButton)findViewById(R.id.fab_sub);

        mFabAdd.setOnClickListener(this);
        mFabSub.setOnClickListener(this);

        tvIncome     = (TextView)findViewById(R.id.tv_income);
        tvExpenses   = (TextView)findViewById(R.id.tv_expenses);
        tvTotal      = (TextView)findViewById(R.id.tv_total);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //delete Item color default because we need to custom color for each item
        navigationView.setItemIconTintList(null);

        //set color Item Navigation
        setColorItemNavigation();



        //initial FragmentManager
        manager = getSupportFragmentManager();


        //animation click listview
       // this.overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadAdMob();
        loadHome();
        showIncomeTotal();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(manager.getBackStackEntryCount() > 0) {
                FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
                manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);

                loadHome();
            }else
                super.onBackPressed();
        }
    }







    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            loadHome();
        } else if (id == R.id.nav_calendar) {
            loadCalendar();
        } else if (id == R.id.nav_add) {
            loadAdd();
        } else if (id == R.id.nav_subtract) {
            loadSubtract();
        } else if (id == R.id.nav_share) {
            shareFriend();
        } else if (id == R.id.nav_scan) {
            Intent intentScan = new Intent(this, ScanReceiptActivity.class);
            startActivity(intentScan);
        }else if(id == R.id.nav_feedback){
            sendFeedBack();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    @Override
    public void onFragmentInteraction(String name) {
        showIncomeTotal();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_add:

                loadAdd();
                break;
            case R.id.fab_sub:
                loadSubtract();
                break;

            default:
                break;
        }
    }




    /**********************************
     Load Admob
     ***********************************/

    private void loadAdMob(){
        // Use AdRequest.Builder.addTestDevice("011B6C2EF91024DC507DF5F73B35301D") to get test ads on this device.
        mAdView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(Constant.TESTING_ID)
                .build();
        mAdView.loadAd(adRequest);
    }



    /************************************
     Load home page
     ************************************/

    private void loadHome(){
        //delete backstack
        if(mSavedInstanceState == null) {
            if (manager.getBackStackEntryCount() > 0) {
                FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
                manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            toolbar.setTitle("Money Manager");
            if (mHomeFragment == null)
                mHomeFragment = new HomeFragment(this);

            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.content_fragment, mHomeFragment, "Home");
            transaction.commitAllowingStateLoss();
        }
    }




    /*************************************************
     Add Money
     ***************************************************/

    private void loadAdd(){
        toolbar.setTitle(Constant.ADD_MONEY_TITLE);
        if(mSavedInstanceState == null) {
            if (mMoneyAddFragment == null)
                mMoneyAddFragment = new MoneyAddFragment(this);

            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.content_fragment, mMoneyAddFragment, "Add");
            transaction.addToBackStack("Home");
            //transaction.commit();

            transaction.commitAllowingStateLoss();
        }
    }



    /*************************************************
     *      subtract Money
     ***************************************************/

    private void loadSubtract(){
        toolbar.setTitle(Constant.SUBTRACT_MONEY_TITLE);
        if(mSavedInstanceState == null) {
            if (mMoneySubtractFragment == null)
                mMoneySubtractFragment = new MoneySubtractFragment(this);

            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.content_fragment, mMoneySubtractFragment, "Subtract");
            transaction.addToBackStack("Home");
            //transaction.commit();
            transaction.commitAllowingStateLoss();
        }

    }



    /****************************************************
     *      Calculate Income, Expenses, Total
     ****************************************************/

    private void showIncomeTotal(){
        String income   ="0";
        String expenses ="0";
        String total    ="0";

        double totalInc=0;
        double totalExp=0;
        double totalMon=0;

        mydb = DatabaseHelper.getInstance(this);
        income= mydb.sumStatement(Constant.TYPE_INCOME);


        mydb = DatabaseHelper.getInstance(this);
        expenses= mydb.sumStatement(Constant.TYPE_EXPENSES);


        if(income != null) {
            totalInc = Double.parseDouble(income);
            tvIncome.setText("+ $"+income);
        }else
            tvIncome.setText("$0");

        if(expenses != null) {
            totalExp = Double.parseDouble(expenses);
            tvExpenses.setText("- $"+expenses);
        }else
            tvExpenses.setText("$0");

        totalMon = totalInc - totalExp;
        String formattedTotal = String.format("%.2f",totalMon);
        total = "$"+formattedTotal;
        if(totalMon>0) {
            //total = "$" + Double.toString(totalMon);
            tvTotal.setTextColor(Color.parseColor(Constant.COLOR_BLACK));
        }else{
            //total = "$" + Double.toString(totalMon);
            tvTotal.setTextColor(Color.parseColor(Constant.COLOR_RED));
        }
        tvTotal.setText(total);
        mydb.close();
    }


    /*************************************************
     *      LoadCalendar
     **********************************************/

    private void loadCalendar(){
        toolbar.setTitle("Calendar");
        if(mSavedInstanceState == null) {
            if (mCalendarFragment == null)
                mCalendarFragment = new CalendarFragment(this);

            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.content_fragment, mCalendarFragment, "Calendar");
            transaction.addToBackStack("Home");
            transaction.commitAllowingStateLoss();
        }
    }

    /***********************************************
     *      share
     ***********************************************/

    private void shareFriend(){

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT,"Share Money Manager");
        sharingIntent.putExtra(Intent.EXTRA_TEXT,"Ready to manager your money to click here. "+Constant.SHAREAPP);
        startActivity(Intent.createChooser(sharingIntent,"Share via"));
    }


    /**************************************************
     *      send email
     **************************************************/

    private void sendFeedBack(){

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        String emailOfCustomer[] = {Constant.EMAIL};
        emailIntent.putExtra(Intent.EXTRA_EMAIL,emailOfCustomer);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,Constant.FEEDBACK);

        startActivity(Intent.createChooser(emailIntent,"Send your email in: "));
    }



    @Override
    public void onUpdate() {
        Log.d(Constant.TAG,"Update Table of Money Activity");
     //   showIncomeTotal();
        Fragment fragment = null;
        fragment = getSupportFragmentManager().findFragmentByTag("Home");
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(fragment);
        ft.attach(fragment);
        ft.commitAllowingStateLoss();
    }



    /***********************
        Set Color of Navigation
     *************************/
    private void setColorItemNavigation(){

        //set Color of Home
        MenuItem homeItem = navigationView.getMenu().findItem(R.id.nav_home);
        SpannableString spanHome = new SpannableString(homeItem.getTitle());
        spanHome.setSpan(new TextAppearanceSpan(this,R.style.Home),0,spanHome.length(),0);
        homeItem.setTitle(spanHome);
        //set color of icon Home
        homeItem.getIcon().setColorFilter(Color.parseColor(Constant.COLOR_DARK_BLUE),PorterDuff.Mode.SRC_IN);

        //set color of Calendar
        MenuItem calendarItem = navigationView.getMenu().findItem(R.id.nav_calendar);
        SpannableString spanCalendar = new SpannableString(calendarItem.getTitle());
        spanCalendar.setSpan(new TextAppearanceSpan(this,R.style.Calendar),0,spanCalendar.length(),0);
        calendarItem.setTitle(spanCalendar);
        //set Color of icon Calendar
        calendarItem.getIcon().setColorFilter(Color.parseColor(Constant.COLOR_DARK_BLUE),PorterDuff.Mode.SRC_IN);

        //set color of income
        MenuItem incomeItem = navigationView.getMenu().findItem(R.id.nav_add);
        SpannableString span = new SpannableString(incomeItem.getTitle());
        span.setSpan(new TextAppearanceSpan(this,R.style.Income),0,span.length(),0);
        incomeItem.setTitle(span);
        //set color for icon Income
        incomeItem.getIcon().setColorFilter(Color.parseColor(Constant.COLOR_MONEY_GREEN),PorterDuff.Mode.SRC_IN);

        //set color of expense
        MenuItem expenseItem = navigationView.getMenu().findItem(R.id.nav_subtract);
        SpannableString spanExpense = new SpannableString(expenseItem.getTitle());
        spanExpense.setSpan(new TextAppearanceSpan(this,R.style.Expense),0,spanExpense.length(),0);
        expenseItem.setTitle(spanExpense);
        //set color for icon expense
        expenseItem.getIcon().setColorFilter(Color.parseColor(Constant.COLOR_BLOOD_RED),PorterDuff.Mode.SRC_ATOP);

        //set color of Scan
        MenuItem scanItem = navigationView.getMenu().findItem(R.id.nav_scan);
        SpannableString spanScan = new SpannableString(scanItem.getTitle());
        spanScan.setSpan(new TextAppearanceSpan(this,R.style.Scan),0,spanScan.length(),0);
        scanItem.setTitle(spanScan);
        //set color for icon scan
        scanItem.getIcon().setColorFilter(Color.parseColor(Constant.COLOR_DARK_BLUE),PorterDuff.Mode.SRC_IN);


        //set color of Share
        MenuItem shareItem = navigationView.getMenu().findItem(R.id.nav_share);
        SpannableString spanShare = new SpannableString(shareItem.getTitle());
        spanShare.setSpan(new TextAppearanceSpan(this,R.style.Share),0,spanShare.length(),0);
        shareItem.setTitle(spanShare);
        //set color for icon share
        shareItem.getIcon().setColorFilter(Color.parseColor(Constant.COLOR_DARK_BLUE),PorterDuff.Mode.SRC_IN);


        //set color of Share
        MenuItem feedbackItem = navigationView.getMenu().findItem(R.id.nav_feedback);
        SpannableString spanFeedBack = new SpannableString(feedbackItem.getTitle());
        spanFeedBack.setSpan(new TextAppearanceSpan(this,R.style.Feedback),0,spanFeedBack.length(),0);
        feedbackItem.setTitle(spanFeedBack);
        //set color for icon share
        feedbackItem.getIcon().setColorFilter(Color.parseColor(Constant.COLOR_DARK_BLUE),PorterDuff.Mode.SRC_IN);
    }




}
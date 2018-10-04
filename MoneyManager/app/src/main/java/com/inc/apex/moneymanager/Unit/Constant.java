package com.inc.apex.moneymanager.Unit;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Dat.Pham on 8/16/2017.
 */

public class Constant {

    public static String TAG = "APEX";

    public static String ADD = "ADD";
    public static String SUB = "SUB";
    public static String HOME = "HOME";

    public static int TYPE_INCOME = 1;
    public static int TYPE_EXPENSES = 2;


    //Size of Phone
    public static int WIDTH_OF_IMAGE_VIEW_TAKE_PICTURE = 0;
    public static int HEIGHT_OF_IMAGE_VIEW_TAKE_PICTURE = 0;
    public static int WIDTH_PHONE=0;
    public static int HEIGHT_PHONE=0;



    //Format date
    /*
            Year:   YYYY (eg 1997)
            Year and month:  YYYY-MM (eg 1997-07)
            Complete date:   YYYY-MM-DD (eg 1997-07-16)
            Complete date plus hours and minutes:            YYYY-MM-DDThh:mmTZD (eg 1997-07-16T19:20+01:00)
            Complete date plus hours, minutes and seconds:   YYYY-MM-DDThh:mm:ssTZD (eg 1997-07-16T19:20:30+01:00)
     */

    public  static SimpleDateFormat dayFormat_one   = new SimpleDateFormat("EEE, MMM d yyyy");
    public  static SimpleDateFormat dayFormat_two   = new SimpleDateFormat("EEEE, MMM d yyyy");
    public  static SimpleDateFormat dayFormat_three = new SimpleDateFormat("M-yyyy");
    public  static SimpleDateFormat dayFormat_four  = new SimpleDateFormat("EEEE");
    public  static SimpleDateFormat dayFormat_five  = new SimpleDateFormat("d");
    public  static SimpleDateFormat dayFormat_six   = new SimpleDateFormat("EEEE, d, yyyy");
    public  static SimpleDateFormat dayFormat_seven = new SimpleDateFormat("yyyyMMdd_HHmmss");
    public  static SimpleDateFormat dayFormat_eight = new SimpleDateFormat("HH:mm");
    public  static SimpleDateFormat dayFormat_nine  = new SimpleDateFormat("yyyy-MM-DD");

    //Color
    public static String COLOR_MAGENTA    = "#FF00FF";
    public static String COLOR_RED        = "#cc0000";
    public static String COLOR_BLOOD_RED  = "#830303";
    public static String COLOR_LIMEGREEN  = "#32CD32";
    public static String COLOR_WHITE      = "#FFFFFF";
    public static String COLOR_GRAY       = "#CCCCCC";
    public static String COLOR_LIGHT_GRAY = "#A0A0A0";
    public static String COLOR_LIGHT_BLUE = "#c8e8ff";
    public static String COLOR_LIGHT_RED  = "#FF4081";
    public static String COLOR_LESSGREEN  = "#26B426";
    public static String COLOR_ORANGE     = "#FFAE19";
    public static String COLOR_BLACK      = "#000000";
    public static String COLOR_MONEY_GREEN = "#85bb65";
    public static String COLOR_DARK_BLUE   ="#0d4065";

    //Name
    public static String ADD_MONEY_TITLE         = "Add to Income";
    public static String SUBTRACT_MONEY_TITLE    = "Add to Expense";
    public static String TITLE_EDIT_DIALOG       = "Edit Information";
    public static String ACCOUNT                 = "Accounts";
    //Link

    public static String EMAIL    = "ApexAdProApps@gmail.com";
    public static String FEEDBACK = "Feedback of Customer";
    public static String SHAREAPP = "http://www.apexonlinepro.com/";

    public static String ADUNIT_ID_INTER = "ca-app-pub-7576786668808413/6929518205";
    public static String ADMOB_ID        = "ca-app-pub-7576786668808413~4713761868";
    public static String TESTING_ID      = "011B6C2EF91024DC507DF5F73B35301D";

    //ERROR
    public static String ERROR_EMPTY ="The Item name can't be empty";

}

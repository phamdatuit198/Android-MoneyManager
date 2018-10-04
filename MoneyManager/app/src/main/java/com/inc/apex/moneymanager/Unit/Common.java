package com.inc.apex.moneymanager.Unit;

import android.content.Context;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Dat.Pham on 8/16/2017.
 */

public class Common {


    public static  DatabaseHelper comDataBaseHelper;

    public static void setupCommon(Context conn){

        //initialize databasehelper
        comDataBaseHelper = DatabaseHelper.getInstance(conn);
//
//        //create table data
//        comDataBaseHelper.insertDataApplication(Constant.NAME_MONEY_MANAGER,Constant.CONTENT_MONEY_MANAGER,Constant.IMAGE_MONEY_MANAGER);
//        comDataBaseHelper.insertDataApplication(Constant.NAME_GPS_RUNNING,Constant.CONTENT_GPS_RUNNING,Constant.IMAGE_GPS_RUNNING);
//        comDataBaseHelper.insertDataApplication(Constant.NAME_CALENDAR_APPOITMENT,Constant.CONTENT_CALENDAR_APPOITMENT,Constant.IMAGE_CALENDAR_APPOITMENT);
//        //comDataBaseHelper.insertDataApplication(Constant.NAME_EPIC,Constant.CONTENT_EPIC,Constant.IMAGE_EPIC);
//        comDataBaseHelper.insertDataApplication(Constant.NAME_CAMERA,Constant.CONTENT_CAMERA,Constant.IMAGE_CAMERA);
//        //setCameraTouch
//        oldDistanceTouchCamera = 0;
//        newDistanceTouchCamera = 0;


    }


    public static String convertDatetoString(SimpleDateFormat formatDate, Date dateTime){
        String result = null;
        result = formatDate.format(dateTime);
        return result;
    }


    public static Date convertStringtoDate(SimpleDateFormat format, String string){
        Date result = null;

        try{
            result = format.parse(string);

        }
        catch (ParseException e){
            e.printStackTrace();
        }
        return result;
    }

    public static String sumIncomeByDate(int type, String dateTime){

        String result = "0";
        result= comDataBaseHelper.sumStatementByDate(type,dateTime);
        comDataBaseHelper.close();

        if(result == null)
            result = "0";

        return result;
    }



   public static void AnimationClick(View view){
       Animation animation = new AlphaAnimation(0.3f, 1.0f);
       animation.setDuration(4000);
       view.startAnimation(animation);
   }


}

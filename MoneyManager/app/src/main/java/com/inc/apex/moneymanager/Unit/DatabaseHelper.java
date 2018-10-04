package com.inc.apex.moneymanager.Unit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.inc.apex.moneymanager.Object.Account;
import com.inc.apex.moneymanager.Object.ActivityInforMoney;
import com.inc.apex.moneymanager.Object.DateEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dat.Pham on 8/16/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME                   = "Daily.db";
    public static final String TABLE_NAME_ACTIVITY_MONEY       = "Activity_table";
    public static final String TABLE_NAME_BANK                 = "bank_table";

    public static final String COL_ID       = "ID";
    public static final String COL_TYPE     = "TYPE";
    public static final String COL_DATE     = "DATE";
    public static final String COL_ACCOUNT  = "ACCOUNT";
    public static final String COL_CATEGORY = "CATEGORY";
    public static final String COL_AMOUNT   = "AMOUNT";
    public static final String COL_CONTENT  = "CONTENT";
    public static final String COL_NAMEBANK = "NAMEBANK";


    private static DatabaseHelper sInstance;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    public static DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table if not exists " + TABLE_NAME_ACTIVITY_MONEY + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,TYPE INTEGER,DATE DATETIME,ACCOUNT TEXT,CATEGORY TEXT,AMOUNT TEXT,CONTENT TEXT)");
        db.execSQL("create table if not exists " + TABLE_NAME_BANK + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAMEBANK TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ACTIVITY_MONEY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_BANK);
        onCreate(db);
    }

    /*****************************************************************************************************************************
     FUNCTION ACCESS DATABASE IN DAILY_TABLE
     ******************************************************************************************************************************/


    public boolean insertData(String type, String date, String account, String category, String amount, String content) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TYPE, type);
        contentValues.put(COL_DATE,date );
        contentValues.put(COL_ACCOUNT, account);
        contentValues.put(COL_CATEGORY, category);
        contentValues.put(COL_AMOUNT, amount);
        contentValues.put(COL_CONTENT, content);
        long result = db.insert(TABLE_NAME_ACTIVITY_MONEY, null, contentValues);

        db.close();

        if (result == -1)
            return false;
        else
            return true;
    }


    public boolean updateData(int id, int type, String date, String account, String category, String amount, String content){

        SQLiteDatabase dbWrite = this.getWritableDatabase();
        ContentValues  contentValues = new ContentValues();
        contentValues.put(COL_TYPE,String.valueOf(type));
        contentValues.put(COL_DATE,date);
        contentValues.put(COL_ACCOUNT,account);
        contentValues.put(COL_CATEGORY,category);
        contentValues.put(COL_AMOUNT,amount);
        contentValues.put(COL_CONTENT,content);

        String selection = COL_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        return dbWrite.update(TABLE_NAME_ACTIVITY_MONEY,contentValues,selection,selectionArgs) >0;
    }





    public List<ActivityInforMoney> getAllStatement() {

        List<ActivityInforMoney> listActivityInforMoney = new ArrayList<ActivityInforMoney>();
        String selectQuerry = "SELECT * FROM "+TABLE_NAME_ACTIVITY_MONEY;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(selectQuerry,null);

        if(res.moveToFirst()){
            do{
                ActivityInforMoney state = new ActivityInforMoney();
                state.setID(Integer.parseInt(res.getString(0)));
                state.setType(Integer.parseInt(res.getString(1)));
                state.setDate(res.getString(2));
                state.setAccount(res.getString(3));
                state.setCategory(res.getString(4));
                state.setAmount(res.getString(5));
                state.setContents(res.getString(6));

                listActivityInforMoney.add(state);
            }while(res.moveToNext());
        }
        return listActivityInforMoney;

    }




    public List<DateEvent> getDateEvent(){

        List<DateEvent> listDateEvent = new ArrayList<DateEvent>();
       // String selectQuerry = "SELECT DISTINCT DATE FROM "+TABLE_NAME_ACTIVITY_MONEY;
        String selectQuerry = "SELECT DISTINCT DATE FROM "+TABLE_NAME_ACTIVITY_MONEY+ " ORDER BY DATE";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(selectQuerry,null);

        if(res.moveToFirst()){
            do{
                DateEvent state = new DateEvent();
                state.setDate(res.getString(0));

                listDateEvent.add(state);
            }while(res.moveToNext());
        }


        return listDateEvent;
    }




    public List<ActivityInforMoney> getAllStatementByDate(String dateTime) {

        List<ActivityInforMoney> listActivityInforMoney = new ArrayList<ActivityInforMoney>();
        String selectQuerry = "SELECT * FROM "+TABLE_NAME_ACTIVITY_MONEY+" WHERE DATE= '"+dateTime.toString()+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(selectQuerry,null);

        if(res.moveToFirst()){
            do{
                ActivityInforMoney state = new ActivityInforMoney();
                state.setID(Integer.parseInt(res.getString(0)));
                state.setType(Integer.parseInt(res.getString(1)));
                state.setDate(res.getString(2));
                state.setAccount(res.getString(3));
                state.setCategory(res.getString(4));
                state.setAmount(res.getString(5));
                state.setContents(res.getString(6));

                listActivityInforMoney.add(state);
            }while(res.moveToNext());
        }


        return listActivityInforMoney;

    }




    public boolean deleteStatement(ActivityInforMoney state){
        boolean result = true;
        SQLiteDatabase db = this.getWritableDatabase();
        result = db.delete(TABLE_NAME_ACTIVITY_MONEY,COL_ID+"=?",new String[] {String.valueOf(state.getID())}) > 0;
        db.close();

        return result;

    }




    public String sumStatement(int type){
        String stringSum = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String querry = "SELECT SUM("+COL_AMOUNT+") FROM "+TABLE_NAME_ACTIVITY_MONEY+" WHERE TYPE = "+type;
        Cursor res = db.rawQuery(querry,null);

        if(res.moveToFirst())
            stringSum = res.getString(0);


        return stringSum;
    }




    public String sumStatementByDate(int type, String dateTime){
        String stringSum = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String querry = "SELECT SUM("+COL_AMOUNT+") FROM "+TABLE_NAME_ACTIVITY_MONEY+" WHERE TYPE = "+type+" AND DATE= '"+dateTime.toString()+"'";
        Cursor res = db.rawQuery(querry,null);

        if(res.moveToFirst())
            stringSum = res.getString(0);
        return stringSum;
    }


    /*************************************************************************************************************************************************************************************************
                    FUNCTION OF ACCOUNT OBJECT
     *************************************************************************************************************************************************************************************************/

    public boolean insertNewAccount(String name){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAMEBANK,name);
        long result = db.insert(TABLE_NAME_BANK,null,contentValues);
        db.close();

        if(result == -1)
            return false;
        else
            return true;

    }


    public List<Account> getAllAccount(){

        SQLiteDatabase db = this.getReadableDatabase();
        List<Account>listAccountBank = new ArrayList<Account>();
        String selectQuerry = "SELECT * FROM "+TABLE_NAME_BANK;
        Cursor res = db.rawQuery(selectQuerry,null);

        if(res.moveToFirst()){
            do{
                Account account = new Account();
                account.setID(Integer.parseInt(res.getString(0)));
                account.setNameBank(res.getString(1));

                listAccountBank.add(account);
            }while(res.moveToNext());
        }

        return listAccountBank;
    }

}

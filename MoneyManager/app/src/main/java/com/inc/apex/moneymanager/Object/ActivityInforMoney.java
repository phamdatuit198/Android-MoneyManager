package com.inc.apex.moneymanager.Object;

/**
 * Created by Dat.Pham on 8/16/2017.
 */

public class ActivityInforMoney {

    private int ID;
    private String Date;
    private String Account;
    private String Category;
    private String Amount;
    private String Contents;
    private int Type;

    public int getID() {
        return ID;
    }

    public String getDate() {
        return Date;
    }

    public String getAccount() {
        return Account;
    }

    public String getCategory() {
        return Category;
    }

    public String getAmount() {
        return Amount;
    }

    public String getContents() {
        return Contents;
    }

    public int getType() {
        return Type;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public void setContents(String contents) {
        Contents = contents;
    }

    public void setType(int type) {
        Type = type;
    }
}

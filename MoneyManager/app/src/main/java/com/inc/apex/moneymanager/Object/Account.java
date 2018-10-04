package com.inc.apex.moneymanager.Object;

/**
 * Created by Dat.Pham on 10/9/2017.
 */

public class Account {

    private int ID;
    private String nameBank;

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setNameBank(String nameBank) {
        this.nameBank = nameBank;
    }

    public int getID() {
        return ID;
    }

    public String getNameBank() {
        return nameBank;
    }
}

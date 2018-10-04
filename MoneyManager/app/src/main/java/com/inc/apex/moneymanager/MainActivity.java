package com.inc.apex.moneymanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.ads.MobileAds;
import com.inc.apex.moneymanager.Unit.Common;
import com.inc.apex.moneymanager.Unit.Constant;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();
    }

    private void initialization(){

        Common.setupCommon(this);
        MobileAds.initialize(this, Constant.ADMOB_ID);


        Intent intentMoney = new Intent(this, MoneyActivity.class);
        startActivity(intentMoney);
        finish();
    }
}

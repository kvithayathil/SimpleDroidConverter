package com.jedikv.simpleconverter.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jedikv.simpleconverter.App;
import com.jedikv.simpleconverter.dbutils.ConversionItemDbHelper;
import com.jedikv.simpleconverter.dbutils.CurrencyDbHelper;
import com.jedikv.simpleconverter.dbutils.CurrencyPairDbHelper;

import javax.inject.Inject;

import icepick.Icepick;

/**
 * Created by Kurian on 08/05/2015.
 */
public class BaseActivity extends AppCompatActivity {

    @Inject
    protected CurrencyDbHelper mCurrencyEntityHelper;
    @Inject
    protected CurrencyPairDbHelper mCurrencyPairEntityHelper;
    @Inject
    protected ConversionItemDbHelper mConversionEntityHelper;

    @Inject SharedPreferences mSharedPrefs;

    @Override
    protected void onStart() {
        super.onStart();
        App.getBusInstance().register(this);
    }

    @Override
    protected void onStop() {
        App.getBusInstance().unregister(this);
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);

        ((App)getApplication()).getAppComponent().inject(this);

        //mCurrencyEntityHelper = new CurrencyDbHelper(this);
        //mCurrencyPairEntityHelper = new CurrencyPairDbHelper(this);
        //mConversionEntityHelper = new ConversionItemDbHelper(this);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    protected int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
    }


    protected SharedPreferences getDefaultSharedPrefs() {

        return mSharedPrefs;
    }

    protected CurrencyDbHelper getCurrencyDbHelper() {
        return mCurrencyEntityHelper;
    }

    protected CurrencyPairDbHelper getPairDbHelper() { return mCurrencyPairEntityHelper; }

    public ConversionItemDbHelper getConversionEntityHelper() {
        return mConversionEntityHelper;
    }
}

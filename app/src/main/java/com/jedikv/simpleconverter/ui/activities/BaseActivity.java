package com.jedikv.simpleconverter.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jedikv.simpleconverter.App;
import com.jedikv.simpleconverter.AppComponent;

import javax.inject.Inject;

import icepick.Icepick;

/**
 * Created by Kurian on 08/05/2015.
 */
public class BaseActivity extends AppCompatActivity {

    @Inject SharedPreferences sharedPrefs;

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

        this.sharedPrefs = ((App)getApplication())
                .getAppComponent().providesSharedPrefs();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    public AppComponent getApplicationComponent() {
        return App.get(this).getAppComponent();
    }

    protected SharedPreferences getDefaultSharedPrefs() {
        return this.sharedPrefs;
    }
}

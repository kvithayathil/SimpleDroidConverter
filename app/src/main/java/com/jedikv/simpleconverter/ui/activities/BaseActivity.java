package com.jedikv.simpleconverter.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jedikv.simpleconverter.App;

import icepick.Icepick;

/**
 * Created by Kurian on 08/05/2015.
 */
public class BaseActivity extends AppCompatActivity {

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

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }
}

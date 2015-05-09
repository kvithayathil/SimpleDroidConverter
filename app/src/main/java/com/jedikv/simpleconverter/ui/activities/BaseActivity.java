package com.jedikv.simpleconverter.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import icepick.Icepick;

/**
 * Created by Kurian on 08/05/2015.
 */
public class BaseActivity extends AppCompatActivity {

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

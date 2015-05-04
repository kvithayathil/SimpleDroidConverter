package com.jedikv.simpleconverter.ui.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.Menu;
import android.view.MenuItem;

import com.jedikv.simpleconverter.R;
import com.jedikv.simpleconverter.intentsevice.CurrencyUpdateIntentService;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.et_input)
    AppCompatEditText etInput;
    @InjectView(R.id.btn_update_currency)
    AppCompatButton btnDownload;
    @InjectView(R.id.tv_currency_code)
    AppCompatTextView tvCurrencyCode;
    @InjectView(R.id.tv_currency_symbol)
    AppCompatTextView tvCurrencySymbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

    }


    @OnClick(R.id.btn_update_currency)
    public void downloadCurrency() {

        ArrayList<String> list = new ArrayList<>(Arrays.asList("GBP", "CHF"));

        CurrencyUpdateIntentService.startService(this, list, "USD");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

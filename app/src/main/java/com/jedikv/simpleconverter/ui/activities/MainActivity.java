package com.jedikv.simpleconverter.ui.activities;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jedikv.simpleconverter.App;
import com.jedikv.simpleconverter.R;
import com.jedikv.simpleconverter.busevents.CurrencyUpdateEvent;
import com.jedikv.simpleconverter.intentsevice.CurrencyUpdateIntentService;
import com.jedikv.simpleconverter.ui.adapters.CurrencyAdapter;
import com.squareup.otto.Subscribe;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import timber.log.Timber;


public class MainActivity extends BaseActivity {

    @InjectView(R.id.et_input)
    AppCompatEditText etInput;
    @InjectView(R.id.btn_update_currency)
    AppCompatButton btnDownload;
    @InjectView(R.id.tv_currency_code)
    AppCompatTextView tvCurrencyCode;
    @InjectView(R.id.tv_currency_symbol)
    AppCompatTextView tvCurrencySymbol;
    @InjectView(R.id.rl_container)
    RelativeLayout rlContainer;
    @InjectView(R.id.list)
    RecyclerView recyclerView;

    private CurrencyAdapter mCurrencyAdapter;

    private boolean mInputFocus = false;
    private String mInputedValueString;
    private final DecimalFormat mDecimalFormat = new DecimalFormat("#0.0000", new DecimalFormatSymbols(Locale.getDefault()));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mDecimalFormat.setParseBigDecimal(true);
        mDecimalFormat.setMinimumFractionDigits(4);

        mCurrencyAdapter = new CurrencyAdapter(App.get(this));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mCurrencyAdapter);

        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    convertValue(etInput.getText().toString());
                    dismissScreenKeyboard(etInput);
                    return true;
                }

                return false;
            }
        });

        etInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus) {
                    convertValue(etInput.getText().toString());
                    dismissScreenKeyboard(etInput);
                }
            }
        });

        rlContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dismissScreenKeyboard(etInput);
                return true;
            }
        });
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

    private void convertValue(String entry) {

        mInputedValueString = entry;

        try {
            BigDecimal enteredValue = (BigDecimal)mDecimalFormat.parse(entry);
            etInput.setText(mDecimalFormat.format(enteredValue.doubleValue()));
            mCurrencyAdapter.updateCurrencyTargets("USD", enteredValue);

            downloadCurrency();

        } catch (NumberFormatException e) {
            Timber.e(e, e.getMessage());

        } catch (ParseException e) {
            Timber.e(e, e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Dismiss the on screen keyboard and clear focus from an edittext
     * @param editText
     */
    private void dismissScreenKeyboard(EditText editText) {

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        editText.clearFocus();
    }

    @Subscribe
    public void updateCurrencyEvent(CurrencyUpdateEvent event) {

        mCurrencyAdapter.notifyDataSetChanged();
    }
}

package com.jedikv.simpleconverter.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jedikv.simpleconverter.App;
import com.jedikv.simpleconverter.R;
import com.jedikv.simpleconverter.busevents.CurrencyUpdateEvent;
import com.jedikv.simpleconverter.dbutils.CurrencyDbHelper;
import com.jedikv.simpleconverter.intentsevice.CurrencyUpdateIntentService;
import com.jedikv.simpleconverter.ui.adapters.CurrencyConversionsAdapter;
import com.jedikv.simpleconverter.utils.Constants;
import com.jedikv.simpleconverter.utils.ConversionUtils;
import com.melnykov.fab.FloatingActionButton;
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
import converter_db.CurrencyEntity;
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
    @InjectView(R.id.fab)
    FloatingActionButton floatingActionButton;
    @InjectView(R.id.ib_flag)
    ImageButton ibFlag;

    @InjectView(R.id.toolbar)
    Toolbar toolBar;

    private CurrencyConversionsAdapter mCurrencyConversionsAdapter;

    private boolean mInputFocus = false;
    private String mInputedValueString;
    private final DecimalFormat mDecimalFormat = new DecimalFormat("#0.0000", new DecimalFormatSymbols(Locale.getDefault()));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        setSupportActionBar(toolBar);
        mDecimalFormat.setParseBigDecimal(true);
        mDecimalFormat.setMinimumFractionDigits(4);

        mCurrencyConversionsAdapter = new CurrencyConversionsAdapter(App.get(this));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mCurrencyConversionsAdapter);

        floatingActionButton.attachToRecyclerView(recyclerView);

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

    @OnClick(R.id.fab)
    public void addCurrency() {

        startCurrencyPicker(CurrencyPickerActivity.REQUEST_CODE_ADD_CURRENCY, new ArrayList<>(Arrays.asList("GBP", "CHF")));
    }

    @OnClick(R.id.ib_flag)
    public void changeSourceCurrency() {

        startCurrencyPicker(CurrencyPickerActivity.REQUEST_CODE_CHANGE_CURRENCY, new ArrayList<>(Arrays.asList(getSourceCurrency())));
    }

    @OnClick(R.id.btn_update_currency)
    public void downloadCurrency() {

        ArrayList<String> list = new ArrayList<>(Arrays.asList("GBP", "CHF"));

        CurrencyUpdateIntentService.startService(this, list, getSourceCurrency());
    }

    private void startCurrencyPicker(int requestCode, ArrayList<String> currencyArray) {

        Bundle bundle = new Bundle();
        bundle.putStringArrayList(CurrencyPickerActivity.EXTRA_CURRENCY_LIST, new ArrayList<>(Arrays.asList(new String[]{"USD"})));

        Intent pickCurrencyIntent = new Intent(this, CurrencyPickerActivity.class);
        pickCurrencyIntent.putExtras(bundle);
        startActivityForResult(pickCurrencyIntent, requestCode);
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
            mCurrencyConversionsAdapter.updateCurrencyTargets("USD", enteredValue);

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

    @Override
    protected void onResume() {
        super.onResume();
        updateSourceCurrencyUI();
    }

    @Subscribe
    public void updateCurrencyEvent(CurrencyUpdateEvent event) {

        mCurrencyConversionsAdapter.notifyDataSetChanged();
    }


    private void updateSourceCurrencyUI() {

        CurrencyEntity entity = getCurrencyDbHelper().getCurrency(getSourceCurrency());

        String countryCode = entity.getCode().substring(0,2).toLowerCase();
        Timber.d("Country code: " + countryCode);

        final int flagId = ConversionUtils.getDrawableResId(this, entity.getCode().substring(0,2).toLowerCase() + "_");
        ibFlag.setImageResource(flagId);
        tvCurrencyCode.setText(entity.getCode());
        tvCurrencySymbol.setText(entity.getSymbol());

    }

    public String getSourceCurrency() {
        return getDefaultSharedPrefs().getString(Constants.PREFS_CURRENTLY_SELECTED_CURRENCY, getString(R.string.default_source_currency));
    }

    public void updateSourceCurrency(String currencyCode) {

        if(getDefaultSharedPrefs().edit().putString(Constants.PREFS_CURRENTLY_SELECTED_CURRENCY, currencyCode).commit()) {
            updateSourceCurrencyUI();
            downloadCurrency();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == CurrencyPickerActivity.RESULT_CODE_SUCCESS) {

            String currencyCode = data.getStringExtra(CurrencyPickerActivity.EXTRA_SELECTED_CURRENCY_CODE);

            switch (requestCode) {

                case CurrencyPickerActivity.REQUEST_CODE_ADD_CURRENCY: {

                    break;
                }

                case CurrencyPickerActivity.REQUEST_CODE_CHANGE_CURRENCY: {
                    updateSourceCurrency(currencyCode);
                    break;
                }
            }
        }
    }
}

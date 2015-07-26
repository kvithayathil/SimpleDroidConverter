package com.jedikv.simpleconverter.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.jedikv.simpleconverter.api.YahooCurrencyDownloadService;
import com.jedikv.simpleconverter.busevents.CurrencyUpdateEvent;
import com.jedikv.simpleconverter.busevents.RemoveConversionEvent;
import com.jedikv.simpleconverter.ui.adapters.CurrencyConversionsAdapter;
import com.jedikv.simpleconverter.utils.AndroidUtils;
import com.jedikv.simpleconverter.utils.Constants;
import com.melnykov.fab.FloatingActionButton;
import com.squareup.otto.Subscribe;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import converter_db.ConversionItem;
import converter_db.CurrencyEntity;
import converter_db.CurrencyPairEntity;
import icepick.Icicle;
import rx.Subscriber;
import timber.log.Timber;


public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.et_input)
    AppCompatEditText etInput;
    @Bind(R.id.tv_currency_code)
    AppCompatTextView tvCurrencyCode;
    @Bind(R.id.tv_currency_symbol)
    AppCompatTextView tvCurrencySymbol;
    @Bind(R.id.rl_container)
    RelativeLayout rlContainer;
    @Bind(R.id.list)
    RecyclerView recyclerView;
    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;
    @Bind(R.id.ib_flag)
    ImageButton ibFlag;

    @Bind(R.id.toolbar)
    Toolbar toolBar;

    private boolean mIsWatching = true;

    private CurrencyConversionsAdapter mCurrencyConversionsAdapter;

    private boolean mInputFocus = false;


    @Icicle
    String mInputedValueString;

    private final DecimalFormat mDecimalFormat = new DecimalFormat("#0.0000", new DecimalFormatSymbols(Locale.getDefault()));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag(TAG);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolBar);

        mDecimalFormat.setParseBigDecimal(true);
        mDecimalFormat.setMinimumFractionDigits(4);
        mCurrencyConversionsAdapter = new CurrencyConversionsAdapter(App.get(this), recyclerView, getSourceCurrency());


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


        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Only do live updates when the edittext is focused
                if (mIsWatching) {
                    convertValue(s.toString());
                }
            }
        });

        Timber.d("Cached input value: " + mInputedValueString);

        if(savedInstanceState == null) {
            mInputedValueString = getDefaultSharedPrefs().getString(Constants.PREFS_CACHED_SAVED_INPUT_VALUE, "0");
        }

        convertValue(mInputedValueString);


        rlContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dismissScreenKeyboard(etInput);
                return true;
            }
        });

    }

    @OnFocusChange(R.id.et_input)
    public void onInputFocus(boolean focused) {

        Timber.d("On input focus: " + focused);
        mIsWatching = focused;
        if(!focused) {
            convertValue(etInput.getText().toString());
            dismissScreenKeyboard(etInput);
        }
    }

    @OnClick(R.id.fab)
    public void addCurrency() {

        startCurrencyPicker(CurrencyPickerActivity.REQUEST_CODE_ADD_CURRENCY, mCurrencyConversionsAdapter.getSelectedCurrencyCodeList());
    }

    @OnClick(R.id.ib_flag)
    public void changeSourceCurrency() {

        startCurrencyPicker(CurrencyPickerActivity.REQUEST_CODE_CHANGE_CURRENCY, new ArrayList<>(Arrays.asList(getSourceCurrency())));
    }

    public void downloadCurrency(List<String> currencyList) {
       // CurrencyUpdateIntentService.startService(this, currencyList, getSourceCurrency());
        if(!currencyList.isEmpty()) {
            currencyDownloadService.executeRequest(currencyList, getSourceCurrency());
        }
    }

    private void startCurrencyPicker(int requestCode, ArrayList<String> currencyArray) {

        Bundle bundle = new Bundle();
        bundle.putStringArrayList(CurrencyPickerActivity.EXTRA_CURRENCY_LIST, currencyArray);

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

        if(!TextUtils.isEmpty(entry)) {
            mInputedValueString = entry;
        } else {
            mInputedValueString = "0";
        }
        try {
            BigDecimal enteredValue = (BigDecimal)mDecimalFormat.parse(mInputedValueString);
            //etInput.setText(mDecimalFormat.format(enteredValue.doubleValue()));
            mCurrencyConversionsAdapter.updateCurrencyTargets(getSourceCurrency(), enteredValue);

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
        mIsWatching = false;
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

        BigDecimal inputInt =  mCurrencyConversionsAdapter.getInputValue();
        inputInt.setScale(4, BigDecimal.ROUND_HALF_UP);
        //BigDecimal inputDecimal = inputInt.divide(new BigDecimal(10000));

        editText.setText(mDecimalFormat.format(inputInt.doubleValue()));
        editText.clearFocus();

        downloadCurrency(mCurrencyConversionsAdapter.getSelectedCurrencyCodeList());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSourceCurrencyUI();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //Cache any input values for re-use when relaunching the application
        getDefaultSharedPrefs().edit().putString(Constants.PREFS_CACHED_SAVED_INPUT_VALUE, etInput.getText().toString()).apply();
    }

    @Subscribe
    public void updateCurrencyEvent(CurrencyUpdateEvent event) {
        convertValue(etInput.getText().toString());
    }

    @Subscribe
    public void removeItemEvent(RemoveConversionEvent event) {
        mCurrencyConversionsAdapter.removeItem(event.getPosition());

        //Show the fab to address glitch where it won't come back up mid list when deleting any item
        if(!floatingActionButton.isVisible()) {
            floatingActionButton.show();
        }
    }


    private void updateSourceCurrencyUI() {

        CurrencyEntity entity = getCurrencyDbHelper().getCurrency(getSourceCurrency());

        String countryCode = entity.getCode().substring(0,2).toLowerCase();
        Timber.d("Country code: " + countryCode);

        final int flagId = AndroidUtils.getDrawableResIdByCurrencyCode(this, entity.getCode());
        ibFlag.setImageResource(flagId);
        tvCurrencyCode.setText(entity.getCode());
        tvCurrencySymbol.setText(entity.getSymbol());

        convertValue(etInput.getText().toString());
    }

    public String getSourceCurrency() {
        return getDefaultSharedPrefs().getString(Constants.PREFS_CURRENTLY_SELECTED_CURRENCY, getString(R.string.default_source_currency));
    }

    public void updateSourceCurrency(long currencyCode) {

        CurrencyEntity currencyEntity = mCurrencyEntityHelper.getById(currencyCode);

        if(getDefaultSharedPrefs().edit().putString(Constants.PREFS_CURRENTLY_SELECTED_CURRENCY, currencyEntity.getCode()).commit()) {
            updateSourceCurrencyUI();
            downloadCurrency(mCurrencyConversionsAdapter.getSelectedCurrencyCodeList());

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == CurrencyPickerActivity.RESULT_CODE_SUCCESS) {

            long currencyCode = data.getLongExtra(CurrencyPickerActivity.EXTRA_SELECTED_CURRENCY_CODE, -1);
            Timber.d("Result currency code: " + currencyCode);
            switch (requestCode) {

                case CurrencyPickerActivity.REQUEST_CODE_ADD_CURRENCY: {

                    addCurrencyToList(currencyCode);
                    break;
                }

                case CurrencyPickerActivity.REQUEST_CODE_CHANGE_CURRENCY: {
                    updateSourceCurrency(currencyCode);
                    break;
                }
            }
        }
    }

    private void addCurrencyToList(long currencyCode) {

        CurrencyEntity sourceCurrencyEntity = getCurrencyDbHelper().getCurrency(getSourceCurrency());
        CurrencyEntity targetCurrencyEntity = getCurrencyDbHelper().getById(currencyCode);

        CurrencyPairEntity entity = getPairDbHelper().getCurrencyPair(sourceCurrencyEntity.getNumericCode(), currencyCode);

        if(entity == null) {

            Timber.d("New currency pair entry");
            //Create a dummy pair for now till it's updated over the web
            entity = new CurrencyPairEntity();
            entity.setSource_id(sourceCurrencyEntity);
            entity.setTarget_id(targetCurrencyEntity);
            entity.setRate(0);
            entity.setCreated_date(new Date());
            long id = getPairDbHelper().insertOrUpdate(entity);
            entity.setId(id);
        }

        ConversionItem conversionItem = new ConversionItem();
        conversionItem.setCurrencyPairEntity(entity);
        conversionItem.setPosition(mCurrencyConversionsAdapter.getItemCount());
        long conversionId = getConversionEntityHelper().insertOrUpdate(conversionItem);
        conversionItem.setId(conversionId);
        mCurrencyConversionsAdapter.addItem(conversionItem);
        convertValue(etInput.getText().toString());

        //Update currency at the end
        downloadCurrency(Arrays.asList(targetCurrencyEntity.getCode()));

    }
}

package com.jedikv.simpleconverter.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jedikv.simpleconverter.App;
import com.jedikv.simpleconverter.R;
import com.jedikv.simpleconverter.busevents.CurrencyUpdateEvent;
import com.jedikv.simpleconverter.busevents.RemoveConversionEvent;
import com.jedikv.simpleconverter.presenters.ConversionPresenter;
import com.jedikv.simpleconverter.ui.adapters.CurrencyConversionsAdapter;
import com.jedikv.simpleconverter.ui.adapters.gestures.CurrencyTouchItemCallback;
import com.jedikv.simpleconverter.ui.views.CurrencyInputView;
import com.jedikv.simpleconverter.ui.views.IConversionView;
import com.jedikv.simpleconverter.utils.Constants;
import com.squareup.otto.Subscribe;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import converter_db.ConversionItem;
import converter_db.CurrencyEntity;
import converter_db.CurrencyPairEntity;
import icepick.Icicle;
import timber.log.Timber;


public class MainActivity extends BaseActivity implements IConversionView {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.currency_input)
    CurrencyInputView currencyInputView;
    @Bind(R.id.rl_container)
    RelativeLayout rlContainer;
    @Bind(R.id.list)
    RecyclerView recyclerView;
    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout parent;
    @Bind(R.id.toolbar)
    Toolbar toolBar;


    @Inject
    ConversionPresenter conversionPresenter;

    private CurrencyConversionsAdapter mCurrencyConversionsAdapter;

    @Icicle
    String mInputedValueString;

    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();

    //private final DecimalFormat mDecimalFormat = new DecimalFormat("#0.0000", new DecimalFormatSymbols(Locale.getDefault()));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag(TAG);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolBar);
        getApplicationComponent().inject(this);
        conversionPresenter.attachView(this);

        mCurrencyConversionsAdapter = new CurrencyConversionsAdapter(App.get(this), parent, getCurrentSourceCurrency());


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mCurrencyConversionsAdapter);


        setUpTouchGestures();



        currencyInputView.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                    case EditorInfo.IME_NULL:
                    case KeyEvent.KEYCODE_ENTER:
                        currencyInputView.dismissKeyboard();
                        conversionPresenter.convertValue(currencyInputView.getInputValue());
                        conversionPresenter.downloadCurrency(mCurrencyConversionsAdapter.getSelectedCurrencyCodeList());
                        return true;

                    default:
                        return false;
                }

            }
        });


        currencyInputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    conversionPresenter.convertValue(s.toString());
            }
        });


        mInputedValueString = getDefaultSharedPrefs().getString(Constants.PREFS_CACHED_SAVED_INPUT_VALUE, "0.00");
        Timber.d("Cached input value: " + mInputedValueString);

        currencyInputView.setValue(mInputedValueString);


        rlContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currencyInputView.dismissKeyboard();
                conversionPresenter.downloadCurrency(mCurrencyConversionsAdapter.getSelectedCurrencyCodeList());
                return true;
            }
        });

    }

    @Override
    protected void onDestroy() {
        conversionPresenter.detachView();
        super.onDestroy();
    }

    @OnClick(R.id.fab)
    public void addCurrency() {

        startCurrencyPicker(CurrencyPickerActivity.REQUEST_CODE_ADD_CURRENCY, mCurrencyConversionsAdapter.getSelectedCurrencyCodeList());
    }

    @OnClick(R.id.ib_flag)
    public void changeSourceCurrency() {

        startCurrencyPicker(CurrencyPickerActivity.REQUEST_CODE_CHANGE_CURRENCY, new ArrayList<>(Arrays.asList(getCurrentSourceCurrency())));
    }

    private void setUpTouchGestures() {

        ItemTouchHelper touchHelper = new ItemTouchHelper(new CurrencyTouchItemCallback(mCurrencyConversionsAdapter));
        touchHelper.attachToRecyclerView(recyclerView);
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


    /*
    private void convertValue(String entry) {

        if(!TextUtils.isEmpty(entry)) {
            mInputedValueString = entry;
        } else {
            mInputedValueString = "0";
        }
        try {
            BigDecimal enteredValue = (BigDecimal)mDecimalFormat.parse(mInputedValueString);
            mCurrencyConversionsAdapter.updateCurrencyTargets(getSourceCurrency(), enteredValue);

        } catch (NumberFormatException e) {
            Timber.e(e, e.getMessage());

        } catch (ParseException e) {
            Timber.e(e, e.getMessage());
            e.printStackTrace();
        }
    }
    */

    @Override
    protected void onResume() {
        super.onResume();
        updateViews();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //Cache any input values for re-use when relaunching the application
        getDefaultSharedPrefs().edit().putString(Constants.PREFS_CACHED_SAVED_INPUT_VALUE, currencyInputView.getInputValue()).apply();
    }

    @Subscribe
    public void updateCurrencyEvent(CurrencyUpdateEvent event) {
        conversionPresenter.convertValue(currencyInputView.getInputValue());
    }

    @Subscribe
    public void removeItemEvent(RemoveConversionEvent event) {
        currencyInputView.dismissKeyboard();
        mCurrencyConversionsAdapter.removeItem(event.getPosition());
    }



    public void updateSourceCurrency(long currencyCode) {

        CurrencyEntity currencyEntity = mCurrencyEntityHelper.getById(currencyCode);
        Timber.d("id: " + currencyCode + " code: " + currencyEntity.getCode());

        if(getDefaultSharedPrefs().edit().putString(Constants.PREFS_CURRENTLY_SELECTED_CURRENCY, currencyEntity.getCode()).commit()) {
            getDefaultSharedPrefs().edit().putLong(Constants.PREFS_CURRENTLY_SELECTED_CURRENCY_CODE, currencyCode).commit();
            updateViews();
        }

        conversionPresenter.updateFromSourceCurrency(currencyCode);

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
        conversionPresenter.addCurrency(getCurrentSourceCurrencyCode(), currencyCode);
    }

    @Override
    public String getCurrentSourceCurrency() {
        return getDefaultSharedPrefs().getString(Constants.PREFS_CURRENTLY_SELECTED_CURRENCY, getString(R.string.default_source_currency));

    }

    @Override
    public long getCurrentSourceCurrencyCode() {
        return getDefaultSharedPrefs().getLong(Constants.PREFS_CURRENTLY_SELECTED_CURRENCY_CODE, R.integer.default_source_currency_code);
    }

    @Override
    public int getListSize() {
        return mCurrencyConversionsAdapter.getItemCount();
    }

    @Override
    public void insertConversionItem(ConversionItem conversionItem) {
        mCurrencyConversionsAdapter.addItem(conversionItem);
        conversionPresenter.convertValue(currencyInputView.getInputValue());

    }

    @Override
    public void updateViews() {
        currencyInputView.setValue(getDefaultSharedPrefs().getString(Constants.PREFS_CACHED_SAVED_INPUT_VALUE, "0.0000"));
        CurrencyEntity entity = getCurrencyDbHelper().getCurrency(getCurrentSourceCurrency());
        currencyInputView.setCurrency(entity);
        conversionPresenter.convertValue(currencyInputView.getInputValue());
    }

    @Override
    public void updateList(BigDecimal inputValue) {
        mCurrencyConversionsAdapter.updateCurrencyTargets(getCurrentSourceCurrency(), inputValue);
    }


}

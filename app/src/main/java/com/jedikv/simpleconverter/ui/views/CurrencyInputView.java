package com.jedikv.simpleconverter.ui.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jedikv.simpleconverter.R;
import com.jedikv.simpleconverter.utils.AndroidUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnFocusChange;
import converter_db.CurrencyEntity;
import timber.log.Timber;

/**
 * Created by KV_87 on 01/08/15.
 */
public class CurrencyInputView extends LinearLayout {

    public static final String TAG = CurrencyInputView.class.getSimpleName();

    private final DecimalFormat decimalFormat = new DecimalFormat("#0.0000", new DecimalFormatSymbols(Locale.getDefault()));


    @BindView(R.id.et_input)
    AppCompatEditText input;
    @BindView(R.id.ib_flag)
    ImageButton flagSelect;
    @BindView(R.id.tv_currency_symbol)
    AppCompatTextView currencySymbol;
    @BindView(R.id.tv_currency_code)
    AppCompatTextView currencyCode;

    public CurrencyInputView(Context context) {
        this(context, null, 0);
    }

    public CurrencyInputView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CurrencyInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        Timber.tag(TAG);
        decimalFormat.setParseBigDecimal(true);
        decimalFormat.setMinimumFractionDigits(4);
        View view = LayoutInflater.from(context).inflate(R.layout.currency_input_layout, this);
        ButterKnife.bind(view);
    }


    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        flagSelect.setOnClickListener(l);
        currencyCode.setOnClickListener(l);
    }

    public void addTextChangedListener(@Nullable TextWatcher watcher) {
        input.addTextChangedListener(watcher);
    }

    public void setOnEditorActionListener(@Nullable TextView.OnEditorActionListener l) {
        input.setOnEditorActionListener(l);
    }

    public String getInputValue() {
        return input.getText().toString();
    }

    public void setValue(String value) {
        setValue(Double.valueOf(value));
    }

    public void setValue(double value) {
        String stringValue = decimalFormat.format(value);
        Timber.d("String value " + value);
        input.setText(stringValue);
    }

    public boolean isFocused() {
        return input.isFocused();
    }

    public void clearFocus() {
        input.clearFocus();
    }

    public void clearValue() {
        input.setText("");
    }

    public void setCurrency(@NonNull CurrencyEntity currency) {
        currencyCode.setText(currency.getCode());
        currencySymbol.setText(currency.getSymbol());

        String countryCode = currency.getCode().substring(0,2).toLowerCase();
        Timber.d("Country code: " + countryCode);

        final int flagId = AndroidUtils.getDrawableResIdByCurrencyCode(getContext(), currency.getCode());
        flagSelect.setImageResource(flagId);
    }

    public void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
        clearFocus();
    }

    @OnFocusChange(R.id.et_input)
    public void onInputFocus(boolean isFocused) {
        if(!isFocused) {
            setValue(input.getText().toString());
        }
    }

}

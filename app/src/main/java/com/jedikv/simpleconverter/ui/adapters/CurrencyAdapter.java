package com.jedikv.simpleconverter.ui.adapters;

import android.content.Context;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jedikv.simpleconverter.App;
import com.jedikv.simpleconverter.R;
import com.jedikv.simpleconverter.dbutils.CurrencyDbHelper;
import com.jedikv.simpleconverter.dbutils.CurrencyPairDbHelper;
import com.jedikv.simpleconverter.utils.ConversionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import converter_db.CurrencyEntity;
import converter_db.CurrencyPairEntity;
import timber.log.Timber;

/**
 * Created by Kurian on 08/05/2015.
 */
public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder> {

    private List<CurrencyPairEntity> mCurrencyPairList;
    private CurrencyPairDbHelper mCurrencyPairDbHelper;
    private CurrencyDbHelper mCurrencyDbHelper;

    private String mSourceCurrencyCode;

    private int mInputValue;


    public CurrencyAdapter(Context context) {
        Timber.tag(CurrencyAdapter.class.getSimpleName());
        mCurrencyPairDbHelper = new CurrencyPairDbHelper(App.get(context));
        mCurrencyDbHelper = new CurrencyDbHelper(App.get(context));
        mCurrencyPairList = new ArrayList<>();

    }

    public void updateCurrencyTargets(String currencyCode, BigDecimal inputValue) {

        mSourceCurrencyCode = currencyCode;
        mCurrencyPairList.clear();
        mCurrencyPairList.addAll(mCurrencyPairDbHelper.getCurrencyTargetList(currencyCode + "/"));

        //Ensure the value is converted to int to retain float values
        mInputValue = inputValue.multiply(new BigDecimal(10000)).intValue();

        Timber.d("Preinput: " + inputValue + " PostInput: " + mInputValue);
        notifyDataSetChanged();
    }

    @Override
    public CurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_list_card, parent, false);
        CurrencyViewHolder viewHolder = new CurrencyViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CurrencyViewHolder holder, int position) {

        CurrencyPairEntity pairEntity = mCurrencyPairList.get(position);
        String code = pairEntity.getPair().substring(4);

        CurrencyEntity currencyEntity = mCurrencyDbHelper.getCurrency(code);
        holder.bind(mInputValue, pairEntity, currencyEntity);
    }

    @Override
    public int getItemCount() {
        return mCurrencyPairList.size();
    }

    public static class CurrencyViewHolder extends RecyclerView.ViewHolder {

        private final DecimalFormat mDecimalFormat = new DecimalFormat("#0.0000", new DecimalFormatSymbols(Locale.getDefault()));


        @InjectView(R.id.iv_flag)
        ImageView ivFlag;
        @InjectView(R.id.tv_currency_name)
        AppCompatTextView tvCurrencyName;
        @InjectView(R.id.tv_value)
        AppCompatTextView tvValue;

        public CurrencyViewHolder(View v) {
            super(v);
            ButterKnife.inject(this, v);
            mDecimalFormat.setParseBigDecimal(true);
            mDecimalFormat.setMinimumFractionDigits(4);
        }


        public void bind(int value, CurrencyPairEntity currencyPairEntity, CurrencyEntity currencyEntity) {


            String code = currencyPairEntity.getPair().substring(4);
            setFlagDrawable(ivFlag.getContext(), code.substring(0,2).toLowerCase());

            tvCurrencyName.setText(currencyEntity.getName());
            tvValue.setText(currencyEntity.getSymbol() + "value");

            setValue(value, currencyPairEntity.getRate());
        }

        private void setFlagDrawable(Context context, String currencyCode) {

            Timber.d("Country Code: " + currencyCode);

            final int flagId = ConversionUtils.getDrawableResId(context, currencyCode + "_");
            //context.getResources().getIdentifier(currencyCode+ "_", "drawable", context.getPackageName());
            ivFlag.setImageResource(flagId);
        }

        public void setValue(int inputValue, int rate) {

            Timber.d("Input value: " + inputValue + " rate: " + rate);
            int result = inputValue * rate;

            BigDecimal intResult = new BigDecimal(result);
            //Revert the value back to the original decimal point position
            BigDecimal decimalResult = intResult.divide(new BigDecimal(10000 * 10000), 4, RoundingMode.HALF_UP);

            Timber.d("Result: " + result + " Converted result: " + decimalResult);

            tvValue.setText(decimalResult.toString());
        }

    }

}

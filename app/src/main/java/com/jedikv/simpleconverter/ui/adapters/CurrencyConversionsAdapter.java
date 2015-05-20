package com.jedikv.simpleconverter.ui.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.jedikv.simpleconverter.App;
import com.jedikv.simpleconverter.R;
import com.jedikv.simpleconverter.busevents.RemoveConversionEvent;
import com.jedikv.simpleconverter.dbutils.ConversionItemDbHelper;
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
import butterknife.OnClick;
import converter_db.ConversionEntity;
import converter_db.CurrencyEntity;
import converter_db.CurrencyPairEntity;
import timber.log.Timber;

/**
 * Created by Kurian on 08/05/2015.
 */
public class CurrencyConversionsAdapter extends RecyclerView.Adapter<CurrencyConversionsAdapter.CurrencyViewHolder> {

    private List<ConversionEntity> mConverterList;
    private CurrencyPairDbHelper mCurrencyPairDbHelper;
    private CurrencyDbHelper mCurrencyDbHelper;
    private ConversionItemDbHelper mConversionDbHelper;

    private String mSourceCurrencyCode;

    private long mInputValue;


    public CurrencyConversionsAdapter(Context context, String sourceCurrency) {
        Timber.tag(CurrencyConversionsAdapter.class.getSimpleName());
        mCurrencyPairDbHelper = new CurrencyPairDbHelper(context);
        mCurrencyDbHelper = new CurrencyDbHelper(context);
        mConversionDbHelper = new ConversionItemDbHelper(context);
        mConverterList = new ArrayList<>();
        mSourceCurrencyCode = sourceCurrency;

    }

    public void updateCurrencyTargets(String currencyCode, BigDecimal inputValue) {

        mSourceCurrencyCode = currencyCode;
        mConverterList.clear();
        mConverterList.addAll(mConversionDbHelper.getAll());

        //Ensure the value is converted to int to retain float values
        mInputValue = inputValue.multiply(new BigDecimal(10000)).longValue();

        Timber.d("Preinput: " + inputValue + " PostInput: " + mInputValue);
        notifyDataSetChanged();
    }

    public void onPostNetworkUpdate() {

        mConverterList.clear();
        mConverterList.addAll(mConversionDbHelper.getAll());
        notifyDataSetChanged();
    }

    public long getInputValue() {
        return mInputValue;
    }

    public ArrayList<String> getSelectedCurrencyCodeList() {

        ArrayList<String> codeList = new ArrayList<>();

        for(ConversionEntity entity : mConverterList) {

            codeList.add(entity.getCurrency_code().getCode());
        }

        return codeList;
    }

    @Override
    public CurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_list_card, parent, false);
        CurrencyViewHolder viewHolder = new CurrencyViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CurrencyViewHolder holder, int position) {

        ConversionEntity conversionEntity = mConverterList.get(position);
        CurrencyEntity currencyEntity = conversionEntity.getCurrency_code();


        //Keep the position in sync with the adapter
        if(conversionEntity.getPosition() != position) {
            conversionEntity.setPosition(position);
            mConversionDbHelper.update(conversionEntity);
        }

        CurrencyPairEntity pairEntity = mCurrencyPairDbHelper.getGetPairByCodes(mSourceCurrencyCode, currencyEntity.getCode());

        holder.bind(mInputValue, pairEntity, currencyEntity);
    }

    @Override
    public int getItemCount() {
        return mConverterList.size();
    }

    public List<ConversionEntity> getItems() {
        return mConverterList;
    }

    public void addItem(ConversionEntity conversionEntity) {

        if(mConverterList.add(conversionEntity)) {
            notifyItemInserted(mConverterList.size() - 1);
        }
    }

    public void addItemAtPosition(int position, ConversionEntity conversionEntity) {

        mConverterList.add(position, conversionEntity);
        notifyItemInserted(position);
    }

    public ConversionEntity removeItem(int position) {

        ConversionEntity entity = mConverterList.remove(position);
        mConversionDbHelper.deleteItem(entity);
        notifyItemRemoved(position);
        return entity;
    }



    public static class CurrencyViewHolder extends RecyclerView.ViewHolder {

        private final DecimalFormat mDecimalFormat = new DecimalFormat("#0.0000", new DecimalFormatSymbols(Locale.getDefault()));


        @InjectView(R.id.iv_flag)
        ImageView ivFlag;
        @InjectView(R.id.tv_currency_name)
        AppCompatTextView tvCurrencyName;
        @InjectView(R.id.tv_value)
        AppCompatTextView tvValue;
        @InjectView(R.id.ib_remove)
        ImageButton ibRemove;
        @InjectView(R.id.tv_currency_code)
        AppCompatTextView tvCurrencyCode;

        public CurrencyViewHolder(View v) {
            super(v);
            ButterKnife.inject(this, v);
            mDecimalFormat.setParseBigDecimal(true);
            mDecimalFormat.setMinimumFractionDigits(4);
        }


        public void bind(long value, CurrencyPairEntity currencyPairEntity, CurrencyEntity currencyEntity) {


            String code = currencyEntity.getCode();
            setFlagDrawable(ivFlag.getContext(), code.substring(0, 2).toLowerCase());

            tvCurrencyCode.setText(code);
            tvCurrencyName.setText(currencyEntity.getName());
            setValue(value, currencyPairEntity.getRate());
        }

        private void setFlagDrawable(Context context, String currencyCode) {

            Timber.d("Country Code: " + currencyCode);

            final int flagId = ConversionUtils.getDrawableResId(context, currencyCode + "_");
            //context.getResources().getIdentifier(currencyCode+ "_", "drawable", context.getPackageName());
            ivFlag.setImageResource(flagId);
        }

        public void setValue(long inputValue, long rate) {

            Timber.d("Input value: " + inputValue + " rate: " + rate);
            long result = inputValue * rate;

            BigDecimal intResult = new BigDecimal(result);
            //Revert the value back to the original decimal point position
            BigDecimal decimalResult = intResult.divide(new BigDecimal(10000 * 10000), 4, RoundingMode.HALF_UP);

            Timber.d("Result: " + result + " Converted result: " + decimalResult);

            tvValue.setText(decimalResult.toString());
        }

        @OnClick(R.id.ib_remove)
        public void removeItem() {

            Timber.d("Remove adapter position: " + getAdapterPosition() + " Layout position: " + getLayoutPosition() + " Old position: " + getOldPosition());

            App.getBusInstance().post(new RemoveConversionEvent(getAdapterPosition()));
        }

    }

}

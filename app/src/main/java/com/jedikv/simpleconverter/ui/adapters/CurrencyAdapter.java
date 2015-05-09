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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
        CurrencyEntity currencyEntity = mCurrencyDbHelper.getCurrency(mSourceCurrencyCode);
        holder.bind(pairEntity, currencyEntity);
    }

    @Override
    public int getItemCount() {
        return mCurrencyPairList.size();
    }

    public static class CurrencyViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.iv_flag)
        ImageView ivFlag;
        @InjectView(R.id.tv_currency_name)
        AppCompatTextView tvCurrencyName;
        @InjectView(R.id.tv_value)
        AppCompatTextView tvValue;

        public CurrencyViewHolder(View v) {
            super(v);
            ButterKnife.inject(this, v);
        }


        public void bind(CurrencyPairEntity currencyPairEntity, CurrencyEntity currencyEntity) {


            String code = currencyPairEntity.getPair().substring(4);
            setFlagDrawable(ivFlag.getContext(), code);

            tvCurrencyName.setText(currencyEntity.getName());
            tvValue.setText(currencyEntity.getSymbol() + "value");


        }

        private void setFlagDrawable(Context context, String currencyCode) {

            final int flagId = context.getResources().getIdentifier(currencyCode, "drawable", context.getPackageName());
            ivFlag.setImageResource(flagId);
        }

        public void setValue(int inputValue, int rate) {

            int result = inputValue * rate;

        }

    }

}

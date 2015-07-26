package com.jedikv.simpleconverter.ui.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.jedikv.simpleconverter.App;
import com.jedikv.simpleconverter.R;
import com.jedikv.simpleconverter.busevents.AddCurrencyEvent;
import com.jedikv.simpleconverter.dbutils.CurrencyDbHelper;
import com.jedikv.simpleconverter.utils.AndroidUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import converter_db.CurrencyEntity;
import timber.log.Timber;

/**
 * Created by Kurian on 13/05/2015.
 */
public class CurrencyPickerAdapter extends RecyclerView.Adapter<CurrencyPickerAdapter.CurrencyItemViewHolder> implements Filterable {

    private final List<CurrencyEntity> mCurrencyList;
    private List<CurrencyEntity> mFilteredList;
    private CurrencyFilter mCurrencyFilter;
    private CurrencyDbHelper currencyDbHelper;

    public CurrencyPickerAdapter(Context context, List<String> currencyCodesToFilter) {
        currencyDbHelper = new CurrencyDbHelper(context);
        mCurrencyList = currencyDbHelper.getFilteredCurrencies(currencyCodesToFilter);
        mFilteredList = mCurrencyList;

        getFilter();
    }

    @Override
    public CurrencyItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_picker_card, parent, false);
        CurrencyItemViewHolder viewHolder = new CurrencyItemViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CurrencyItemViewHolder holder, int position) {

        CurrencyEntity entity = mFilteredList.get(position);
        holder.bind(entity);
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return mFilteredList.get(position).getNumericCode();
    }

    public static class CurrencyItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.iv_flag)
        ImageView ivFlag;
        @Bind(R.id.tv_currency_code)
        AppCompatTextView tvCurrencyCode;
        @Bind(R.id.tv_currency_name)
        AppCompatTextView tvCurrencyName;
        @Bind(R.id.tv_currency_symbol)
        AppCompatTextView tvCurrencySymbol;

        private CurrencyEntity currencyEntity;

        public CurrencyItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
        }

        public void bind(CurrencyEntity currencyEntity) {

            this.currencyEntity = currencyEntity;

            final int flagId = AndroidUtils.getDrawableResIdByCurrencyCode(ivFlag.getContext(), currencyEntity.getCode());

            if(TextUtils.equals(currencyEntity.getCode(), "XCD")) {
                Timber.d("XCD: " + flagId);
            }

            ivFlag.setImageResource(flagId);

            tvCurrencyName.setText(currencyEntity.getName());
            tvCurrencyCode.setText(currencyEntity.getCode());

            //Some currency symbols are just the currency code, so we avoid having to print it twice
            if(!TextUtils.equals(currencyEntity.getCode(), currencyEntity.getSymbol())) {
                tvCurrencySymbol.setText(currencyEntity.getSymbol());
            } else {
                tvCurrencySymbol.setText("");
            }

        }

        @Override
        public void onClick(View v) {

            App.getBusInstance().post(new AddCurrencyEvent(currencyEntity));
        }

    }

    @Override
    public Filter getFilter() {

        if(mCurrencyFilter == null) {
            mCurrencyFilter = new CurrencyFilter();
        }

        return mCurrencyFilter;
    }


    private class CurrencyFilter extends Filter {


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults filterResults = new FilterResults();
            if(!TextUtils.isEmpty(constraint)) {
                ArrayList<CurrencyEntity> tempList = new ArrayList<>();

                for(CurrencyEntity currency:mCurrencyList) {

                    String content = currency.getCode() + " " + currency.getName() + " " + currency.getSymbol() + " " + currency.getCountryName();

                    if(content.toLowerCase().contains(constraint.toString().toLowerCase())) {

                        tempList.add(currency);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;

            } else {

                filterResults.count = mCurrencyList.size();
                filterResults.values = mCurrencyList;
            }

            return filterResults;
        }

        /**
         * Notify about filtered list to UI
         * @param constraint text
         * @param results filtered result
         */
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mFilteredList = (ArrayList<CurrencyEntity>)results.values;
            notifyDataSetChanged();
        }
    }
}

package com.jedikv.simpleconverter.ui.adapters;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.jedikv.simpleconverter.R;
import com.jedikv.simpleconverter.ui.model.CurrencyModel;
import com.jedikv.simpleconverter.utils.AndroidUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Kurian on 13/05/2015.
 */
public class CurrencyPickerAdapter
        extends RecyclerView.Adapter<CurrencyPickerAdapter.CurrencyItemViewHolder>
        implements Filterable {

    private List<CurrencyModel> currencyList;
    private List<CurrencyModel> filteredList;
    private CurrencyFilter currencyFilter;

    private CurrencyListener listener;

    public CurrencyPickerAdapter() {
        currencyList = new ArrayList<>();
        filteredList = currencyList;
        getFilter();
    }

    @Override
    public CurrencyItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.currency_picker_card, parent, false);
        return new CurrencyItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CurrencyItemViewHolder holder, int position) {
        holder.bind(filteredList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void loadCurrencies(List<CurrencyModel> currencies) {
        this.currencyList = currencies;
        this.filteredList = currencyList;
        notifyDataSetChanged();
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return filteredList.get(position).numericCode();
    }

    public void setCurrencyListener(CurrencyListener listener) {
        this.listener = listener;
    }

    public interface CurrencyListener {
        void onItemClick(CurrencyModel currency);
    }

    public static class CurrencyItemViewHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_flag)
        ImageView ivFlag;
        @BindView(R.id.tv_currency_code)
        AppCompatTextView tvCurrencyCode;
        @BindView(R.id.tv_currency_name)
        AppCompatTextView tvCurrencyName;
        @BindView(R.id.tv_currency_symbol)
        AppCompatTextView tvCurrencySymbol;

        private CurrencyModel currency;
        private CurrencyListener listener;

        public CurrencyItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
        }

        public void bind(CurrencyModel currency, CurrencyListener l) {

            this.currency = currency;
            this.listener = l;

            final int flagId = AndroidUtils
                    .getDrawableResIdByCurrencyCode(ivFlag.getContext(), currency.isoCode());

            if(TextUtils.equals(currency.isoCode(), "XCD")) {
                Timber.d("XCD: " + flagId);
            }

            ivFlag.setImageResource(flagId);

            tvCurrencyName.setText(currency.name());
            tvCurrencyCode.setText(currency.isoCode());

            //Some currency symbols are just the currency code,
            //so we avoid having to print it twice
            if(!TextUtils.equals(currency.isoCode(), currency.symbol())) {
                tvCurrencySymbol.setText(currency.symbol());
            } else {
                tvCurrencySymbol.setText("");
            }
        }

        @Override
        public void onClick(View v) {
            if(listener != null) {
                listener.onItemClick(currency);
            }
        }

    }

    @Override
    public Filter getFilter() {
        if(currencyFilter == null) {
            currencyFilter = new CurrencyFilter();
        }
        return currencyFilter;
    }


    private class CurrencyFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults filterResults = new FilterResults();
            if(!TextUtils.isEmpty(constraint)) {
                ArrayList<CurrencyModel> tempList = new ArrayList<>();

                for(CurrencyModel currency : currencyList) {

                    String content = currency.isoCode() + " " + currency.name()
                            + " " + currency.symbol() + " " + currency.location();

                    if(content.toLowerCase().contains(constraint.toString().toLowerCase())) {

                        tempList.add(currency);
                    }
                }
                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = currencyList.size();
                filterResults.values = currencyList;
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
            filteredList = (ArrayList<CurrencyModel>)results.values;
            notifyDataSetChanged();
        }
    }
}

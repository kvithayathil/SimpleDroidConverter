package com.jedikv.simpleconverter.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
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
import com.jedikv.simpleconverter.ui.adapters.gestures.ItemTouchHelperAdapter;
import com.jedikv.simpleconverter.ui.adapters.gestures.ItemTouchHelperViewHolder;
import com.jedikv.simpleconverter.utils.AndroidUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import converter_db.ConversionItem;
import converter_db.CurrencyEntity;
import converter_db.CurrencyPairEntity;
import timber.log.Timber;

/**
 * Created by Kurian on 08/05/2015.
 */
public class CurrencyConversionsAdapter extends RecyclerView.Adapter<CurrencyConversionsAdapter.CurrencyViewHolder> implements ItemTouchHelperAdapter {

    private List<ConversionItem> mConverterList;

    private CurrencyPairDbHelper mCurrencyPairDbHelper;
    private CurrencyDbHelper mCurrencyDbHelper;
    private ConversionItemDbHelper mConversionDbHelper;

    private CurrencyEntity sourceCurrencyEntity;

    private String mSourceCurrencyCode;

    private BigDecimal mInputValue;

    CoordinatorLayout parent;


    @Inject
    public CurrencyConversionsAdapter(Context context, CoordinatorLayout parent, String sourceCurrency) {
        Timber.tag(CurrencyConversionsAdapter.class.getSimpleName());
        mCurrencyPairDbHelper = new CurrencyPairDbHelper(context);
        mCurrencyDbHelper = new CurrencyDbHelper(context);
        mConversionDbHelper = new ConversionItemDbHelper(context);
        mConverterList = new ArrayList<>();
        sourceCurrencyEntity = mCurrencyDbHelper.getCurrency(sourceCurrency);

        mConverterList.addAll(mConversionDbHelper.getAll());

        this.parent = parent;
    }


    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return mConverterList.get(position).getId();
    }

    public void updateCurrencyTargets(String currencyCode, BigDecimal inputValue) {

        mSourceCurrencyCode = currencyCode;

        sourceCurrencyEntity = mCurrencyDbHelper.getCurrency(currencyCode);
        //Clear the dao session cache so that it can be re-queried onBind
        mConversionDbHelper.clearCache();

        //Ensure the value is converted to int to retain float values
        mInputValue = inputValue;

        Timber.d("Preinput: " + inputValue + " PostInput: " + mInputValue);
        notifyDataSetChanged();
    }


    public BigDecimal getInputValue() {
        return mInputValue;
    }

    public ArrayList<String> getSelectedCurrencyCodeList() {

        ArrayList<String> codeList = new ArrayList<>();


        List<CurrencyPairEntity> pairEntityList = mCurrencyPairDbHelper.getPairsToBeUpdated(sourceCurrencyEntity.getNumericCode());

        for(CurrencyPairEntity entity : pairEntityList) {

           CurrencyEntity currencyEntity = entity.getTarget_id();
            codeList.add(currencyEntity.getCode());
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

        Timber.d("onBind at position: " + position);

        ConversionItem conversionEntity = mConverterList.get(position);
        CurrencyPairEntity pairEntity = mCurrencyPairDbHelper.queryById(conversionEntity.getPair_id());
        CurrencyEntity currencyEntity = pairEntity.getTarget_id();


        //Keep the position in sync with the adapter
        if(conversionEntity.getPosition() != position) {
            conversionEntity.setPosition(position);
            mConversionDbHelper.update(conversionEntity);
        }


        holder.bind(mInputValue, pairEntity, currencyEntity);

    }

    @Override
    public int getItemCount() {
        return mConverterList.size();
    }

    public List<ConversionItem> getItems() {
        return mConverterList;
    }

    public void addItem(ConversionItem conversionEntity) {

        if(mConverterList.add(conversionEntity)) {
            notifyItemInserted(conversionEntity.getPosition());
        }
    }

    public void addItemAtPosition(int position, ConversionItem conversionEntity) {

        mConverterList.add(position, conversionEntity);
        notifyItemInserted(position);
    }

    public ConversionItem removeItem(int position) {

        final ConversionItem entity = mConverterList.remove(position);
        entity.setPosition(position);
        mConversionDbHelper.deleteItem(entity);
        notifyItemRemoved(position);

        Snackbar.make(parent, R.string.snack_bar_deleted, Snackbar.LENGTH_LONG).setAction(R.string.snack_bar_undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConversionDbHelper.insertOrUpdate(entity);
                addItemAtPosition(entity.getPosition(), entity);
            }
        }).show();

        return entity;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mConverterList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        App.getBusInstance().post(new RemoveConversionEvent(position));

    }

    public static class CurrencyViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        private final DecimalFormat mDecimalFormat = new DecimalFormat("#0.0000", new DecimalFormatSymbols(Locale.getDefault()));

        @Bind(R.id.card_view)
        CardView cardView;
        @Bind(R.id.iv_flag)
        ImageView ivFlag;
        @Bind(R.id.tv_currency_name)
        AppCompatTextView tvCurrencyName;
        @Bind(R.id.tv_value)
        AppCompatTextView tvValue;
        @Bind(R.id.ib_remove)
        ImageButton ibRemove;
        @Bind(R.id.tv_currency_code)
        AppCompatTextView tvCurrencyCode;

        public CurrencyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            mDecimalFormat.setParseBigDecimal(true);
            mDecimalFormat.setMinimumFractionDigits(4);
        }


        public void bind(BigDecimal inputValue, CurrencyPairEntity currencyPairEntity, CurrencyEntity currencyEntity) {

            String code = currencyEntity.getCode();
            final int flagId = AndroidUtils.getDrawableResIdByCurrencyCode(ivFlag.getContext(), code);
            ivFlag.setImageResource(flagId);

            tvCurrencyCode.setText(code);
            tvCurrencyName.setText(currencyEntity.getName());
            setValue(inputValue, currencyPairEntity.getRate());
        }

        public void setValue(BigDecimal inputValue, int rate) {

            Timber.d("Input value: " + inputValue + " rate: " + rate);
            BigDecimal result = inputValue.multiply(new BigDecimal(rate).movePointLeft(4));
            //Revert the value back to the original decimal point position
            result.setScale(4, BigDecimal.ROUND_HALF_UP);
            Timber.d("Result move point left: " + result);

            //BigDecimal decimalResult = intResult.divide(new BigDecimal(10000 * 10000), 4, RoundingMode.HALF_UP);

            //Timber.d("Result: " + result + " Converted result: " + decimalResult);

            tvValue.setText(result.toString());
        }

        @OnClick(R.id.ib_remove)
        public void removeItem() {

            Timber.d("Remove adapter position: " + getAdapterPosition() + " Layout position: " + getLayoutPosition() + " Old position: " + getOldPosition());

            App.getBusInstance().post(new RemoveConversionEvent(getAdapterPosition()));
        }

        @OnLongClick(R.id.card_view)
        public boolean longClickDrag(@NonNull View view) {
            return true;
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.parseColor("#d2d2d2"));
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(Color.WHITE);
        }
    }

}

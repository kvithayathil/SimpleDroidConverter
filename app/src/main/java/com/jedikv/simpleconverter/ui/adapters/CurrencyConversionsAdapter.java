package com.jedikv.simpleconverter.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
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
import com.jedikv.simpleconverter.utils.AndroidUtils;
import com.jedikv.simpleconverter.utils.ConversionUtils;
import com.makeramen.dragsortadapter.DragSortAdapter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnLongClick;
import converter_db.ConversionEntity;
import converter_db.CurrencyEntity;
import converter_db.CurrencyPairEntity;
import timber.log.Timber;

/**
 * Created by Kurian on 08/05/2015.
 */
public class CurrencyConversionsAdapter extends DragSortAdapter<CurrencyConversionsAdapter.CurrencyViewHolder> {

    private List<ConversionEntity> mConverterList;

    private CurrencyPairDbHelper mCurrencyPairDbHelper;
    private CurrencyDbHelper mCurrencyDbHelper;
    private ConversionItemDbHelper mConversionDbHelper;

    private String mSourceCurrencyCode;

    private BigDecimal mInputValue;


    @Inject
    public CurrencyConversionsAdapter(Context context, RecyclerView recyclerView, String sourceCurrency) {
        super(recyclerView);
        Timber.tag(CurrencyConversionsAdapter.class.getSimpleName());
        mCurrencyPairDbHelper = new CurrencyPairDbHelper(context);
        mCurrencyDbHelper = new CurrencyDbHelper(context);
        mConversionDbHelper = new ConversionItemDbHelper(context);
        mConverterList = new ArrayList<>();
        mSourceCurrencyCode = sourceCurrency;

        mConverterList.addAll(mConversionDbHelper.getAll());
    }


    @Override
    public int getPositionForId(long id) {

        for(int i = 0; i < mConverterList.size(); i++) {
            if(mConverterList.get(i).getId() == id) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public boolean move(int fromPosition, int toPosition) {
        mConverterList.add(toPosition, mConverterList.remove(fromPosition));
        return true;
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
        mConverterList.clear();
        mConverterList.addAll(mConversionDbHelper.getAll());

        //Ensure the value is converted to int to retain float values
        mInputValue = inputValue;

        Timber.d("Preinput: " + inputValue + " PostInput: " + mInputValue);
        notifyDataSetChanged();
    }

    public void onPostNetworkUpdate() {

        mConverterList.clear();
        mConverterList.addAll(mConversionDbHelper.getAll());
        notifyDataSetChanged();
    }

    public BigDecimal getInputValue() {
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
        CurrencyViewHolder viewHolder = new CurrencyViewHolder(this, v);

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

        if(pairEntity == null) {
            pairEntity = new CurrencyPairEntity();
            pairEntity.setRate(0);
            pairEntity.setPair(mSourceCurrencyCode + "/" + currencyEntity.getCode());
            long id = mCurrencyPairDbHelper.insertOrUpdate(pairEntity);
            pairEntity.setId(id);
        }

        holder.bind(mInputValue, pairEntity, currencyEntity);

        holder.cardView.setVisibility(getDraggingId() == conversionEntity.getId() ? View.INVISIBLE : View.VISIBLE);
        holder.cardView.postInvalidate();
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

    public static class CurrencyViewHolder extends DragSortAdapter.ViewHolder {

        private final DecimalFormat mDecimalFormat = new DecimalFormat("#0.0000", new DecimalFormatSymbols(Locale.getDefault()));

        @InjectView(R.id.card_view)
        CardView cardView;
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

        public CurrencyViewHolder(DragSortAdapter adapter, View v) {
            super(adapter, v);
            ButterKnife.inject(this, v);
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
            startDrag();
            return true;
        }
    }

}

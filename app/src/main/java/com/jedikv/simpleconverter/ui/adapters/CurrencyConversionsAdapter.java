package com.jedikv.simpleconverter.ui.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.jedikv.simpleconverter.App;
import com.jedikv.simpleconverter.R;
import com.jedikv.simpleconverter.busevents.RemoveConversionEvent;
import com.jedikv.simpleconverter.ui.adapters.gestures.ItemTouchHelperAdapter;
import com.jedikv.simpleconverter.ui.adapters.gestures.ItemTouchHelperViewHolder;
import com.jedikv.simpleconverter.ui.model.ConversionItemModel;
import com.jedikv.simpleconverter.utils.AndroidUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import timber.log.Timber;

/**
 * Created by Kurian on 08/05/2015.
 */
public class CurrencyConversionsAdapter
        extends RecyclerView.Adapter<CurrencyConversionsAdapter.CurrencyViewHolder>
        implements ItemTouchHelperAdapter {

    private List<ConversionItemModel> conversionItems;
    private BigDecimal inputValue;
    private CoordinatorLayout parent;

    @Inject
    public CurrencyConversionsAdapter(CoordinatorLayout parent) {
        Timber.tag(CurrencyConversionsAdapter.class.getSimpleName());
        this.parent = parent;
        this.conversionItems = Collections.emptyList();
        inputValue = BigDecimal.ZERO;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return conversionItems.get(position).conversionId();
    }

    public BigDecimal getInputValue() {
        return inputValue;
    }

    @Override
    public CurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.currency_list_card, parent, false);
        return new CurrencyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CurrencyViewHolder holder, int position) {
        Timber.d("onBind at position: " + position);
        holder.bind(inputValue, conversionItems.get(position));
    }

    @Override
    public int getItemCount() {
        return conversionItems.size();
    }

    public List<ConversionItemModel> getItems() {
        return conversionItems;
    }

    public void addItem(ConversionItemModel conversion) {
        conversionItems.add(0, conversion);
        notifyItemInserted(0);
    }

    public void updateValue(String value) {
        //Assume 0 at these inputs
        if(TextUtils.isEmpty(value)
                || value.startsWith("0")
                || value.startsWith(".")) {
            inputValue = BigDecimal.ZERO;
        } else {
            inputValue = new BigDecimal(value);
        }
        notifyDataSetChanged();
    }

    public void addItemAtPosition(int position, ConversionItemModel conversion) {
        conversionItems.add(position, conversion);
        notifyItemInserted(position);
    }

    public ConversionItemModel removeItem(final int position) {

        final ConversionItemModel item = conversionItems.remove(position);
        notifyItemRemoved(position);

        Snackbar.make(parent, R.string.snack_bar_deleted, Snackbar.LENGTH_LONG)
                .setAction(R.string.snack_bar_undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addItemAtPosition(position, item);
                    }
                }).setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                //Only process the delete if the UNDO hasn't been clicked
                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                    //TODO Delete via presenter
                }
            }
        }).show();

        return item;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(conversionItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        App.getBusInstance().post(new RemoveConversionEvent(position));
    }

    static class CurrencyViewHolder extends RecyclerView.ViewHolder
            implements ItemTouchHelperViewHolder {

        @BindView(R.id.card_view)
        CardView cardView;
        @BindView(R.id.iv_flag)
        ImageView ivFlag;
        @BindView(R.id.tv_currency_name)
        AppCompatTextView tvCurrencyName;
        @BindView(R.id.tv_value)
        AppCompatTextView tvValue;
        @BindView(R.id.ib_remove)
        ImageButton ibRemove;
        @BindView(R.id.tv_currency_code)
        AppCompatTextView tvCurrencyCode;

        public CurrencyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        public void bind(BigDecimal inputValue, ConversionItemModel conversionItem) {

            final String code = conversionItem.isoCode();
            final int flagId
                    = AndroidUtils.getDrawableResIdByCurrencyCode(itemView.getContext(), code);
            ivFlag.setImageResource(flagId);
            tvCurrencyCode.setText(code);
            tvCurrencyName.setText(conversionItem.name());
            setValue(inputValue, conversionItem.rate());
        }

        private void setValue(BigDecimal inputValue, BigDecimal value) {
            Timber.d("Input value: %1$s rate: %2$s", inputValue, value);
            final BigDecimal result = inputValue.multiply(value);
            //Revert the value back to the original decimal point position
            result.setScale(4, BigDecimal.ROUND_HALF_UP);
            Timber.d("Result move point left: %1$s", result);
            tvValue.setText(result.toString());
        }

        @OnClick(R.id.ib_remove)
        public void removeItem() {

            Timber.d("Remove adapter position: " + getAdapterPosition() + " Layout position: "
                    + getLayoutPosition() + " Old position: " + getOldPosition());

            App.getBusInstance().post(new RemoveConversionEvent(getAdapterPosition()));
        }

        @OnLongClick(R.id.card_view)
        public boolean longClickDrag(@NonNull View view) {
            return true;
        }

        @Override
        public void onItemSelected() {
            ((CardView) itemView).setCardBackgroundColor(Color.parseColor("#d2d2d2"));
        }

        @Override
        public void onItemClear() {
            ((CardView) itemView).setCardBackgroundColor(Color.WHITE);
        }
    }

}

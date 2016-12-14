package com.jedikv.simpleconverter.ui.conversionscreen;

import com.jedikv.simpleconverter.ui.model.ConversionItemModel;
import com.jedikv.simpleconverter.ui.model.CurrencyModel;
import com.jedikv.simpleconverter.ui.views.MvpView;

import java.math.BigDecimal;

import converter_db.ConversionItem;

/**
 * Created by KV_87 on 20/09/2015.
 */
public interface ConversionView extends MvpView {

    void updateViews();

    void insertConversionItem(ConversionItemModel conversionItem);

    int getListSize();

    void updateSelectedCurrency(CurrencyModel source, int value);


}

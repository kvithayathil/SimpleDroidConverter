package com.jedikv.simpleconverter.ui.views;

import java.math.BigDecimal;

import converter_db.ConversionItem;
import converter_db.CurrencyPairEntity;

/**
 * Created by KV_87 on 20/09/2015.
 */
public interface IConversionView extends IView {

    String getCurrentSourceCurrency();

    long getCurrentSourceCurrencyCode();

    void updateViews();

    void updateList(BigDecimal inputValue);

    void insertConversionItem(ConversionItem conversionItem);

    int getListSize();
}

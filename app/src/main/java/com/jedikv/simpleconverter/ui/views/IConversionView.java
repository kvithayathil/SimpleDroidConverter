package com.jedikv.simpleconverter.ui.views;

import java.math.BigDecimal;

/**
 * Created by KV_87 on 20/09/2015.
 */
public interface IConversionView extends IView {

    String getCurrentSourceCurrency();

    void updateViews();

    void updateList(BigDecimal inputValue);
}

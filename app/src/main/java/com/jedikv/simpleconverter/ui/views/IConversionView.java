package com.jedikv.simpleconverter.ui.views;

/**
 * Created by KV_87 on 20/09/2015.
 */
public interface IConversionView {

    String getCurrentSourceCurrency();

    void updateViews();

    void updateList(String inputValue);
}

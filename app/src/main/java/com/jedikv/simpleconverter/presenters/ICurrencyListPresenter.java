package com.jedikv.simpleconverter.presenters;

import java.util.List;

/**
 * Created by KV_87 on 28/12/2015.
 */
public interface ICurrencyListPresenter {

    List<String> getListToHide(long sourceCurrencyCode);

}

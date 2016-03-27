package com.jedikv.simpleconverter.presenters;

import android.content.Context;

import com.jedikv.simpleconverter.dbutils.CurrencyDbHelper;
import com.jedikv.simpleconverter.dbutils.CurrencyPairDbHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import converter_db.CurrencyEntity;
import converter_db.CurrencyPairEntity;

/**
 * Created by KV_87 on 28/12/2015.
 */
public class CurrencyListPresenter implements ICurrencyListPresenter {

    private CurrencyPairDbHelper currencyPairDbHelper;
    private CurrencyDbHelper currencyDbHelper;

    public CurrencyListPresenter(CurrencyPairDbHelper currencyPairDbHelper, CurrencyDbHelper currencyDbHelper) {
        this.currencyDbHelper = currencyDbHelper;
        this.currencyPairDbHelper = currencyPairDbHelper;
    }

    @Override
    public List<String> getListToHide(long sourceCurrencyCode) {
        List<CurrencyPairEntity> pairEntityList = currencyPairDbHelper.getPairsToBeUpdated(sourceCurrencyCode);

        if(pairEntityList != null && !pairEntityList.isEmpty()) {

            ArrayList<String> codeList = new ArrayList<>(pairEntityList.size());

            for(CurrencyPairEntity entity : pairEntityList) {
                CurrencyEntity currencyEntity = entity.getTarget_id();
                codeList.add(currencyEntity.getCode());
            }
            return codeList;
        } else {
            return Collections.EMPTY_LIST;
        }
    }
}

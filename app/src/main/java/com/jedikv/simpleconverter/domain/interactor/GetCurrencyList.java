package com.jedikv.simpleconverter.domain.interactor;

import com.jedikv.simpleconverter.data.PersistentDataSource;
import com.jedikv.simpleconverter.domain.CurrencyItem;
import com.jedikv.simpleconverter.domain.executor.PostExecutionThread;
import com.jedikv.simpleconverter.domain.executor.ThreadExecutor;
import com.jedikv.simpleconverter.ui.model.CurrencyModel;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Kurian on 30/11/2016.
 */

public class GetCurrencyList extends UseCase {

    private final PersistentDataSource localDataSource;

    public GetCurrencyList(PersistentDataSource persistentDataSource,
                           PostExecutionThread mainThread,
                           ThreadExecutor executionThread) {
        super(mainThread, executionThread);
        this.localDataSource = persistentDataSource;
    }


    public Observable<List<CurrencyModel>> getCurrencyList(List<String> selectedCurrencies) {
        return localDataSource
                .getFilteredCurrencies(selectedCurrencies)
                .subscribeOn(getExecutionThread().getScheduler())
                .map(new Func1<List<CurrencyItem>, List<CurrencyModel>>() {
                    @Override
                    public List<CurrencyModel> call(List<CurrencyItem> currencyItems) {
                        List<CurrencyModel> models
                                = new ArrayList<>(currencyItems.size());

                        for (CurrencyItem item : currencyItems) {
                            models.add(CurrencyModel.builder()
                                    .location(item.location())
                                    .isoCode(item.isoCode())
                                    .numericCode(item.numCode())
                                    .name(item.name())
                                    .symbol(item.symbol())
                                    .build());
                        }

                        return models;
                    }
                })
                .observeOn(getPostExecutionThread().getScheduler());
    }
}

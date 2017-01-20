package com.jedikv.simpleconverter.domain.interactor;

import com.jedikv.simpleconverter.data.PersistentDataSource;
import com.jedikv.simpleconverter.domain.ConversionItem;
import com.jedikv.simpleconverter.domain.CurrencyItem;
import com.jedikv.simpleconverter.domain.executor.PostExecutionThread;
import com.jedikv.simpleconverter.domain.executor.ThreadExecutor;
import com.jedikv.simpleconverter.ui.model.ConversionItemModel;
import com.jedikv.simpleconverter.ui.model.CurrencyModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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


    public Observable<List<CurrencyModel>> selectCurrencyForSource(final String sourceIso) {
        return localDataSource
                .getFilteredCurrencies(Collections.singletonList(sourceIso))
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

    public Observable<List<CurrencyModel>> selectCurrencyForConversion(final String sourceIso) {
        return localDataSource
                .getConversionItems(sourceIso)
                .map(new Func1<List<ConversionItemModel>, List<String>>() {
                    @Override
                    public List<String> call(List<ConversionItemModel> conversionItems) {
                        List<String> isoCodes = new ArrayList<>();
                        isoCodes.add(sourceIso);

                        for(ConversionItemModel item : conversionItems) {
                            isoCodes.add(item.isoCode());
                        }
                        return isoCodes;
                    }
                })
                .flatMap(new Func1<List<String>, Observable<List<CurrencyItem>>>() {
                    @Override
                    public Observable<List<CurrencyItem>> call(List<String> strings) {
                        return localDataSource.getFilteredCurrencies(strings);
                    }
                })
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

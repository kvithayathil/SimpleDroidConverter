package com.jedikv.simpleconverter.domain.interactor;

import com.jedikv.simpleconverter.data.NetworkDataSource;
import com.jedikv.simpleconverter.data.PersistentDataSource;
import com.jedikv.simpleconverter.domain.ConversionItem;
import com.jedikv.simpleconverter.domain.CurrencyItem;
import com.jedikv.simpleconverter.domain.executor.PostExecutionThread;
import com.jedikv.simpleconverter.domain.executor.ThreadExecutor;
import com.jedikv.simpleconverter.ui.model.ConversionItemModel;
import com.jedikv.simpleconverter.ui.model.CurrencyModel;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Kurian on 30/11/2016.
 */

public class ConversionOperations extends UseCase {

    private final PersistentDataSource localDataSource;
    private final NetworkDataSource networkDataSource;

    public ConversionOperations(PersistentDataSource localDataSource,
                                NetworkDataSource networkDataSource,
                                PostExecutionThread mainThread,
                                ThreadExecutor executionThread) {
        super(mainThread, executionThread);

        this.localDataSource = localDataSource;
        this.networkDataSource = networkDataSource;
    }


    public Observable<List<ConversionItemModel>> loadConversionItems(final String sourceCurrencyIso,
                                                                     final List<String> targetCurrencyIsos) {

        //Load from local data store
        final Observable<List<ConversionItemModel>> localItem
                = loadConversionsFromLocalStore(sourceCurrencyIso)
                .subscribeOn(getExecutionThread().getScheduler());

        localItem.map(new Func1<List<ConversionItemModel>, List<String>>() {

            @Override
            public List<String> call(List<ConversionItemModel> items) {

                List<String> isoCodes = new ArrayList<>();
                for(ConversionItemModel item : items) {
                    isoCodes.add(item.isoCode());
                }

                return isoCodes;
            }
        })
        .flatMap(new Func1<List<String>, Observable<List<ConversionItemModel>>>() {
            @Override
            public Observable<List<ConversionItemModel>> call(List<String> strings) {
                return downloadAndStoreConversionItems(sourceCurrencyIso, strings);
            }
        });

        //Download from network
        final Observable<List<ConversionItemModel>> downloadConversionItems
                = downloadAndStoreConversionItems(sourceCurrencyIso, targetCurrencyIsos);

        //Emit their responses sequentially
        return Observable.concat(localItem, downloadConversionItems)
                .observeOn(getPostExecutionThread().getScheduler());
    }

    private Observable<List<ConversionItemModel>> loadConversionsFromLocalStore(final String sourceIso) {
        return localDataSource.getConversionItems(sourceIso)
                .subscribeOn(getExecutionThread().getScheduler());

    }

    private Observable<List<ConversionItemModel>> downloadAndStoreConversionItems(final String sourceIso,
                                                                                  List<String> targetIso) {

        return networkDataSource
                .getConversionItems(sourceIso, targetIso)
                .flatMap(new Func1<List<ConversionItem>, Observable<PutResults<ConversionItem>>>() {
                    @Override
                    public Observable<PutResults<ConversionItem>> call(List<ConversionItem> items) {
                        return localDataSource.saveConversionItems(items);
                    }
                })
                .flatMap(new Func1<PutResults<ConversionItem>, Observable<List<ConversionItemModel>>>() {
                    @Override
                    public Observable<List<ConversionItemModel>> call(PutResults<ConversionItem> conversionItemPutResults) {
                        return loadConversionsFromLocalStore(sourceIso);
                    }
                })
                .subscribeOn(getExecutionThread().getScheduler());

    }


    public Observable<Void> removeConversionItem(String targetCurrencyIsoCode, int position) {
        return localDataSource
                .removeConversionItemFromList(targetCurrencyIsoCode, position)
                .subscribeOn(getExecutionThread().getScheduler())
                .observeOn(getPostExecutionThread().getScheduler());
    }

    public Observable<ConversionItemModel> addConversionItem(String sourceCurrencyIso,
                                                             String targetCurrencyIso,
                                                             int targetPosition) {


        final Observable<ConversionItemModel> fromNetwork
                = downloadAndStoreConversionItems(sourceCurrencyIso,
                Collections.singletonList(targetCurrencyIso))
                .flatMap(new Func1<List<ConversionItemModel>, Observable<ConversionItemModel>>() {
                    @Override
                    public Observable<ConversionItemModel> call(List<ConversionItemModel> conversionItemModels) {
                        return Observable.from(conversionItemModels)
                                .first();
                    }
                });

        final Observable<ConversionItemModel> fromLocal = localDataSource
                .addConversionItem(sourceCurrencyIso, targetCurrencyIso, targetPosition);

        return Observable.concat(fromLocal, fromNetwork);
    }

    public Observable<CurrencyModel> fetchSelectedSourceCurrency(String sourceCurrencyIso) {
        return localDataSource.getCurrency(sourceCurrencyIso)
                .subscribeOn(getExecutionThread().getScheduler())
                .observeOn(getPostExecutionThread().getScheduler())
                .map(new Func1<CurrencyItem, CurrencyModel>() {
                    @Override
                    public CurrencyModel call(CurrencyItem item) {
                        return CurrencyModel.builder()
                                .numericCode(item.numCode())
                                .isoCode(item.isoCode())
                                .symbol(item.symbol())
                                .name(item.name())
                                .location(item.location())
                                .build();
                    }
                });
    }
}

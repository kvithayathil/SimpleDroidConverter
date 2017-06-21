package com.jedikv.simpleconverter.ui.conversionscreen;

import com.jedikv.simpleconverter.data.LocalKeyValueCache;
import com.jedikv.simpleconverter.domain.interactor.ConversionOperations;
import com.jedikv.simpleconverter.ui.base.BasePresenter;
import com.jedikv.simpleconverter.ui.model.ConversionItemModel;
import com.jedikv.simpleconverter.ui.model.CurrencyModel;

import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by Kurian on 13/12/2016.
 */

public class ConversionViewPresenter extends BasePresenter<ConversionView> {

    private final ConversionOperations conversionOperations;
    private final LocalKeyValueCache localKeyValueCache;

    @Inject
    public ConversionViewPresenter(ConversionOperations conversionOperations,
                                   LocalKeyValueCache localKeyValueCache) {

        this.conversionOperations = conversionOperations;
        this.localKeyValueCache = localKeyValueCache;
    }

    public void addConversionItem(String sourceCurrencyIso,
                                  String targetCurrencyIso,
                                  int position) {
        addSubscription(conversionOperations
                .addConversionItem(sourceCurrencyIso, targetCurrencyIso, position)
                .subscribe(new Action1<ConversionItemModel>() {
                    @Override
                    public void call(ConversionItemModel item) {
                        if (isViewAttached()) {
                            getView().insertConversionItem(item);
                        }
                    }
                }));
    }

    public void removeConversionItem(String targetCurrencyIso,
                                     int position) {
        addSubscription(conversionOperations
                .removeConversionItem(targetCurrencyIso, position)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        //Already removed from adapter
                    }
                }));
    }

    public void loadConversionItems(String sourceCurrencyIso) {
        addSubscription(conversionOperations
                .loadConversionItems(sourceCurrencyIso)
                .subscribe(new Action1<List<ConversionItemModel>>() {
                    @Override
                    public void call(List<ConversionItemModel> items) {
                        if (isViewAttached()) {
                            getView().updateConversions(items);
                        }
                    }
                }));
    }

    public void cacheSourceEntry(String currencyIso, String value) {
        localKeyValueCache.saveSelectedSourceCurrencyIsoAndValue(currencyIso, value);
    }

    public void loadSelectedSourceCurrency(String isoCode) {
        addSubscription(conversionOperations
                .fetchSelectedSourceCurrency(isoCode)
                .subscribe(new Action1<CurrencyModel>() {
                    @Override
                    public void call(CurrencyModel source) {
                        if (isViewAttached()) {
                            getView().updateSelectedCurrency(source,
                                    localKeyValueCache.fetchSavedValue());
                        }
                    }
                }));
    }

    @Override
    public void attachView(ConversionView view) {
        super.attachView(view);
        final String cachedIso = localKeyValueCache.fetchSelectedSourceCurrency();

        addSubscription(conversionOperations
                .fetchSelectedSourceCurrency(cachedIso)
                .subscribe(new Action1<CurrencyModel>() {
                    @Override
                    public void call(CurrencyModel currency) {
                        final String value = localKeyValueCache.fetchSavedValue();
                        getView().updateSelectedCurrency(currency, value);
                    }
                }));
    }
}

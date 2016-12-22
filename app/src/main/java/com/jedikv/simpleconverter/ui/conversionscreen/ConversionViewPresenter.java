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

    private ConversionView view;

    @Inject
    public ConversionViewPresenter(ConversionOperations conversionOperations,
                                   LocalKeyValueCache localKeyValueCache) {

        this.conversionOperations = conversionOperations;
        this.localKeyValueCache = localKeyValueCache;
    }

    @Override
    public void attachView(ConversionView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void addConversionItem(String sourceCurrencyIso,
                                  String targetCurrencyIso,
                                  int position) {

        conversionOperations.addConversionItem(sourceCurrencyIso, targetCurrencyIso, position)
                .subscribe(new Action1<ConversionItemModel>() {
                    @Override
                    public void call(ConversionItemModel item) {
                        if (view != null) {
                            view.insertConversionItem(item);
                        }
                    }
                });
    }

    public void removeConversionItem(String targetCurrencyIso,
                                     int position) {

        conversionOperations.removeConversionItem(targetCurrencyIso, position)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                    }
                });
    }

    public void loadConversionItems(String sourceCurrencyIso) {
        conversionOperations.loadConversionItems(sourceCurrencyIso)
        .subscribe(new Action1<List<ConversionItemModel>>() {
            @Override
            public void call(List<ConversionItemModel> items) {
                if(view != null) {
                    view.updateConversions(items);
                }
            }
        });
    }

    public void cacheSourceEntry(String currencyIso, int value) {
        localKeyValueCache.saveSelectedSourceCurrencyIsoAndValue(currencyIso, value);
    }

    public void loadSelectedSourceCurrency() {
        final String isoCode = localKeyValueCache.fetchSelectedSourceCurrency();
        final int value = localKeyValueCache.fetchSavedValue();

        conversionOperations.fetchSelectedSourceCurrency(isoCode)
                .subscribe(new Action1<CurrencyModel>() {
                    @Override
                    public void call(CurrencyModel source) {
                        if (view != null)
                            view.updateSelectedCurrency(source, value);
                    }
                });
    }
}

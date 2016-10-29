package com.jedikv.simpleconverter.api;

import com.jedikv.simpleconverter.api.responses.YahooCurrencyRate;
import com.jedikv.simpleconverter.api.responses.YahooDataContainer;
import com.jedikv.simpleconverter.dbutils.CurrencyDbHelper;
import com.jedikv.simpleconverter.dbutils.CurrencyPairDbHelper;
import com.jedikv.simpleconverter.utils.YahooApiUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import converter_db.CurrencyPairEntity;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Kurian on 13/06/2015.
 */
public class YahooCurrencyDownloadService implements ICurrencyDownloadService {

    private static final String TAG = YahooCurrencyDownloadService.class.getSimpleName();

    private IYahooCurrencyApi api;

    CurrencyDbHelper currencyDbHelper;
    CurrencyPairDbHelper currencyPairDbHelper;


    @Inject
    public YahooCurrencyDownloadService(IYahooCurrencyApi api, CurrencyDbHelper currencyDbHelper, CurrencyPairDbHelper currencyPairDbHelper) {
        Timber.tag(TAG);

        this.currencyDbHelper = currencyDbHelper;
        this.currencyPairDbHelper = currencyPairDbHelper;

        this.api = api;

    }

    @Override
    public Subscription executeRequest(List<String> targetCurrencies, String sourceCurrency, final OnRequestFinished listener) {

        final List<String> currencyPair = YahooApiUtils.createReverseFromPairs(targetCurrencies, sourceCurrency);

        //Don't bother hitting the server if there's nothing there :)

        String query = YahooApiUtils.generateYQLCurrencyQuery(currencyPair);

        return api.getCurrencyPairs(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<YahooDataContainer, Observable<YahooCurrencyRate>>() {
                    @Override
                    public Observable<YahooCurrencyRate> call(YahooDataContainer yahooDataContainer) {
                        return Observable.from(yahooDataContainer.getQuery().getResults().getRate());
                    }
                }).flatMap(new Func1<YahooCurrencyRate, Observable<CurrencyPairEntity>>() {
                    @Override
                    public Observable<CurrencyPairEntity> call(YahooCurrencyRate yahooCurrencyRate) {
                        return Observable.just(createCurrencyPairEntity(yahooCurrencyRate));
                    }
                }).map(new Func1<CurrencyPairEntity, Long>() {
                    @Override
                    public Long call(CurrencyPairEntity currencyPairEntity) {

                        //Check whether to update or create a new entry and return the id of the entry
                        if (currencyPairDbHelper.updateCurrencyPair(currencyPairEntity) > 0) {

                            Timber.d("Entry updated");
                            return currencyPairDbHelper.getCurrencyPairFromIdPair(currencyPairEntity.getSource_currency(), currencyPairEntity.getTarget_currency()).getId();

                        } else {

                            long id = currencyPairDbHelper.insertOrUpdate(currencyPairEntity);
                            Timber.d("Entry created with ID: " + id);
                            return id;
                        }
                    }
                }).subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("Request success!");
                        //App.getBusInstance().post(new CurrencyUpdateEvent());

                        if (listener != null) {
                            listener.onRequestSuccess();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, e.getMessage());
                        if (listener != null) {
                            listener.onRequestError(e);
                        }
                    }

                    @Override
                    public void onNext(Long id) {
                        Timber.d("Currency Entity: " + id + " updated.");
                    }
                });
    }

    /**
     * Convert the yahoo rate to the local rate entity
     *
     * @param rate
     * @return
     */
    private CurrencyPairEntity createCurrencyPairEntity(YahooCurrencyRate rate) {

        String source = rate.getId().substring(0, 3);
        String target = rate.getId().substring(3);

        Timber.d("Source:  " + source + " Target: " + target);

        CurrencyPairEntity entity = new CurrencyPairEntity();
        entity.setLast_updated(new Date());
        entity.setCreated_date(new Date());
        entity.setSource_id(currencyDbHelper.getCurrency(source));
        entity.setTarget_id(currencyDbHelper.getCurrency(target));

        BigDecimal decimalRate = new BigDecimal(rate.getRate());

        Timber.d("BigDecimalRate: " + decimalRate.toPlainString() + " Rate String: " + rate.getRate());

        //Set the rate to a full integer to prevent any rounding errors from floats/doubles
        entity.setRate(decimalRate.multiply(new BigDecimal(10000)).intValue());

        return entity;
    }
}

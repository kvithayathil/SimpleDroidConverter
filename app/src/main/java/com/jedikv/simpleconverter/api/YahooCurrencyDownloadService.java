package com.jedikv.simpleconverter.api;

import android.content.Context;

import com.jedikv.simpleconverter.App;
import com.jedikv.simpleconverter.api.responses.YahooCurrencyRate;
import com.jedikv.simpleconverter.api.responses.YahooDataContainer;
import com.jedikv.simpleconverter.busevents.CurrencyUpdateEvent;
import com.jedikv.simpleconverter.dbutils.CurrencyDbHelper;
import com.jedikv.simpleconverter.dbutils.CurrencyPairDbHelper;
import com.jedikv.simpleconverter.utils.YahooApiUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import converter_db.CurrencyEntity;
import converter_db.CurrencyPairEntity;
import hirondelle.date4j.DateTime;
import retrofit.RestAdapter;
import rx.Subscriber;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * Created by Kurian on 13/06/2015.
 */
public class YahooCurrencyDownloadService {

    private static final String TAG = YahooCurrencyDownloadService.class.getSimpleName();

    private IYahooCurrencyApi api;

    CurrencyDbHelper currencyDbHelper;
    CurrencyPairDbHelper currencyPairDbHelper;


    @Inject
    public YahooCurrencyDownloadService(RestAdapter restAdapter, CurrencyDbHelper currencyDbHelper, CurrencyPairDbHelper currencyPairDbHelper) {
        Timber.tag(TAG);

        this.currencyDbHelper = currencyDbHelper;
        this.currencyPairDbHelper = currencyPairDbHelper;

        api = restAdapter.create(IYahooCurrencyApi.class);

    }

    public void executeRequest(List<String> targetCurrencies, String sourceCurrency) {

        List<String> currencyPair = YahooApiUtils.createReverseFromPairs(targetCurrencies, sourceCurrency);
        Timber.d("Currency Pair Size: " + currencyPair.size());


        if(!currencyPair.isEmpty()) {


            String query = YahooApiUtils.generateYQLCurrencyQuery(currencyPair);


            api.getCurrencyPairs(query).map(new Func1<YahooDataContainer, List<CurrencyPairEntity>>() {
                @Override
                public List<CurrencyPairEntity> call(YahooDataContainer yahooDataContainer) {
                    try {

                        List<CurrencyPairEntity> list = generateCurrencyPairList(yahooDataContainer.getQuery());

                        return list;
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }).subscribe(new Subscriber<List<CurrencyPairEntity>>() {
                @Override
                public void onCompleted() {
                    Timber.d("Request success!");
                    App.getBusInstance().post(new CurrencyUpdateEvent());
                }

                @Override
                public void onError(Throwable e) {
                    Timber.e(e, e.getMessage());
                }

                @Override
                public void onNext(List<CurrencyPairEntity> currencyPairEntityList) {
                    try {
                        saveCurrencyData(currencyPairEntityList);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Timber.e(e, e.getMessage());
                    }
                }
            });

        }
    }

    private List<CurrencyPairEntity> generateCurrencyPairList(YahooDataContainer.YahooCurrencyQueryResult result) throws ParseException{

        DateTime timestamp = new DateTime(result.getCreated());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<YahooCurrencyRate> rates = result.getResults().getRate();

        List<CurrencyPairEntity> entities = new ArrayList<>();

        for(YahooCurrencyRate rate : rates) {

            // rate format - Source/Target e.g. USD/GBP to get USD to GBP conversion

            CurrencyPairEntity entity = new CurrencyPairEntity();
            entity.setLast_updated(new Date());

            String source = rate.getId().substring(0,3);
            String target = rate.getId().substring(3);

            Timber.d("Source: " + source + " Target: " + target);

            CurrencyEntity sourceEntity = currencyDbHelper.getCurrency(source);
            CurrencyEntity targetEntity = currencyDbHelper.getCurrency(target);

            entity.setSource_id(sourceEntity);
            entity.setTarget_id(targetEntity);
            entity.setCreated_date(new Date());
            BigDecimal decimalRate = new BigDecimal(rate.getRate());

            Timber.d("BigDecimalRate: " + decimalRate.toPlainString() + " Rate String: " + rate.getRate());

            //Set the rate to a full integer to prevent any rounding errors from floats/doubles
            entity.setRate(decimalRate.multiply(new BigDecimal(10000)).intValue());
            entities.add(entity);
        }

        return entities;
    }

    /**
     * Save to local database
     * @param result the yahoo currency result
     */
    private void saveCurrencyData(List<CurrencyPairEntity> currencyPairEntityList) throws ParseException {

        currencyPairDbHelper.bulkInsertOrUpdate(currencyPairEntityList);
    }

    private IYahooCurrencyApi getApi() {
        return api;
    }
}

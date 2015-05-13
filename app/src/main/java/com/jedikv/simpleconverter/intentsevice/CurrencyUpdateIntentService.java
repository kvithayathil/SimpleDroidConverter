package com.jedikv.simpleconverter.intentsevice;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.jedikv.simpleconverter.App;
import com.jedikv.simpleconverter.api.IYahooCurrencyApi;
import com.jedikv.simpleconverter.api.YahooCurrencyRestAdapter;
import com.jedikv.simpleconverter.api.responses.YahooDataContainer;
import com.jedikv.simpleconverter.api.responses.YahooCurrencyRate;
import com.jedikv.simpleconverter.busevents.CurrencyUpdateEvent;
import com.jedikv.simpleconverter.dbutils.CurrencyPairDbHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import converter_db.CurrencyPairEntity;
import hirondelle.date4j.DateTime;
import retrofit.RestAdapter;
import timber.log.Timber;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class CurrencyUpdateIntentService extends IntentService {

    private static final String TAG = CurrencyUpdateIntentService.class.getSimpleName();
    private final RestAdapter mRestAdapter;

    private final DecimalFormat mRateDecimalFormat = new DecimalFormat("#0.0000");

    private static final String EXTRA_CURRENCY_TARGETS = "com.jedikv.simpleconverter.intentsevice.extra.pairs";
    private static final String EXTRA_CURRENCY_SOURCE = "com.jedikv.simpleconverter.intentsevice.extra.source";

    /**
     * Starts this service to perform a currency update
     *
     * @see IntentService
     */
    public static void startService(Context context, ArrayList<String> targetCurrencies, String sourceCurrency) {
        Intent intent = new Intent(context, CurrencyUpdateIntentService.class);

        intent.putStringArrayListExtra(EXTRA_CURRENCY_TARGETS, targetCurrencies);
        intent.putExtra(EXTRA_CURRENCY_SOURCE, sourceCurrency);

        context.startService(intent);
    }


    public CurrencyUpdateIntentService() {
        super(TAG);
        Timber.tag(TAG);
        mRestAdapter = YahooCurrencyRestAdapter.getInstance();
    }


    /**
     * Build the YQL statement to pass into the request
     * @param currencyPairs list of currency pairs to pass in e.g. USDGBP, USDCHF...
     * @return the YQL statement to execute the request
     */
    private String generateYQLCurrencyQuery(List<String> currencyPairs) {

        //select * from yahoo.finance.xchange where pair in ("USDMXN", "USDCHF")

        StringBuilder sb = new StringBuilder("select * from yahoo.finance.xchange where pair in ");
        sb.append("(");

        int count = 1;

        for (String currencyPair : currencyPairs) {

            sb.append("\"").append(currencyPair).append("\"");

            //Check if it's not the last entry in the list
            if(count < currencyPairs.size()) {
                sb.append(",");
            }

            count++;
        }

        sb.append(")");

        return sb.toString();
    }

    private List<String> createReverseFromPairs(List<String> targetCurrencies, String sourceCurrency) {
        
        List<String> currencyPairs = new ArrayList<>();

        for(String targetCurrency : targetCurrencies) {

            currencyPairs.add(targetCurrency + sourceCurrency);
            currencyPairs.add(sourceCurrency + targetCurrency);
        }

        return currencyPairs;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            List<String> targetCurrencies = intent.getStringArrayListExtra(EXTRA_CURRENCY_TARGETS);
            String sourceCurrency = intent.getStringExtra(EXTRA_CURRENCY_SOURCE);

            List<String> currencyPair = createReverseFromPairs(targetCurrencies, sourceCurrency);
            String query = generateYQLCurrencyQuery(currencyPair);

            Timber.d("Query: " + query);

            YahooDataContainer result = mRestAdapter.create(IYahooCurrencyApi.class).getCurrencyPairs(query);

            if(result != null) {
                try {
                    saveCurrencyData(result.getQuery());
                } catch (ParseException e) {
                    e.printStackTrace();
                    Timber.e(e, e.getMessage());
                }
            }
        }
    }

    /**
     * Save to local database
     * @param result the yahoo currency result
     */
    private void saveCurrencyData(YahooDataContainer.YahooCurrencyQueryResult result) throws ParseException {

        DateTime timestamp = new DateTime(result.getCreated());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse("2013-12-4");

        List<YahooCurrencyRate> rates = result.getResults().getRate();

        List<CurrencyPairEntity> entities = new ArrayList<>();

        for(YahooCurrencyRate rate : rates) {

            CurrencyPairEntity entity = new CurrencyPairEntity();
            entity.setDate(date);
            entity.setPair(rate.getName());
            BigDecimal decimalRate = new BigDecimal(rate.getRate());

            Timber.d("BigDecimalRate: " + decimalRate.toPlainString() + " Rate String: " + rate.getRate());

            //Set the rate to a full integer to prevent any rounding errors from floats/doubles
            entity.setRate(decimalRate.multiply(new BigDecimal(10000)).intValue());
            entities.add(entity);
        }

        CurrencyPairDbHelper helper = new CurrencyPairDbHelper(this);
        helper.bulkInsertOrUpdate(entities);

        App.getBusInstance().post(new CurrencyUpdateEvent());
    }
}

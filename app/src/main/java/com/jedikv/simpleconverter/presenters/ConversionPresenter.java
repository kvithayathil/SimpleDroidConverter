package com.jedikv.simpleconverter.presenters;

import android.text.TextUtils;

import com.jedikv.simpleconverter.api.CurrencyDownloadService;
import com.jedikv.simpleconverter.api.OnRequestFinished;
import com.jedikv.simpleconverter.dbutils.CurrencyDbHelper;
import com.jedikv.simpleconverter.dbutils.CurrencyPairDbHelper;
import com.jedikv.simpleconverter.ui.views.IConversionView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import converter_db.ConversionItem;
import converter_db.CurrencyEntity;
import converter_db.CurrencyPairEntity;
import rx.Subscription;
import timber.log.Timber;

/**
 * Created by KV_87 on 20/09/2015.
 */
public class ConversionPresenter implements IConversionPresenter {

    private final DecimalFormat decimalFormat;

    private IConversionView conversionView;
    private CurrencyDownloadService downloadService;
    private CurrencyPairDbHelper currencyPairDbHelper;
    private CurrencyDbHelper currencyDbHelper;

    private long currentCurrencyISOCode;

    private Subscription currencySubscription;

    public ConversionPresenter(CurrencyDownloadService downloadService, CurrencyPairDbHelper currencyPairDbHelper, CurrencyDbHelper currencyDbHelper) {
        Timber.d(ConversionPresenter.class.getSimpleName());
        this.currencyPairDbHelper = currencyPairDbHelper;
        this.currencyDbHelper = currencyDbHelper;
        this.downloadService = downloadService;
        decimalFormat = new DecimalFormat("#0.0000", new DecimalFormatSymbols(Locale.getDefault()));
        decimalFormat.setParseBigDecimal(true);
        decimalFormat.setMinimumFractionDigits(4);
    }

    @Override
    public void addCurrency(long targetCurrencyCode) {

        CurrencyEntity sourceCurrencyEntity = currencyDbHelper.getById(currentCurrencyISOCode);
        CurrencyEntity targetCurrencyEntity = currencyDbHelper.getById(targetCurrencyCode);
        Timber.d("TargetCurrency Code: " + targetCurrencyCode);

        Timber.d("source: %1$s", sourceCurrencyEntity.getName());
        Timber.d("source: %1$s", targetCurrencyEntity.getName());

        CurrencyPairEntity entity = currencyPairDbHelper.getCurrencyPair(currentCurrencyISOCode, targetCurrencyCode);
        if(entity == null) {

            Timber.d("New currency pair entry");
            //Create a dummy pair for now till it's updated over the web
            entity = new CurrencyPairEntity();
            entity.setSource_id(sourceCurrencyEntity);
            entity.setTarget_id(targetCurrencyEntity);
            entity.setRate(0);
            entity.setCreated_date(new Date());
            long id = currencyPairDbHelper.insertOrUpdate(entity);
            entity.setId(id);
        }

        ConversionItem conversionItem = new ConversionItem();
        conversionItem.setCurrencyPairEntity(entity);
        conversionItem.setPosition(conversionView.getListSize());
        conversionView.insertConversionItem(conversionItem);

    }

    @Override
    public void updateFromSourceCurrency(long sourceCurrencyCode) {

        this.currentCurrencyISOCode = sourceCurrencyCode;

        List<CurrencyPairEntity> pairEntityList = currencyPairDbHelper.getPairsToBeUpdated(sourceCurrencyCode);
        Timber.d("Pair size: %1$d", pairEntityList.size());
        if(pairEntityList != null && !pairEntityList.isEmpty()) {

            ArrayList<String> codeList = new ArrayList<>(pairEntityList.size());

            for(CurrencyPairEntity entity : pairEntityList) {
                CurrencyEntity currencyEntity = entity.getTarget_id();
                codeList.add(currencyEntity.getCode());
            }
            downloadCurrency(codeList);
        }
    }


    private void downloadCurrency(List<String> currencyList) {
        if(currencySubscription != null) {
            currencySubscription.unsubscribe();
        }

        Timber.d("Currency Pair Size: %1$d", currencyList.size());


        if(!currencyList.isEmpty()) {
            String currencyCode = currencyDbHelper.getById(currentCurrencyISOCode).getCode();
            currencySubscription = downloadService.executeRequest(currencyList, currencyCode, new OnRequestFinished() {
                @Override
                public void onRequestSuccess() {
                    if(conversionView != null) {
                        conversionView.updateViews();
                    }
                }

                @Override
                public void onRequestError(Throwable e) {

                }
            });
        }
    }

    public void convertValue(String value) {

        if(TextUtils.isEmpty(value)) {
           value = "0";
        }

        try {
            BigDecimal enteredValue = (BigDecimal)decimalFormat.parse(value);

            Timber.d("Inputted: %1$s Converted: %2$s", value, enteredValue.toPlainString());

            conversionView.updateList(enteredValue);

        } catch (NumberFormatException e) {
            Timber.e(e, e.getMessage());
        } catch (ParseException e) {
            Timber.e(e, e.getMessage());
        }
    }

    @Override
    public void removeItem(long id) {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void detachView() {
        Timber.d("%1$s detached", conversionView.getClass().getSimpleName());
        this.conversionView = null;

        if(currencySubscription != null) {
            currencySubscription.unsubscribe();
        }
    }

    @Override
    public void attachView(IConversionView view) {
        Timber.d("%1$s attached", view.getClass().getSimpleName());
        this.conversionView = view;
    }

    @Override
    public void onPause() {

    }


}

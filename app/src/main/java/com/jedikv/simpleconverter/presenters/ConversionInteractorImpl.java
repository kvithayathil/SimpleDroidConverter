package com.jedikv.simpleconverter.presenters;

import android.text.TextUtils;

import com.jedikv.simpleconverter.api.OnRequestFinished;
import com.jedikv.simpleconverter.api.YahooCurrencyDownloadService;
import com.jedikv.simpleconverter.ui.views.IConversionView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import timber.log.Timber;

/**
 * Created by KV_87 on 20/09/2015.
 */
public class ConversionInteractorImpl implements IPresenterBase<IConversionView> {

    private final DecimalFormat decimalFormat;

    private IConversionView conversionView;
    private YahooCurrencyDownloadService downloadService;

    private Subscription currencySubscription;

    public ConversionInteractorImpl(YahooCurrencyDownloadService yahooCurrencyDownloadService) {
        Timber.d(ConversionInteractorImpl.class.getSimpleName());
        this.downloadService = yahooCurrencyDownloadService;
        decimalFormat = new DecimalFormat("#0.0000", new DecimalFormatSymbols(Locale.getDefault()));
        decimalFormat.setParseBigDecimal(true);
        decimalFormat.setMinimumFractionDigits(4);
    }


    private void createL(String yo, String lo) {

        Observable.just(new )

    }

    public void downloadCurrency(List<String> currencyList) {
        if(currencySubscription != null) {
            currencySubscription.unsubscribe();
        }

        Timber.d("Currency Pair Size: " + currencyList.size());


        if(!currencyList.isEmpty()) {
            currencySubscription = downloadService.executeRequest(currencyList, conversionView.getCurrentSourceCurrency(), new OnRequestFinished() {
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

            Timber.d("Inputted: " + value + " Converted: " + enteredValue.toPlainString());

            conversionView.updateList(enteredValue);

        } catch (NumberFormatException e) {
            Timber.e(e, e.getMessage());
        } catch (ParseException e) {
            Timber.e(e, e.getMessage());
        }
    }

    @Override
    public void onResume() {

    }

    @Override
    public void detachView() {
        Timber.d(conversionView.getClass().getSimpleName() + " detached");
        this.conversionView = null;

        if(currencySubscription != null) {
            currencySubscription.unsubscribe();
        }
    }

    @Override
    public void attachView(IConversionView view) {
        Timber.d(view.getClass().getSimpleName() + " attached");
        this.conversionView = view;
    }

    @Override
    public void onPause() {

    }
}

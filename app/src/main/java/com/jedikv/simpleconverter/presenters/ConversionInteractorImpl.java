package com.jedikv.simpleconverter.presenters;

import android.text.TextUtils;

import com.jedikv.simpleconverter.api.YahooCurrencyDownloadService;
import com.jedikv.simpleconverter.ui.views.IConversionView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import rx.Subscription;
import timber.log.Timber;

/**
 * Created by KV_87 on 20/09/2015.
 */
public class ConversionInteractorImpl implements IConversionInteractor {

    private final DecimalFormat decimalFormat = new DecimalFormat("#0.0000", new DecimalFormatSymbols(Locale.getDefault()));

    private IConversionView conversionView;
    private YahooCurrencyDownloadService downloadService;

    private Subscription currencySubscription;

    public ConversionInteractorImpl(IConversionView conversionView, YahooCurrencyDownloadService yahooCurrencyDownloadService) {
        Timber.d(ConversionInteractorImpl.class.getSimpleName());
        this.conversionView = conversionView;
        this.downloadService = yahooCurrencyDownloadService;
    }

    @Override
    public void downloadCurrency(List<String> currencyList) {
        currencySubscription = downloadService.executeRequest(currencyList, conversionView.getCurrentSourceCurrency());
    }

    @Override
    public void convertValue(String value) {

        if(!TextUtils.isEmpty(value)) {
           value = "0";
        }

        try {
            BigDecimal enteredValue = (BigDecimal)decimalFormat.parse(value);
            conversionView.updateList(value);
            //mCurrencyConversionsAdapter.updateCurrencyTargets(getSourceCurrency(), enteredValue);

        } catch (NumberFormatException e) {
            Timber.e(e, e.getMessage());

        } catch (ParseException e) {
            Timber.e(e, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {
        if(currencySubscription != null) {
            currencySubscription.unsubscribe();
        }
    }
}

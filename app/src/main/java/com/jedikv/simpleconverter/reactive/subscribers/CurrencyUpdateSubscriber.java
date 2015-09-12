package com.jedikv.simpleconverter.reactive.subscribers;

import com.jedikv.simpleconverter.reactive.OnCurrencyUpdateListener;

import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by KV_87 on 12/09/15.
 */
public class CurrencyUpdateSubscriber extends Subscriber<Long> {

    private OnCurrencyUpdateListener listener;

    public CurrencyUpdateSubscriber(OnCurrencyUpdateListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCompleted() {
        listener.OnUpdateCompleted();
    }

    @Override
    public void onError(Throwable e) {
        Timber.e(e, e.getMessage());
    }

    @Override
    public void onNext(Long id) {
        Timber.d("Currency Entity: " + id + " updated.");
    }


}

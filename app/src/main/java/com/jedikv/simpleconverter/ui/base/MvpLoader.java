package com.jedikv.simpleconverter.ui.base;

import android.content.Context;
import android.support.v4.content.Loader;

import timber.log.Timber;

/**
 * Created by Kurian on 18/01/2017.
 */

public class MvpLoader<P extends BasePresenter> extends Loader<P> {

    private final PresenterFactory<P> factory;
    private final String tag;

    private P presenter;

    public MvpLoader(Context context, PresenterFactory<P> factory, String tag) {
        super(context);
        this.tag = tag;
        this.factory = factory;
        Timber.tag(tag);
    }

    @Override
    protected void onStartLoading() {
        Timber.d("onStartLoading %1$s", tag);

        //Deliver an existing presenter instance
        if(presenter != null) {
            deliverResult(presenter);
            return;
        }
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        Timber.d("onForceLoad %1$s", tag);
        presenter = factory.create();
        deliverResult(presenter);
    }

    @Override
    public void deliverResult(P data) {
        super.deliverResult(data);
        Timber.d("deliverResult %1$s", tag);
    }

    @Override
    protected void onReset() {
        Timber.d("onReset %1$s", tag);
        if(presenter != null) {
            presenter.onDestroy();
            presenter = null;
        }
    }

    public P getPresenter() {
        return presenter;
    }
}
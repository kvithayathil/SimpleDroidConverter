/***************************************************************************************************
 * The MIT License (MIT)
 * Copyright (c) 2017 Kurian
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * Last modified 21/01/17 16:32
 **************************************************************************************************/

package com.jedikv.simpleconverter.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import timber.log.Timber;

/**
 * Wrapper for handling the creation and operation of the MVP loader
 * Created by Kurian on 21/01/2017.
 */

public class MvpLoaderHandler<P extends BasePresenter> {

    private static final int LOADER_ID = -100;

    private final String presenterTag;
    private final LoaderManager loaderManager;
    private final PresenterFactory<P> presenterFactory;
    private P presenter;

    public MvpLoaderHandler(String presenterTag,
                            LoaderManager loaderManager,
                            PresenterFactory<P> presenterFactory) {

        this.presenterTag = presenterTag;
        this.loaderManager = loaderManager;
        this.presenterFactory = presenterFactory;

        Timber.tag(this.presenterTag);
    }

    public void init(Context context, Receiver<P> receiver) {
        Loader<P> loader = loaderManager.getLoader(LOADER_ID);
        if(loader == null) {
            initLoader(context, receiver);
        } else {
            //No need to recreate the presenter if it already exists
            this.presenter = ((MvpLoader<P>)loader).getPresenter();
            receiver.onPresenterCreatedOrRestored(presenter);
        }
    }

    private void initLoader(final Context context, final Receiver<P> receiver) {

        //LoaderCallbacks as an object, so no hint regarding Loader will be leak to the subclasses.
        loaderManager.initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<P>() {

            @Override
            public Loader<P> onCreateLoader(int id, Bundle args) {
                Timber.d("onCreateLoader");
                return new MvpLoader<>(context, presenterFactory, presenterTag);
            }

            @Override
            public void onLoadFinished(Loader<P> loader, P presenter) {
                Timber.d("onLoadFinished");
                receiver.onPresenterCreatedOrRestored(presenter);
            }

            @Override
            public void onLoaderReset(Loader<P> loader) {
                Timber.d("onLoaderReset");
                MvpLoaderHandler.this.presenter = null;
            }
        });
    }

    /**
     * Callback to receive the presenter from the loader
     * @param <P> Presenter
     */
    public interface Receiver<P extends BasePresenter> {
        void onPresenterCreatedOrRestored(@NonNull P presenter);
    }
}

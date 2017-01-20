package com.jedikv.simpleconverter.ui.activities;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.jedikv.simpleconverter.App;
import com.jedikv.simpleconverter.AppComponent;
import com.jedikv.simpleconverter.ui.base.BaseMvpLoader;
import com.jedikv.simpleconverter.ui.base.BasePresenter;
import com.jedikv.simpleconverter.ui.base.PresenterFactory;
import com.jedikv.simpleconverter.ui.views.MvpView;

import icepick.Icepick;
import timber.log.Timber;

/**
 * Created by Kurian on 08/05/2015.
 */
public abstract class BaseActivity<P extends BasePresenter<V>, V extends MvpView>
        extends AppCompatActivity {


    private static final int LOADER_ID = 1001;

    private P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        initMvpLoader();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        App.getBusInstance().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.attachView(getMvpView());
    }

    @Override
    protected void onPause() {
        presenter.detachView();
        super.onPause();
    }

    @Override
    protected void onStop() {
        App.getBusInstance().unregister(this);
        super.onStop();
    }

    public AppComponent getApplicationComponent() {
        return App.get(this).getAppComponent();
    }


    private void initMvpLoader() {
        //LoaderCallbacks as an object, so no hint regarding Loader will be leak to the subclasses.
        getSupportLoaderManager().initLoader(getLoaderId(),
                null,
                new LoaderManager.LoaderCallbacks<P>() {

            @Override
            public Loader<P> onCreateLoader(int id, Bundle args) {
                Timber.d("onCreateLoader %1$s", getPresenterTag());
                return new BaseMvpLoader<>(getApplicationContext(),
                        getPresenterFactory(),
                        getPresenterTag());
            }

            @Override
            public void onLoadFinished(Loader<P> loader, P data) {
                Timber.d("onLoadFinished %1$s", getPresenterTag());
                presenter = data;
                onPresenterPrepared(presenter);
            }

            @Override
            public void onLoaderReset(Loader<P> loader) {
                Timber.d("onLoaderReset %1$s", getPresenterTag());
                presenter = null;
                onPresenterDestroyed();
            }
        });
    }

    /**
     * Instance of {@link PresenterFactory} use to create a Presenter when needed.
     * This instance should not contain {@link android.app.Activity} context reference since it
     * will be keep on rotations.
     */
    public abstract PresenterFactory<P> getPresenterFactory();

    /**
     * Presenter tag use for logging/tracking purposes.
     */
    public abstract String getPresenterTag();

    /**
     * Hook for subclasses that deliver the {@link BasePresenter} before its View is attached.
     * Can be use to initialize the Presenter or simple hold a reference to it.
     */
    protected abstract void onPresenterPrepared(P presenter);

    /**
     * Hook for subclasses before the screen gets destroyed.
     */
    protected void onPresenterDestroyed() {
        Timber.d("onPresenterDestroyed %1$s", getPresenterTag());
    }

    public P getPresenter() {
        return this.presenter;
    }

    /**
     * Override in case of fragment not implementing {@link MvpView} interface
     */
    public V getMvpView() {
        //Yes ugly cast I know
        return (V) this;
    }

    /**
     * Use this method in case you want to specify a spefic ID for the {@link BaseMvpLoader}.
     * By default its value would be {@link #LOADER_ID}.
     */
    public static int getLoaderId() {
        return LOADER_ID;
    }
}

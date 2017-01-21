package com.jedikv.simpleconverter.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jedikv.simpleconverter.App;
import com.jedikv.simpleconverter.AppComponent;
import com.jedikv.simpleconverter.ui.base.MvpLoaderHandler;
import com.jedikv.simpleconverter.ui.base.MvpLoader;
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

    public static final String TAG = BaseActivity.class.getCanonicalName();

    private static final int LOADER_ID = 1001;

    private P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag(TAG);
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
        Log.i("onStart %1$s", getPresenterTag());
        onPresenterPrepared(presenter);
        presenter.attachView(getMvpView());
        App.getBusInstance().register(this);
    }

    @Override
    protected void onStop() {
        presenter.detachView();
        App.getBusInstance().unregister(this);
        super.onStop();
        Log.i("onStop %1$s", getPresenterTag());
    }

    public AppComponent getApplicationComponent() {
        return App.get(this).getAppComponent();
    }


    private void initMvpLoader() {

        final MvpLoaderHandler<P> loaderHandler
                = new MvpLoaderHandler<>(getPresenterTag(), getSupportLoaderManager(),
                getPresenterFactory());

        loaderHandler.init(this, new MvpLoaderHandler.Receiver<P>() {
            @Override
            public void onPresenterCreatedOrRestored(@NonNull P presenter) {
                Timber.d("onPresenterCreatedOrRestored: %1$s", getPresenterTag());
                setPresenter(presenter);
            }
        });
    }

    /**
     * Used as the final step in the presenter creation, not required for public access
     * @param presenter
     */
    private void setPresenter(P presenter) {
        this.presenter = presenter;
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
     * Use this method in case you want to specify a spefic ID for the {@link MvpLoader}.
     * By default its value would be {@link #LOADER_ID}.
     */
    public static int getLoaderId() {
        return LOADER_ID;
    }
}

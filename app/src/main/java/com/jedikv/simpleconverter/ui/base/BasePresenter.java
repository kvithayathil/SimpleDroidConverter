package com.jedikv.simpleconverter.ui.base;

import com.jedikv.simpleconverter.ui.views.MvpView;

/**
 * Created by Kurian on 13/12/2016.
 */

public abstract class BasePresenter<V extends MvpView> {

    public abstract void attachView(V view);

    public abstract void detachView();
}

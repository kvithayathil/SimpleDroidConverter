package com.jedikv.simpleconverter.presenters;

import com.jedikv.simpleconverter.ui.views.IView;

/**
 * Created by KV_87 on 27/09/2015.
 */
public interface IPresenterBase<V extends IView> {

    void attachView(V view);

    void detachView();

    void onResume();

    void onPause();
}

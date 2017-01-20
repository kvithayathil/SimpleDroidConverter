package com.jedikv.simpleconverter.ui.base;

import com.jedikv.simpleconverter.ui.views.MvpView;

import java.lang.ref.WeakReference;

/**
 * Created by Kurian on 13/12/2016.
 */

public abstract class BasePresenter<V extends MvpView> {

    private WeakReference<V> viewReference;

    public void attachView(V view) {
        if(!isViewAttached()) {
            viewReference = new WeakReference<>(view);
        }
    }

    public void detachView() {
        viewReference.clear();
    }

    public V getView() {
        return viewReference.get();
    }

    /**
     * Check if the view and presenter is still linked
     * @return true if view is still attached
     */
    public boolean isViewAttached() {
        return (viewReference != null)
                && (viewReference.get() != null);
    }

    /**
     * Any final clean up before the presenter is destroyed
     */
    public void onDestroy() {
    }
}

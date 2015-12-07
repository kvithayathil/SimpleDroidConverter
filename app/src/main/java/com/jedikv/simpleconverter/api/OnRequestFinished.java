package com.jedikv.simpleconverter.api;

/**
 * Created by KV_87 on 27/09/2015.
 */
public interface OnRequestFinished {

    void onRequestSuccess();

    void onRequestError(Throwable e);
}

package com.jedikv.simpleconverter.data;

/**
 * Created by Kurian on 02/12/2016.
 */

public class DataContainer<T> {

    private final T data;
    private final long timestamp;

    private static final long STALE_TIME = 1000 * 60;

    public DataContainer(T data) {
        this.data = data;
        timestamp = System.currentTimeMillis();
    }


    public boolean isUpToDate() {
        return System.currentTimeMillis() - timestamp < STALE_TIME;
    }

    public T getData() {
        return this.data;
    }
}

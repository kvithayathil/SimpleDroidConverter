package com.jedikv.simpleconverter.busevents;

/**
 * Created by Kurian on 20/05/2015.
 */
public class RemoveConversionEvent {

    private int mPosition;

    public RemoveConversionEvent(int position) {
        mPosition = position;
    }

    public int getPosition() {
        return mPosition;
    }
}

package com.jedikv.simpleconverter.model;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;

/**
 * Created by KV_87 on 25/04/2016.
 */
@Entity
public interface ConversionItemModel {

    @Key @Generated
    long getId();
    long getPairCode();
    int getPosition();
}

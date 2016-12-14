package com.jedikv.simpleconverter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Kurian on 12/12/2016.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ConversionAppScope {
}

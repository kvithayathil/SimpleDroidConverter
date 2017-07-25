package com.jedikv.simpleconverter.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Qualifier;

/**
 * Created by Kurian on 12/12/2016.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface OkhttpCacheFile {
}

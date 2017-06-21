/***************************************************************************************************
 * The MIT License (MIT)
 * Copyright (c) 2017 Kurian
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * Last modified 18/01/17 17:59
 **************************************************************************************************/

package com.jedikv.simpleconverter.ui.conversionscreen;

import android.support.annotation.NonNull;

import com.jedikv.simpleconverter.data.LocalKeyValueCache;
import com.jedikv.simpleconverter.domain.DomainModule;
import com.jedikv.simpleconverter.domain.interactor.ConversionOperations;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Kurian on 18/01/2017.
 */
@Module(includes = {DomainModule.class})
public class ConversionScreenModule {

    private final MainActivity activity;

    public ConversionScreenModule(MainActivity activity) {
        this.activity = activity;
    }

    @Provides
    @NonNull
    @ConversionScreenScope
    ConversionPresenterFactory providesConversionPresenterFactory(ConversionOperations conversionOperations,
                                                                  LocalKeyValueCache localKeyValueCache) {

        return new ConversionPresenterFactory(conversionOperations, localKeyValueCache);
    }
}
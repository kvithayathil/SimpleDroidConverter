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
 * Last modified 18/01/17 17:08
 **************************************************************************************************/

package com.jedikv.simpleconverter.ui.conversionscreen;

import com.jedikv.simpleconverter.data.LocalKeyValueCache;
import com.jedikv.simpleconverter.domain.interactor.ConversionOperations;
import com.jedikv.simpleconverter.ui.base.PresenterFactory;

import javax.inject.Inject;

/**
 * Created by Kurian on 18/01/2017.
 */

public class ConversionPresenterFactory implements PresenterFactory<ConversionViewPresenter> {

    private final ConversionOperations conversionOps;
    private final LocalKeyValueCache localKeyValueCache;

    @Inject
    public ConversionPresenterFactory(ConversionOperations conversionOps,
                                      LocalKeyValueCache localKeyValueCache) {

        this.conversionOps = conversionOps;
        this.localKeyValueCache = localKeyValueCache;
    }

    @Override
    public ConversionViewPresenter create() {
        return new ConversionViewPresenter(conversionOps, localKeyValueCache);
    }
}

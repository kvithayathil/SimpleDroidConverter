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
 * Last modified 19/01/17 20:25
 **************************************************************************************************/

package com.jedikv.simpleconverter.ui.selectcurrencyscreen;

import com.jedikv.simpleconverter.data.LocalKeyValueCache;
import com.jedikv.simpleconverter.domain.interactor.GetCurrencyList;
import com.jedikv.simpleconverter.ui.base.PresenterFactory;

import javax.inject.Inject;

/**
 * Created by Kurian on 19/01/2017.
 */

public class SelectCurrencyPresenterFactory
        implements PresenterFactory<SelectCurrencyScreenPresenter> {

    private final GetCurrencyList currencyOps;
    private final LocalKeyValueCache localKeyValueCache;

    @Inject
    public SelectCurrencyPresenterFactory(GetCurrencyList currencyOps,
                                      LocalKeyValueCache localKeyValueCache) {

        this.currencyOps = currencyOps;
        this.localKeyValueCache = localKeyValueCache;
    }


    @Override
    public SelectCurrencyScreenPresenter create() {
        return new SelectCurrencyScreenPresenter(currencyOps, localKeyValueCache);
    }
}

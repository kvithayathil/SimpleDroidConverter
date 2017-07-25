package com.jedikv.simpleconverter.api.yahoofinance;

import android.support.annotation.VisibleForTesting;
import com.jedikv.simpleconverter.api.jsonadapters.DateAdapter;
import com.squareup.moshi.Moshi;
import java.text.SimpleDateFormat;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;
import rx.Observable;

/**
 * Created by Kurian on 02/05/2015.
 */
public class YahooApiServiceImpl implements YahooApiService {

    @VisibleForTesting
    YahooApiService service;

    public YahooApiServiceImpl(final OkHttpClient client) {

        //Show http logs only in debug builds
        final Moshi moshi = new Moshi.Builder()
            .add(new DateAdapter(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")))
            .build();

        service = new Retrofit.Builder()
                .baseUrl("https://query.yahooapis.com/v1/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build()
                .create(YahooApiService.class);
    }

    @Override
    public Observable<YahooDataContainerResponse> getCurrencyPairs(String query) {
        return service.getCurrencyPairs(query);
    }
}

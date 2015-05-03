package module;

import com.jedikv.simpleconverter.api.IYahooCurrencyApi;
import com.jedikv.simpleconverter.api.YahooCurrencyRestAdapter;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

/**
 * Created by Kurian on 03/05/2015.
 */
@Module
public class CurrencyUpdaterModule {



    @Provides public IYahooCurrencyApi provideYahooCurrencyApi(RestAdapter restAdapter) {
        return restAdapter.create(IYahooCurrencyApi.class);
    }

    @Provides
    public RestAdapter provideYahooCurrencyRestAdapter() {
        return YahooCurrencyRestAdapter.getInstance();
    }
}

package module;

import com.jedikv.simpleconverter.response.ExchangePairResponse;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Kurian on 02/05/2015.
 */
@Module
public class CurrencyPairModule {

    @Provides
    ExchangePairResponse provideResponse() {

        return null;
    }
}

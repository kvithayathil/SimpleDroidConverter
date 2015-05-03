package module;

import android.app.Application;

import com.jedikv.simpleconverter.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Kurian on 03/05/2015.
 */
@Module
public class AppModule {

    private App mApp;

    public AppModule(App app) {
        this.mApp = app;
    }

    @Provides
    @Singleton
    public Application providesApplication() {
        return this.mApp;
    }
}

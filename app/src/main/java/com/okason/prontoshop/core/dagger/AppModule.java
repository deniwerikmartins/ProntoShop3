package com.okason.prontoshop.core.dagger;

import android.content.Context;

import com.okason.prontoshop.core.ProntoShopApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Valentine on 4/18/2016.
 */
@Module
public class AppModule {
    private final ProntoShopApplication app;

    public AppModule(ProntoShopApplication app) {
        this.app = app;
    }

    @Provides @Singleton
    public ProntoShopApplication getApp() {
        return app;
    }

    @Provides @Singleton
    public Context provideContext() {
        return app;
    }
}

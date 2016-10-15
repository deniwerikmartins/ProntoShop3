package com.okason.prontoshop.core;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.okason.prontoshop.common.AddInitialDataService;
import com.okason.prontoshop.core.dagger.AppComponent;
import com.okason.prontoshop.core.dagger.AppModule;
import com.okason.prontoshop.core.dagger.DaggerAppComponent;
import com.okason.prontoshop.util.Constants;
import com.squareup.otto.Bus;

/**
 * Created by Valentine on 4/9/2016.
 */
public class ProntoShopApplication extends Application {
    private Bus bus;
    public Bus getBus()
    {
        return bus;
    }


    private SharedPreferences sharedPreferences;

    private static ProntoShopApplication instance = new ProntoShopApplication();
    private static AppComponent appComponent;

    public static ProntoShopApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance.bus = new Bus();
        initDefaultProducts();
        getAppComponent();
    }

    public AppComponent getAppComponent() {
        if (appComponent == null){
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
        }
        return appComponent;
    }




    private void initDefaultProducts() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.getBoolean(Constants.FIRST_RUN, true)) {
            startService(new Intent(this, AddInitialDataService.class));
            editor.putBoolean(Constants.FIRST_RUN, false).commit();
        }

    }









}

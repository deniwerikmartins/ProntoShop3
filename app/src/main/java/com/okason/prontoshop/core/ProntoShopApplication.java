package com.okason.prontoshop.core;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.okason.prontoshop.core.dagger.AppComponent;
import com.okason.prontoshop.core.dagger.AppModule;
import com.okason.prontoshop.core.dagger.DaggerAppComponent;
import com.okason.prontoshop.models.Category;
import com.okason.prontoshop.models.Customer;
import com.okason.prontoshop.models.LineItem;
import com.okason.prontoshop.models.Product;
import com.okason.prontoshop.models.SalesTransaction;
import com.okason.prontoshop.util.Constants;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.atomic.AtomicLong;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


/**
 * Created by Valentine on 4/9/2016.
 */
public class ProntoShopApplication extends Application {
    private EventBus bus;
    public org.greenrobot.eventbus.EventBus getBus()
    {
        return bus;
    }

    public static AtomicLong transactionPrimaryKey;
    public static AtomicLong customerPrimaryKey;
    public static AtomicLong lineItemPrimaryKey;
    public static AtomicLong productPrimaryKey;
    public static AtomicLong categoryPrimaryKey;


    private SharedPreferences sharedPreferences;

    private static ProntoShopApplication instance = new ProntoShopApplication();
    private static AppComponent appComponent;

    public static ProntoShopApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance.bus = new EventBus();
        getAppComponent();
        initDefaultProducts();
        initRealm();
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("pronto_shop.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);

        Realm realm = Realm.getDefaultInstance();

        try {
            transactionPrimaryKey = new AtomicLong(realm.where(SalesTransaction.class).max("id").longValue());
        } catch (Exception e) {
            realm.beginTransaction();
            SalesTransaction transaction = new SalesTransaction();
            transaction.setId(0);
            realm.copyToRealm(transaction);
            transactionPrimaryKey = new AtomicLong(realm.where(SalesTransaction.class).max("id").longValue());
            RealmResults<SalesTransaction> results = realm.where(SalesTransaction.class).equalTo("id", 0).findAll();
            results.deleteAllFromRealm();
            realm.commitTransaction();
        }

        try {
            customerPrimaryKey = new AtomicLong(realm.where(Customer.class).max("id").longValue());
        } catch (Exception e) {
            //remove temp invoice
            realm.beginTransaction();
            //create temp Invoice so as to create the table
            Customer tempCustomer = new Customer();
            tempCustomer.setId(0);
            realm.copyToRealm(tempCustomer);
            customerPrimaryKey = new AtomicLong(realm.where(Customer.class).max("id").longValue());
            RealmResults<Customer> customers = realm.where(Customer.class).equalTo("id", 0).findAll();
            customers.deleteAllFromRealm();
            realm.commitTransaction();
        }

        try {
            lineItemPrimaryKey = new AtomicLong(realm.where(LineItem.class).max("id").longValue());
        } catch (Exception e) {
            //remove temp invoice
            realm.beginTransaction();
            //create temp Invoice so as to create the table
            LineItem tempLineItem = new LineItem();
            tempLineItem.setId(0);
            realm.copyToRealm(tempLineItem);
            lineItemPrimaryKey = new AtomicLong(realm.where(LineItem.class).max("id").longValue());
            RealmResults<LineItem> lineItems = realm.where(LineItem.class).equalTo("id", 0).findAll();
            lineItems.deleteAllFromRealm();
            realm.commitTransaction();
        }

        try {
            productPrimaryKey = new AtomicLong(realm.where(Product.class).max("id").longValue());
        } catch (Exception e) {
            //remove temp invoice
            realm.beginTransaction();
            //create temp Invoice so as to create the table
            Product tempProduct = new Product();
            tempProduct.setId(0);
            realm.copyToRealm(tempProduct);
            productPrimaryKey = new AtomicLong(realm.where(Product.class).max("id").longValue());
            RealmResults<Product> products = realm.where(Product.class).equalTo("id", 0).findAll();
            products.deleteAllFromRealm();
            realm.commitTransaction();
        }

        try {
            categoryPrimaryKey = new AtomicLong(realm.where(Category.class).max("id").longValue());
        } catch (Exception e) {
            //remove temp invoice
            realm.beginTransaction();
            //create temp Invoice so as to create the table
            Category tempCategory = new Category();
            tempCategory.setId(0);
            realm.copyToRealm(tempCategory);

            //Now set the primary key again
            categoryPrimaryKey = new AtomicLong(realm.where(Category.class).max("id").longValue() + 1);

            //remove temp category
            RealmResults<Category> categories = realm.where(Category.class).equalTo("id", 0).findAll();
            categories.deleteAllFromRealm();
            realm.commitTransaction();
        }

        realm.close();



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

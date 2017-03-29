package com.okason.prontoshop.core;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.okason.prontoshop.common.MainActivity;
import com.okason.prontoshop.core.listeners.OnDatabaseOperationCompleteListener;
import com.okason.prontoshop.data.SampleCustomerData;
import com.okason.prontoshop.data.SampleProductData;
import com.okason.prontoshop.data.sqlite.CustomerSQLiteRepository;
import com.okason.prontoshop.data.sqlite.ProductSQLiteRepository;
import com.okason.prontoshop.models.Category;
import com.okason.prontoshop.models.Customer;
import com.okason.prontoshop.models.Product;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;


public class AddInitialDataService extends IntentService {

    public AddInitialDataService() {
        super("AddInitialDataService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Category category = new Category();
        category.setCategoryName("Electronics");
        category.setId(ProntoShopApplication.categoryPrimaryKey.getAndIncrement());
        realm.copyToRealm(category);
        realm.commitTransaction();

        List<Product> products = SampleProductData.getSampleProducts();

        realm.beginTransaction();
        for (Product product : products){
            product.setId(ProntoShopApplication.productPrimaryKey.getAndIncrement());
            product.setCategoryName(category.getCategoryName());
            product.setCategoryId(category.getId());
            realm.copyToRealm(product);
        }
        realm.commitTransaction();

        Intent restartIntent = new Intent(this, MainActivity.class);
        restartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(restartIntent);


    }

}

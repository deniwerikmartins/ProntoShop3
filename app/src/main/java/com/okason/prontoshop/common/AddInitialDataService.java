package com.okason.prontoshop.common;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.okason.prontoshop.core.listeners.OnDatabaseOperationCompleteListener;
import com.okason.prontoshop.data.SampleCustomerData;
import com.okason.prontoshop.data.SampleProductData;
import com.okason.prontoshop.models.Customer;
import com.okason.prontoshop.models.Product;
import com.okason.prontoshop.ui.customers.CustomerSQLiteManager;
import com.okason.prontoshop.ui.products.ProductSQLiteManager;

import java.util.ArrayList;
import java.util.List;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class AddInitialDataService extends IntentService {


    public AddInitialDataService() {
        super("AddInitialDataService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //Add sample Customers to database
        List<Customer> customers = SampleCustomerData.getCustomers();
        CustomerSQLiteManager customerSQLiteManager = new CustomerSQLiteManager(getApplicationContext());
        for (Customer customer: customers){
            customerSQLiteManager.addCustomer(customer, new OnDatabaseOperationCompleteListener() {
                @Override
                public void onSQLOperationFailed(String error) {
                    Log.d("Customer", "Error" + error);
                }

                @Override
                public void onSQLOperationSucceded(String message) {
                    Log.d("Customer", "Customer Inserted");
                }
            });
        }

        //Add initial products
        List<Product> products = SampleProductData.getSampleProducts();
        ProductSQLiteManager productSQLiteManager = new ProductSQLiteManager(getApplicationContext());
        for (Product product : products) {
            productSQLiteManager.addProduct(product, new OnDatabaseOperationCompleteListener() {
                @Override
                public void onSQLOperationFailed(String error) {
                    Log.d("First Run", "Error: " + error);
                }

                @Override
                public void onSQLOperationSucceded(String message) {
                    Log.d("First Run", "Success: " + message);
                }
            });
        }

        //Add sample categories
        List<String> categories = new ArrayList<>();
        categories.add("Electronics");
        categories.add("Computers");
        categories.add("Toys");
        categories.add("Garden");
        categories.add("Kitchen");
        categories.add("Clothing");
        categories.add("Health");

        for (String category: categories){
            productSQLiteManager.createOrGetCategoryId(category, new OnDatabaseOperationCompleteListener() {
                @Override
                public void onSQLOperationFailed(String error) {

                }

                @Override
                public void onSQLOperationSucceded(String message) {

                }
            });
        }

        Intent restartIntent = new Intent(this, MainActivity.class);
        restartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(restartIntent);


    }

}

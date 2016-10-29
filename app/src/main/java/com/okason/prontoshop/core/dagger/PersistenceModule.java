package com.okason.prontoshop.core.dagger;

import android.content.Context;

import com.okason.prontoshop.data.sqlite.CustomerSQLiteRepository;
import com.okason.prontoshop.data.sqlite.LineItemSQLiteRepository;
import com.okason.prontoshop.data.sqlite.ProductSQLiteRepository;
import com.okason.prontoshop.data.sqlite.TransactionSQLiteRepository;
import com.okason.prontoshop.ui.checkout.CheckoutContract;
import com.okason.prontoshop.ui.customers.CustomerListContract;
import com.okason.prontoshop.ui.products.ProductListContract;
import com.okason.prontoshop.ui.transactions.TransactionContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Valentine on 4/18/2016.
 */
@Module
public class PersistenceModule {

    @Provides
    public ProductListContract.Repository providesProductRepository(Context context){
        return new ProductSQLiteRepository(context);
    }

    @Provides
    public CustomerListContract.Repository providesCustomerRepository(Context context){
        return new CustomerSQLiteRepository(context);
    }


    @Provides
    public TransactionContract.Repository providesTransactionRepository(Context context){
        return new TransactionSQLiteRepository(context);
    }

    @Provides
    public CheckoutContract.Repository providesLineItemRepository(Context context){
        return new LineItemSQLiteRepository(context);
    }
}

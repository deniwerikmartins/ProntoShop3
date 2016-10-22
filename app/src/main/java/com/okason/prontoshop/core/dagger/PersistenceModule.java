package com.okason.prontoshop.core.dagger;

import android.content.Context;

import com.okason.prontoshop.data.CustomerInMemoryRepository;
import com.okason.prontoshop.data.ProductInMemoryRepository;
import com.okason.prontoshop.data.TransactionInMemoryRepository;
import com.okason.prontoshop.ui.customers.CustomerListContract;
import com.okason.prontoshop.ui.products.ProductListContract;
import com.okason.prontoshop.ui.transaction.TransactionContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Valentine on 4/18/2016.
 */
@Module
public class PersistenceModule {

    @Provides
    public ProductListContract.Repository providesProductRepository(Context context){
        return new ProductInMemoryRepository();
    }

    @Provides
    public CustomerListContract.Repository providesCustomerRepository(Context context){
        return new CustomerInMemoryRepository();
    }


    @Provides
    public TransactionContract.Repository providesTransactionRepository(Context context){
        return new TransactionInMemoryRepository();
    }
}

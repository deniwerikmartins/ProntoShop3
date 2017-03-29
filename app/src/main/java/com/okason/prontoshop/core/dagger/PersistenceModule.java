package com.okason.prontoshop.core.dagger;

import android.content.Context;

import com.okason.prontoshop.common.ShoppingCart;
import com.okason.prontoshop.data.realm.CustomerRealmRepository;
import com.okason.prontoshop.data.realm.LineItemRealmRepository;
import com.okason.prontoshop.data.realm.ProductRealmRepository;
import com.okason.prontoshop.data.realm.TransactionRealmRepository;
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
        //return new ProductSQLiteRepository(context);
        return  new ProductRealmRepository();
    }

    @Provides
    public CustomerListContract.Repository providesCustomerRepository(Context context){
        //return new CustomerSQLiteRepository(context);
        return new CustomerRealmRepository();
    }


    @Provides
    public TransactionContract.Repository providesTransactionRepository(Context context, ShoppingCart mCart){
        //return new TransactionSQLiteRepository(context);
        return new TransactionRealmRepository(mCart);
    }

    @Provides
    public CheckoutContract.Repository providesLineItemRepository(Context context){
        //return new LineItemSQLiteRepository(context);
        return new LineItemRealmRepository();
    }
}

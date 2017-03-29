package com.okason.prontoshop.data.realm;

import com.okason.prontoshop.core.ProntoShopApplication;
import com.okason.prontoshop.core.listeners.OnDatabaseOperationCompleteListener;
import com.okason.prontoshop.models.Category;
import com.okason.prontoshop.models.Product;
import com.okason.prontoshop.ui.products.ProductListContract;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by deni on 29/03/2017.
 */

public class ProductRealmRepository implements ProductListContract.Repository {
    @Override
    public List<Product> getAllProducts() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Product> products = realm.where(Product.class).findAll();
        List<Product> result = realm.copyFromRealm(products);
        realm.close();
        return result;
    }

    @Override
    public Product getProductById(long id) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Product> products = realm.where(Product.class).equalTo("id", id).findAll();
        Product result = products.first();
        Product inMemoryProduct = realm.copyFromRealm(result);
        realm.close();
        return inMemoryProduct;
    }

    @Override
    public void deleteProduct(final Product product, final OnDatabaseOperationCompleteListener listener) {
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
                                          @Override
                                          public void execute(Realm backgroundRealm) {
                                              backgroundRealm.beginTransaction();
                                              Product productToBeDeleted = backgroundRealm.where(Product.class).equalTo("id", product.getId()).findFirst();
                                              productToBeDeleted.deleteFromRealm();
                                          }
                                      }, new Realm.Transaction.OnSuccess() {
                                          @Override
                                          public void onSuccess() {
                                              realm.close();
                                              listener.onSQLOperationSucceded("Deleted");
                                          }
                                      }, new Realm.Transaction.OnError() {
                                          @Override
                                          public void onError(Throwable error) {
                                              realm.close();
                                              listener.onSQLOperationFailed(error.getLocalizedMessage());
                                          }
                                      }
        );



    }

    @Override
    public void addProduct(final Product product, final OnDatabaseOperationCompleteListener listener) {
        final Realm realm = Realm.getDefaultInstance();

        final long id = ProntoShopApplication.productPrimaryKey.incrementAndGet();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm backgroundRealm) {
                product.setId(id);
                backgroundRealm.copyToRealmOrUpdate(product);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                realm.close();
                listener.onSQLOperationSucceded("Added");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                realm.close();
                listener.onSQLOperationFailed(error.getLocalizedMessage());
            }
        });


    }

    @Override
    public void updateProduct(final Product product, final OnDatabaseOperationCompleteListener listener) {

        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm backgroundRealm) {
                backgroundRealm.copyToRealmOrUpdate(product);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                realm.close();
                listener.onSQLOperationSucceded("Updated");

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                realm.close();
                listener.onSQLOperationFailed(error.getLocalizedMessage());
            }
        });


    }

    @Override
    public List<Category> getAllCategories() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Category> categories = realm.where(Category.class).findAll();
        List<Category> result = realm.copyFromRealm(categories);
        realm.close();
        return result;
    }
}

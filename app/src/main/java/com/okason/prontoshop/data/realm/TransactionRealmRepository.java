package com.okason.prontoshop.data.realm;

import com.okason.prontoshop.common.ShoppingCart;
import com.okason.prontoshop.core.listeners.OnDatabaseOperationCompleteListener;
import com.okason.prontoshop.models.LineItem;
import com.okason.prontoshop.models.SalesTransaction;
import com.okason.prontoshop.ui.transactions.TransactionContract;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Valentine on 10/24/2016.
 */

public class TransactionRealmRepository implements TransactionContract.Repository {


    private final ShoppingCart mCart;




    public TransactionRealmRepository(ShoppingCart mCart) {
        this.mCart = mCart;
    }

    @Override
    public List<LineItem> getAllLineItems() {
        return mCart.getShoppingCart();
    }


    @Override
    public long saveTransaction(final SalesTransaction transaction, final OnDatabaseOperationCompleteListener listener) {
        //ensure that the database exists
        final Realm realm = Realm.getDefaultInstance();
        if (transaction != null){
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm backgroundRealm) {
                    backgroundRealm.copyToRealmOrUpdate(transaction);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    listener.onSQLOperationSucceded("Saved");
                    realm.close();
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    listener.onSQLOperationFailed(error.getMessage());
                    realm.close();
                }
            });
        }

        return 0;
    }

    @Override
    public List<SalesTransaction> getAllSalesTransactions() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<SalesTransaction> transactions = realm.where(SalesTransaction.class).findAll();
        List<SalesTransaction> result = realm.copyFromRealm(transactions);
        realm.close();
        return result;
    }

    @Override
    public void updateTransaction(final SalesTransaction transaction, final OnDatabaseOperationCompleteListener listener) {

        final Realm updateRealm = Realm.getDefaultInstance();
        if (transaction != null){
            updateRealm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(transaction);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    listener.onSQLOperationSucceded("Updated");
                    try {
                        updateRealm.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    listener.onSQLOperationFailed(error.getMessage());
                    try {
                        updateRealm.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    @Override
    public SalesTransaction getTransactionById(long id) {

        Realm realm = Realm.getDefaultInstance();
        SalesTransaction transaction = realm.where(SalesTransaction.class).equalTo("id", id).findFirst();
        SalesTransaction inMemoryInvoice = realm.copyFromRealm(transaction);
        realm.close();
        return inMemoryInvoice;
    }

    @Override
    public void deleteTransaction(final long id, final OnDatabaseOperationCompleteListener listener) {
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm backgroundRealm) {
                RealmResults<SalesTransaction> invoices = backgroundRealm.where(SalesTransaction.class).findAll();
                SalesTransaction invoiceToBeDeleted = invoices.where().equalTo("id", id).findFirst();
                invoiceToBeDeleted.deleteFromRealm();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                realm.close();
                listener.onSQLOperationSucceded("Saved");

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                realm.close();
                listener.onSQLOperationFailed(error.getMessage());
            }
        });
    }


}

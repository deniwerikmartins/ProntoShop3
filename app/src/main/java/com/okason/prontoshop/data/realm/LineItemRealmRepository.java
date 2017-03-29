package com.okason.prontoshop.data.realm;

import com.okason.prontoshop.core.ProntoShopApplication;
import com.okason.prontoshop.core.listeners.OnDatabaseOperationCompleteListener;
import com.okason.prontoshop.models.LineItem;
import com.okason.prontoshop.ui.checkout.CheckoutContract;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Valentine on 10/29/2016.
 */

public class LineItemRealmRepository implements CheckoutContract.Repository {



    public LineItemRealmRepository() {


    }


    @Override
    public List<LineItem> getAllLineItemsInATransaction(long transactionId) {
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<LineItem> query = realm.where(LineItem.class);
        query.equalTo("transactionId", transactionId);
        RealmResults<LineItem> result = query.findAll();
        List<LineItem> lineItems = realm.copyFromRealm(result);
        realm.close();
        return lineItems;
    }



    @Override
    public long saveLineItem(final LineItem lineItem, final OnDatabaseOperationCompleteListener listener) {
        Realm insertRealm = Realm.getDefaultInstance();
        final long id = ProntoShopApplication.lineItemPrimaryKey.getAndIncrement();
        if (lineItem != null){
            insertRealm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realmBg) {
                    lineItem.setId(id);
                    realmBg.copyToRealm(lineItem);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    listener.onSQLOperationSucceded("Saved");
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    listener.onSQLOperationFailed(error.getMessage());
                }
            });
        }
        insertRealm.close();
        return id;
    }

    @Override
    public void updateLineItem(final LineItem lineItem, final OnDatabaseOperationCompleteListener listener) {
        //ensure that the database exists

        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm backgroundRealm) {
                backgroundRealm.copyToRealmOrUpdate(lineItem);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                realm.close();
                listener.onSQLOperationSucceded("LineItem updated");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                realm.close();
                listener.onSQLOperationFailed(error.getMessage());
            }

        });

    }

    @Override
    public LineItem getLineItemById(long id) {
        Realm realm = Realm.getDefaultInstance();
        LineItem lineItem = realm.where(LineItem.class).equalTo("id", id).findFirst();
        LineItem inMemoryLineItem = realm.copyFromRealm(lineItem);
        realm.close();
        return inMemoryLineItem;
    }

    @Override
    public void deleteLineItem(final long id, OnDatabaseOperationCompleteListener listener) {

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<LineItem> lineItems = realm.where(LineItem.class).findAll();
                lineItems.where().equalTo("id", id);
                lineItems.deleteAllFromRealm();
            }
        });
        realm.close();

    }
}

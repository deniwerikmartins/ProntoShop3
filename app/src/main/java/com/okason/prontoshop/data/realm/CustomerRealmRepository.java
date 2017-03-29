package com.okason.prontoshop.data.realm;

import com.okason.prontoshop.core.ProntoShopApplication;
import com.okason.prontoshop.core.listeners.OnDatabaseOperationCompleteListener;
import com.okason.prontoshop.models.Customer;
import com.okason.prontoshop.ui.customers.CustomerListContract;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static android.R.attr.id;

/**
 * Created by Valentine on 10/24/2016.
 */

public class CustomerRealmRepository implements CustomerListContract.Repository {


    private final static String LOG_TAG = CustomerRealmRepository.class.getSimpleName();

    public CustomerRealmRepository() {

    }

    @Override
    public List<Customer> getAllCustomers() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Customer> customers = realm.where(Customer.class).findAll();
        customers = customers.sort("id", Sort.DESCENDING);
        List<Customer> result = realm.copyFromRealm(customers);
        realm.close();
        return result;
    }

    @Override
    public Customer getCustomerById(long id) {
        Realm realm = Realm.getDefaultInstance();
        Customer inMemoryCustomer = null;
        try {
            Customer customer = realm.where(Customer.class).equalTo("id", id).findFirst();
            inMemoryCustomer = realm.copyFromRealm(customer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        realm.close();
        return inMemoryCustomer;
    }

    @Override
    public void deleteCustomer(Customer customer, final OnDatabaseOperationCompleteListener listener) {
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm backgroundRealm) {
                RealmResults<Customer> clients = backgroundRealm.where(Customer.class).findAll();
                Customer clientToBeDeleted = clients.where().equalTo("id", id).findFirst();
                clientToBeDeleted.deleteFromRealm();
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
                listener.onSQLOperationSucceded(error.getMessage());
            }
        });

    }

    @Override
    public void addCustomer(final Customer customer, final OnDatabaseOperationCompleteListener listener) {
        final Realm insertRealm = Realm.getDefaultInstance();
        final long id = ProntoShopApplication.customerPrimaryKey.incrementAndGet();
        insertRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm backgroundRealm) {
                customer.setId(id);
                backgroundRealm.copyToRealm(customer);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                insertRealm.close();
                listener.onSQLOperationSucceded("Saved");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                insertRealm.close();
                listener.onSQLOperationFailed(error.getMessage());
            }
        });

    }

    @Override
    public void updateCustomer(Customer customer, OnDatabaseOperationCompleteListener listener) {

    }



}

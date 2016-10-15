package com.okason.prontoshop.ui.addCustomer;

import com.okason.prontoshop.core.ProntoShopApplication;
import com.okason.prontoshop.core.events.CustomerListChangedEvent;
import com.okason.prontoshop.core.listeners.OnDatabaseOperationCompleteListener;
import com.okason.prontoshop.models.Customer;
import com.okason.prontoshop.ui.customers.CustomerListContract;
import com.squareup.otto.Bus;

import javax.inject.Inject;

/**
 * Created by Valentine on 4/7/2016.
 */
public class AddCustomerPresenter implements AddCustomerContract.Action, OnDatabaseOperationCompleteListener{
    private final AddCustomerContract.View mView;
    @Inject CustomerListContract.Repository mRepository;
    @Inject Bus mBus;
    private long customerId = 0;

    public AddCustomerPresenter(AddCustomerContract.View view) {
        mView = view;
        ProntoShopApplication.getInstance().getAppComponent().inject(this);
    }

    @Override
    public void onAddCustomerButtonClick(Customer customer) {
        if (customerId > 0){
            updatedCustomer(customer);
        }else {
            saveCustomer(customer);
        }
    }

    @Override
    public void checkStatus(long id) {
        if (id > 0) {
            if (mRepository.getCustomerById(id) != null){
                customerId = id;
                mView.setEditMode(true);
                mView.populateForm(mRepository.getCustomerById(id));
            }
        }
    }

    @Override
    public void saveCustomer(Customer customer) {
        if (customerId > 0){
            customer.setId(customerId);
            updatedCustomer(customer);
        }else {
            mRepository.addCustomer(customer, this);
        }

    }

    @Override
    public void updatedCustomer(Customer customer) {
        customer.setId(customerId);
        mRepository.updateCustomer(customer,this);
    }

    @Override
    public void onSQLOperationFailed(String error) {
        mView.displayMessage("Error: " + error);
    }

    @Override
    public void onSQLOperationSucceded(String message) {
        mView.displayMessage(message);
        mBus.post(new CustomerListChangedEvent());
    }
}

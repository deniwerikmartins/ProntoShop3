package com.okason.prontoshop.core.listeners;

import com.okason.prontoshop.models.Customer;
import com.okason.prontoshop.models.SalesTransaction;

/**
 * Created by Valentine on 4/24/2016.
 */
public interface OnTransactionSelectedListener {
    void onSelectTransaction(SalesTransaction transaction);
    Customer getCustomer(long id);
}

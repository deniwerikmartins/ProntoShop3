package com.okason.prontoshop.core.listeners;

import com.okason.prontoshop.models.Customer;

/**
 * Created by Valentine on 4/8/2016.
 */
public interface OnCustomerSelectedListener {
    void onSelectCustomer(Customer customer);
    void onLongClickProduct(Customer clickedCustomer);
}

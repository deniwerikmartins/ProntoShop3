package com.okason.prontoshop.core.events;

import com.okason.prontoshop.models.Customer;

/**
 * Created by Valentine on 4/10/2016.
 */
public class CustomerSelectedEvent {
    private final Customer selectedCustomer;
    private final boolean clearCustomer;

    public CustomerSelectedEvent(Customer selectedCustomer, boolean clearCustomer) {
        this.selectedCustomer = selectedCustomer;
        this.clearCustomer = clearCustomer;
    }

    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    public boolean isClearCustomer() {
        return clearCustomer;
    }
}

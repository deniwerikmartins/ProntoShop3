package com.okason.prontoshop.ui.addCustomer;

import com.okason.prontoshop.models.Customer;

/**
 * Created by Valentine on 4/7/2016.
 */
public class AddCustomerContract {
    interface View{
        void populateForm(Customer customer);
        void displayMessage(String message);
        void setEditMode(boolean editMode);
        void clearForm();
    }

    interface Action{
        void onAddCustomerButtonClick(Customer customer);
        void checkStatus(long id);
        void saveCustomer(Customer customer);
        void updatedCustomer(Customer customer);

    }
}

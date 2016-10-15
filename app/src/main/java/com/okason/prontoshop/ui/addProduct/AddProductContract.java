package com.okason.prontoshop.ui.addProduct;

import com.okason.prontoshop.models.Product;

import java.util.List;

/**
 * Created by Valentine on 4/7/2016.
 */
public class AddProductContract {
    interface View{
        void populateForm(Product product);
        void displayMessage(String message);
        void setEditMode(boolean editMode);
        void clearForm();
    }

    interface Action{
        void onAddProductButtonClick(Product product);
        void checkStatus(long id);
        void saveProduct(Product product);
        void updatedProduct(Product product);
        List<String> getCategoryNames();
    }
}

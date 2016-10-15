package com.okason.prontoshop.ui.addProduct;

import com.okason.prontoshop.core.ProntoShopApplication;
import com.okason.prontoshop.core.events.ProductListChangedEvent;
import com.okason.prontoshop.core.listeners.OnDatabaseOperationCompleteListener;
import com.okason.prontoshop.models.Category;
import com.okason.prontoshop.models.Product;
import com.okason.prontoshop.ui.products.ProductListContract;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Valentine on 4/7/2016.
 */
public class AddProductPresenter implements AddProductContract.Action, OnDatabaseOperationCompleteListener{
    private final AddProductContract.View mView;
    private long productId = 0;
    @Inject ProductListContract.Repository mRepository;
    @Inject
    Bus mBus;

    public AddProductPresenter(AddProductContract.View view) {
        mView = view;
        ProntoShopApplication.getInstance().getAppComponent().inject(this);
        mBus.register(this);
    }

    @Override
    public void onAddProductButtonClick(Product product) {

            if (productId > 0){
                product.setId(productId);
                updatedProduct(product);
            }else {
                saveProduct(product);            }


    }



    @Override
    public void checkStatus(long id) {
        if (id > 0) {
            if (mRepository.getProductById(id) != null){
                productId = id;
                mView.setEditMode(true);
                mView.populateForm(mRepository.getProductById(id));
            }
        }
    }

    @Override
    public void saveProduct(Product product) {
        mRepository.addProduct(product, this);
    }

    @Override
    public void updatedProduct(Product product) {
        mRepository.updateProduct(product, this);
    }

    @Override
    public List<String> getCategoryNames() {
        List<String> categoryNames = new ArrayList<>();
        List<Category> categories = mRepository.getAllCategories();

        for (Category category: categories){
            categoryNames.add(category.getCategoryName());
        }
        return categoryNames;
    }

    @Override
    public void onSQLOperationFailed(String error) {
        mView.displayMessage("Error: " + error);
    }

    @Override
    public void onSQLOperationSucceded(String message) {
        mView.displayMessage(message);
        mBus.post(new ProductListChangedEvent());
    }
}

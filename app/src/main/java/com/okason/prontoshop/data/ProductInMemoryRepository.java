package com.okason.prontoshop.data;

import com.okason.prontoshop.core.listeners.OnDatabaseOperationCompleteListener;
import com.okason.prontoshop.models.Category;
import com.okason.prontoshop.models.Product;
import com.okason.prontoshop.ui.products.ProductListContract;

import java.util.List;

/**
 * Created by Valentine on 10/22/2016.
 */

public class ProductInMemoryRepository implements ProductListContract.Repository {
    @Override
    public List<Product> getAllProducts() {
        return SampleProductData.getSampleProducts();
    }

    @Override
    public Product getProductById(long id) {
        return null;
    }

    @Override
    public void deleteProduct(Product product, OnDatabaseOperationCompleteListener listener) {

    }

    @Override
    public void addProduct(Product product, OnDatabaseOperationCompleteListener listener) {

    }

    @Override
    public void updateProduct(Product product, OnDatabaseOperationCompleteListener listener) {

    }

    @Override
    public List<Category> getAllCategories() {
        return null;
    }
}

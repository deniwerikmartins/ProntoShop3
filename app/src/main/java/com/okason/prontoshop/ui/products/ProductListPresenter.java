package com.okason.prontoshop.ui.products;

import com.okason.prontoshop.common.ShoppingCart;
import com.okason.prontoshop.core.ProntoShopApplication;
import com.okason.prontoshop.core.events.ProductListChangedEvent;
import com.okason.prontoshop.core.listeners.OnDatabaseOperationCompleteListener;
import com.okason.prontoshop.models.LineItem;
import com.okason.prontoshop.models.Product;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Valentine on 4/6/2016.
 */
public class ProductListPresenter implements ProductListContract.Actions, OnDatabaseOperationCompleteListener {
    private final ProductListContract.View mView;
    @Inject ProductListContract.Repository mRepository;
    @Inject ShoppingCart mCart;
    @Inject Bus mBus;


    public ProductListPresenter(ProductListContract.View mView) {
        this.mView = mView;
        ProntoShopApplication.getInstance().getAppComponent().inject(this);
        mBus.register(this);
    }


    @Override
    public void loadProducts() {
        List<Product> availableProducts = mRepository.getAllProducts();
        if (availableProducts != null && availableProducts.size() > 0){
            mView.hideEmptyText();
            mView.showProducts(availableProducts);

        }else {
            mView.showEmptyText();
        }

    }

    @Override
    public void onAddProductButtonClicked() {
        mView.showAddProductForm();
    }

    @Override
    public void onAddToCartButtonClicked(Product product) {
        //perform add to checkout button here
        LineItem item = new LineItem(product, 1);
        mCart.addItemToCart(item);
    }

    @Override
    public Product getProduct(long id) {
        return mRepository.getProductById(id);
    }

    @Override
    public void addProduct(Product product) {
        mRepository.addProduct(product, this);
    }

    @Override
    public void onDeleteProductButtonClicked(Product product) {
        mView.showDeleteProductPrompt(product);
    }

    @Override
    public void deleteProduct(Product product) {
        mRepository.deleteProduct(product, this);
        loadProducts();
    }

    @Override
    public void onEditProductButtonClicked(Product product) {
        //mView.showEditProductForm(product);
        mView.showEditProductForm(product);
    }

    @Override
    public void updateProduct(Product product) {
        mRepository.updateProduct(product, this);
    }

    @Subscribe
    public void onProductListChanged(ProductListChangedEvent event){
        loadProducts();
    }



    @Override
    public void onGoogleSearchButtonClicked(Product product) {
        mView.showGoogleSearch(product);
    }


    @Override
    public void onSQLOperationFailed(String error) {
        mView.showMessage(error);
    }

    @Override
    public void onSQLOperationSucceded(String message) {
        mView.showMessage(message);
    }
}

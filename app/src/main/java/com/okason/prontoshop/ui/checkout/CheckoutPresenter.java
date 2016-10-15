package com.okason.prontoshop.ui.checkout;

import android.util.Log;

import com.okason.prontoshop.common.ShoppingCart;
import com.okason.prontoshop.core.ProntoShopApplication;
import com.okason.prontoshop.core.listeners.OnDatabaseOperationCompleteListener;
import com.okason.prontoshop.models.LineItem;
import com.okason.prontoshop.models.SalesTransaction;
import com.okason.prontoshop.ui.transaction.TransactionContract;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Valentine on 4/7/2016.
 */
public class CheckoutPresenter implements CheckoutContract.Actions, OnDatabaseOperationCompleteListener {
    private final CheckoutContract.View mView;
    @Inject TransactionContract.Repository mRepository;
    private final static String LOG_TAG = CheckoutPresenter.class.getSimpleName();
    private final static boolean DEBUG = true;

    private double subtotal = 0.0;
    private double total;
    private double tax;
    private double taxRate = 0.08;
    private String selectedPaymentType = "";
    private boolean paid = false;


    @Inject
    ShoppingCart mCart;

    public CheckoutPresenter(CheckoutContract.View cartView) {
        this.mView = cartView;
        ProntoShopApplication.getInstance().getAppComponent().inject(this);
    }


    @Override
    public void loadLineItems() {
        List<LineItem> availableProducts = mCart.getShoppingCart();
        calculateTotals(availableProducts);
        if (availableProducts != null && availableProducts.size() > 0){
            mView.hideEmptyText();
            mView.showLineItem(availableProducts);
        }else {
            mView.showEmptyText();
        }

    }

    private void calculateTotals(List<LineItem> availableProducts) {
        subtotal = 0.0;
        total = 0.0;
        tax = 0.0;

        for (LineItem item: availableProducts){
            subtotal += item.getSumPrice();
        }

        tax = subtotal * taxRate;
        total = tax + subtotal;
        mView.showCartTotals(tax, subtotal, total);
    }


    @Override
    public void onCheckoutButtonClicked() {
        mView.showConfirmCheckout();
    }

    @Override
    public void onDeleteItemButtonClicked(LineItem item) {
        //Ensure there is an existing Shopping Cart
        mCart.removeItemFromCart(item);
        loadLineItems();
    }

    @Override
    public void checkout() {
        //Ensure a customer is selected
        if (mCart.getShoppingCart() == null || mCart.getShoppingCart().size() == 0){
            mView.showMessage("Cart is empty");
            return;
        }
        if (mCart.getSelectedCustomer() == null || mCart.getSelectedCustomer().getId() == 0){
            mView.showMessage("No Customer selected");
            return;
        }

        SalesTransaction transaction = new SalesTransaction();
        transaction.setCustomerId(mCart.getSelectedCustomer().getId());
        transaction.setLineItems(mCart.getShoppingCart());
        transaction.setTaxAmount(tax);
        transaction.setSubTotalAmount(subtotal);
        transaction.setTotalAmount(total);
        transaction.setPaymentType(selectedPaymentType);
        transaction.setPaid(paid);
        mRepository.saveTransaction(transaction, this);



        mCart.clearShoppingCart();
        loadLineItems();
    }

    @Override
    public void onClearButtonClicked() {
        mView.showConfirmClearCart();
    }

    @Override
    public void clearShoppingCart() {
        mCart.clearShoppingCart();
        loadLineItems();
    }

    @Override
    public void setPaymentType(String paymentType) {
        if (DEBUG){
            Log.d(LOG_TAG, "Set Payment Type: " + paymentType);
        }
        selectedPaymentType = paymentType;
    }

    @Override
    public void markAsPaid(boolean isPaid) {
        paid = isPaid;
    }


    @Override
    public void onItemQuantityChanged(LineItem item, int qty) {
        mCart.updateItemQty(item, qty);
        loadLineItems();
    }

    public CheckoutContract.View getView() {
        return mView;
    }



    @Override
    public void onSQLOperationFailed(String error) {
        mView.showMessage("Error: " + error);
    }

    @Override
    public void onSQLOperationSucceded(String message) {
        mView.showMessage(message);
    }
}

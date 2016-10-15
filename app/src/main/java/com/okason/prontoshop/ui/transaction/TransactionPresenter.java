package com.okason.prontoshop.ui.transaction;

import com.okason.prontoshop.core.ProntoShopApplication;
import com.okason.prontoshop.core.listeners.OnDatabaseOperationCompleteListener;
import com.okason.prontoshop.models.Customer;
import com.okason.prontoshop.models.SalesTransaction;
import com.okason.prontoshop.ui.customers.CustomerListContract;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Valentine on 4/24/2016.
 */
public class TransactionPresenter implements TransactionContract.Actions, OnDatabaseOperationCompleteListener {
    private final TransactionContract.View mView;
    @Inject TransactionContract.Repository mRepository;
    @Inject CustomerListContract.Repository mCustomerRepository;

    public TransactionPresenter(TransactionContract.View view) {
        mView = view;
        ProntoShopApplication.getInstance().getAppComponent().inject(this);
    }


    @Override
    public void loadSalesTransactions() {
        List<SalesTransaction> transactions = mRepository.getAllSalesTransactions();
        if (transactions != null && transactions.size() > 0){
            mView.hideEmptyText();
            mView.showSalesTransaction(transactions);
        }else {
            mView.showEmptyText();
        }

    }

    @Override
    public void onDeleteItemButtonClicked(SalesTransaction transaction) {
        mView.showConfirmDeletePrompt(transaction);
    }

    @Override
    public void editTransaction(SalesTransaction transaction) {
        mRepository.updateTransaction(transaction, this);
    }

    @Override
    public void deleteTransaction(SalesTransaction transaction) {
        mRepository.deleteTransaction(transaction.getId(), this);
    }

    @Override
    public Customer getCustomerById(long id) {
        return mCustomerRepository.getCustomerById(id);
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

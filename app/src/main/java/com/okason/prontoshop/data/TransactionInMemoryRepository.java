package com.okason.prontoshop.data;

import com.okason.prontoshop.core.listeners.OnDatabaseOperationCompleteListener;
import com.okason.prontoshop.models.LineItem;
import com.okason.prontoshop.models.SalesTransaction;
import com.okason.prontoshop.ui.transaction.TransactionContract;

import java.util.List;

/**
 * Created by Valentine on 10/22/2016.
 */

public class TransactionInMemoryRepository implements TransactionContract.Repository {
    @Override
    public List<LineItem> getAllLineItems() {
        return null;
    }

    @Override
    public void saveTransaction(SalesTransaction transaction, OnDatabaseOperationCompleteListener listener) {

    }

    @Override
    public List<SalesTransaction> getAllSalesTransactions() {
        return null;
    }

    @Override
    public void updateTransaction(SalesTransaction transaction, OnDatabaseOperationCompleteListener listener) {

    }

    @Override
    public SalesTransaction getTransactionById(long id) {
        return null;
    }

    @Override
    public void deleteTransaction(long id, OnDatabaseOperationCompleteListener listener) {

    }
}

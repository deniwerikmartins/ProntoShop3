package com.okason.prontoshop.ui.transaction;

import com.okason.prontoshop.core.listeners.OnDatabaseOperationCompleteListener;
import com.okason.prontoshop.models.Customer;
import com.okason.prontoshop.models.LineItem;
import com.okason.prontoshop.models.SalesTransaction;

import java.util.List;

/**
 * Created by Valentine on 4/24/2016.
 */
public interface TransactionContract {

    public interface View{
        void showSalesTransaction(List<SalesTransaction> transactions);
        void showEmptyText();
        void hideEmptyText();
        void showConfirmDeletePrompt(SalesTransaction transaction);
        void showMessage(String message);

    }

    public interface Actions{
        void loadSalesTransactions();
        void onDeleteItemButtonClicked(SalesTransaction transaction);
        void editTransaction(SalesTransaction transaction);
        void deleteTransaction(SalesTransaction transaction);
        Customer getCustomerById(long id);
    }

    public interface Repository{
        List<LineItem> getAllLineItems();
        void saveTransaction(SalesTransaction transaction, OnDatabaseOperationCompleteListener listener);
        List<SalesTransaction> getAllSalesTransactions();
        void updateTransaction(SalesTransaction transaction, OnDatabaseOperationCompleteListener listener);
        SalesTransaction getTransactionById(long id);
        void deleteTransaction(long id, OnDatabaseOperationCompleteListener listener);


    }

}

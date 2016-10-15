package com.okason.prontoshop.ui.transaction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.okason.prontoshop.common.ShoppingCart;
import com.okason.prontoshop.core.ProntoShopApplication;
import com.okason.prontoshop.core.listeners.OnDatabaseOperationCompleteListener;
import com.okason.prontoshop.data.DatabaseHelper;
import com.okason.prontoshop.models.LineItem;
import com.okason.prontoshop.models.SalesTransaction;
import com.okason.prontoshop.util.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Valentine on 4/24/2016.
 */
public class TransactionSQLiteManager implements TransactionContract.Repository {
    private final Context mContext;
    private DatabaseHelper DbHelper;
    private SQLiteDatabase database;
    private boolean DEBUG = false;
    private final static String LOG_TAG = TransactionSQLiteManager.class.getSimpleName();
    @Inject
    ShoppingCart mCart;

    public TransactionSQLiteManager(Context context) {
        mContext = context;
        DbHelper = DatabaseHelper.newInstance(mContext);
        database =  DbHelper.getWritableDatabase();
        ProntoShopApplication.getInstance().getAppComponent().inject(this);
    }


    @Override
    public List<LineItem> getAllLineItems() {
        return mCart.getShoppingCart();
    }

    @Override
    public void saveTransaction(SalesTransaction transaction, OnDatabaseOperationCompleteListener listener) {
        //ensure that the database exists
        if (database != null) {
            //prepare the transaction information that will be saved to the database
            ContentValues values = new ContentValues();
            values.put(Constants.COLUMN_CUSTOMER_ID, transaction.getCustomerId());
            values.put(Constants.COLUMN_LINE_ITEMS, transaction.getJsonLineItems());
            values.put(Constants.COLUMN_PAYMENT_STATUS, transaction.isPaid());
            values.put(Constants.COLUMN_PAYMENT_TYPE, transaction.getPaymentType());
            values.put(Constants.COLUMN_TAX_AMOUNT, transaction.getTaxAmount());
            values.put(Constants.COLUMN_SUB_TOTAL_AMOUNT, transaction.getSubTotalAmount());
            values.put(Constants.COLUMN_TOTAL_AMOUNT, transaction.getTotalAmount());
            values.put(Constants.COLUMN_DATE_CREATED, System.currentTimeMillis());
            values.put(Constants.COLUMN_LAST_UPDATED, System.currentTimeMillis());
            try {
                database.insertOrThrow(Constants.TRANSACTION_TABLE, null, values);
                listener.onSQLOperationSucceded("Transaction saved");
                if (DEBUG){
                    Log.d(LOG_TAG, "Transaction saved");
                }
            } catch (SQLException e) {
                listener.onSQLOperationFailed(e.getCause() + " " + e.getMessage());
            }
        }

    }

    @Override
    public List<SalesTransaction> getAllSalesTransactions() {
        //initialize an empty list of transactions
        List<SalesTransaction> transactions = new ArrayList<>();

        //sql command to select all SalesTransactions;
        String selectQuery = "SELECT * FROM " + Constants.TRANSACTION_TABLE;

        //make sure the database is not empty
        if (database != null) {

            //get a cursor for all lineItems in the database
            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    //get each transaction in the cursor
                    transactions.add(SalesTransaction.getSalesTransactionFromCursor(cursor));
                    cursor.moveToNext();
                }
            }
        }

        return transactions;
    }

    @Override
    public void updateTransaction(SalesTransaction transaction, OnDatabaseOperationCompleteListener listener) {

        //ensure that the database exists
        if (database != null) {
            //prepare the transaction information that will be saved to the database
            ContentValues values = new ContentValues();
            values.put(Constants.COLUMN_CUSTOMER_ID, transaction.getCustomerId());
            values.put(Constants.COLUMN_LINE_ITEMS, transaction.getJsonLineItems());
            values.put(Constants.COLUMN_PAYMENT_STATUS, convertBooleanToInt(transaction.isPaid()));
            values.put(Constants.COLUMN_PAYMENT_TYPE, transaction.getPaymentType());
            values.put(Constants.COLUMN_TAX_AMOUNT, transaction.getTaxAmount());
            values.put(Constants.COLUMN_SUB_TOTAL_AMOUNT, transaction.getSubTotalAmount());
            values.put(Constants.COLUMN_TOTAL_AMOUNT, transaction.getTotalAmount());
            values.put(Constants.COLUMN_DATE_CREATED, System.currentTimeMillis());
            values.put(Constants.COLUMN_LAST_UPDATED, System.currentTimeMillis());

            //Now update the this row with the information supplied
            int result =  database.update(Constants.TRANSACTION_TABLE, values,
                    Constants.COLUMN_ID + " = " + transaction.getId(), null);
            if (result == 1){
                listener.onSQLOperationSucceded("Transaction Updated");
            }else{
                listener.onSQLOperationFailed("Transaction Update Failed");
            }
        }
    }

    @Override
    public SalesTransaction getTransactionById(long id) {

        //Get the cursor representing the SalesTransaction
        Cursor cursor = database.rawQuery("SELECT * FROM " + Constants.TRANSACTION_TABLE + " WHERE " +
                Constants.COLUMN_ID + " = '" + id + "'", null);

        //Create a variable of data type SalesTransaction
        SalesTransaction transaction;


        if (cursor.moveToFirst()) {
            transaction = SalesTransaction.getSalesTransactionFromCursor(cursor);
        } else {
            transaction = null;
        }

        //Return result: either a valid transaction or null
        return transaction;
    }

    @Override
    public void deleteTransaction(long id, OnDatabaseOperationCompleteListener listener) {
        // Ensure database exists.
        if (database != null) {
            int result = database.delete(Constants.TRANSACTION_TABLE, Constants.COLUMN_ID + " = " + id, null);

            if (result > 0) {
                listener.onSQLOperationSucceded("Transaction Deleted");
            } else {
                listener.onSQLOperationFailed("Unable to Delete Transaction");
            }

        }

    }

    public int convertBooleanToInt(Boolean boolValue) {
        if (boolValue) {
            return 1;
        }else{
            return 0;
        }

    }
}

package com.okason.prontoshop.ui.sales;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.okason.prontoshop.core.listeners.OnDatabaseOperationCompleteListener;
import com.okason.prontoshop.data.DatabaseHelper;
import com.okason.prontoshop.models.SalesTransaction;
import com.okason.prontoshop.util.Constants;

/**
 * Created by Valentine on 4/17/2016.
 */
public class TransactionSQLiteManager {

    private final Context mContext;
    private DatabaseHelper DbHelper;

    public TransactionSQLiteManager(Context context) {
        mContext = context;
        DbHelper = DatabaseHelper.newInstance(mContext);
    }

    public void deleteTransaction(SalesTransaction transaction, OnDatabaseOperationCompleteListener listener) {
        // Get a writable database.
        SQLiteDatabase database = DbHelper.getWritableDatabase();

        // Ensure database exists.
        if (database != null) {
            int result = database.delete(Constants.TRANSACTION_TABLE, Constants.COLUMN_ID + " = " + transaction.getId(), null);

            if (result > 0) {
                listener.onSQLOperationSucceded("Product Deleted");
            } else {
                listener.onSQLOperationFailed("Unable to Delete Product");
            }
            // Close database connection.
            database.close();
        }

    }
}

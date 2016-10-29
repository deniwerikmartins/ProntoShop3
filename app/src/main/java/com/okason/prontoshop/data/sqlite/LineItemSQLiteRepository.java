package com.okason.prontoshop.data.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.okason.prontoshop.core.listeners.OnDatabaseOperationCompleteListener;
import com.okason.prontoshop.data.DatabaseHelper;
import com.okason.prontoshop.models.LineItem;
import com.okason.prontoshop.ui.checkout.CheckoutContract;

import java.util.List;

/**
 * Created by Valentine on 10/29/2016.
 */

public class LineItemSQLiteRepository implements CheckoutContract.Repository {

    private final Context mContext;
    private DatabaseHelper DbHelper;
    private boolean DEBUG = false;
    private final static String LOG_TAG = ProductSQLiteRepository.class.getSimpleName();
    private SQLiteDatabase database;

    public LineItemSQLiteRepository(Context context) {
        mContext = context;
        DbHelper = DatabaseHelper.newInstance(mContext);
        database = DbHelper.getWritableDatabase();
    }


    @Override
    public List<LineItem> getAllLineItemsInATransaction(long transactionId) {
        return null;
    }

    @Override
    public long saveLineItem(LineItem lineItem, OnDatabaseOperationCompleteListener listener) {
        return 0;
    }

    @Override
    public void updateLineItem(LineItem lineItem, OnDatabaseOperationCompleteListener listener) {

    }

    @Override
    public LineItem getLineItemById(long id) {
        return null;
    }

    @Override
    public void deleteLineItem(long id, OnDatabaseOperationCompleteListener listener) {

    }
}

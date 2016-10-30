package com.okason.prontoshop.data.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.okason.prontoshop.core.ProntoShopApplication;
import com.okason.prontoshop.core.listeners.OnDatabaseOperationCompleteListener;
import com.okason.prontoshop.data.DatabaseHelper;
import com.okason.prontoshop.models.LineItem;
import com.okason.prontoshop.models.Product;
import com.okason.prontoshop.ui.checkout.CheckoutContract;
import com.okason.prontoshop.ui.products.ProductListContract;
import com.okason.prontoshop.util.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Valentine on 10/29/2016.
 */

public class LineItemSQLiteRepository implements CheckoutContract.Repository {

    private final Context mContext;
    private DatabaseHelper DbHelper;
    private SQLiteDatabase database;
    private boolean DEBUG = false;
    private final static String LOG_TAG = ProductSQLiteRepository.class.getSimpleName();

    @Inject ProductListContract.Repository mProductRepository;

    public LineItemSQLiteRepository(Context context) {
        mContext = context;
        DbHelper = DatabaseHelper.newInstance(mContext);
        database = DbHelper.getWritableDatabase();
        ProntoShopApplication.getInstance().getAppComponent().inject(this);
    }


    @Override
    public List<LineItem> getAllLineItemsInATransaction(long transactionId) {
        List<LineItem> lineItems = new ArrayList<LineItem>();

        //Command to select all Categories
        String selectQuery = "SELECT * FROM " + Constants.LINEITEM_TABLE + "WHERE " + Constants.COLUMN_TRANSACTION_ID + " = " + transactionId;

        //Get a cursor for all Categories in the database
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                lineItems.add(getLineItemFromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return lineItems;
    }

    private LineItem getLineItemFromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_ID));
        int qty = cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_QUANTITY));
        long productId = cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_PRODUCT_ID));
        long transactionId = cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_PRODUCT_ID));
        Product product = mProductRepository.getProductById(productId);
        LineItem item = new LineItem(product, qty);
        item.setId(id);
        item.setTransactionId(transactionId);
        return item;
    }

    @Override
    public long saveLineItem(LineItem lineItem, OnDatabaseOperationCompleteListener listener) {
        //ensure that the database exists
        long result = -1;
        if (database != null) {
            //prepare the transaction information that will be saved to the database
            ContentValues values = new ContentValues();
            values.put(Constants.COLUMN_QUANTITY, lineItem.getQuantity());
            values.put(Constants.COLUMN_TRANSACTION_ID, lineItem.getTransactionId());
            values.put(Constants.COLUMN_PRODUCT_ID, lineItem.getProductId());
            values.put(Constants.COLUMN_DATE_CREATED, System.currentTimeMillis());
            values.put(Constants.COLUMN_LAST_UPDATED, System.currentTimeMillis());
            try {
             result = database.insertOrThrow(Constants.LINEITEM_TABLE, null, values);
                listener.onSQLOperationSucceded("LineItem Added");
            } catch (SQLException e) {
                listener.onSQLOperationFailed(e.getCause() + " " + e.getMessage());
            }
        }
        return result;
    }

    @Override
    public void updateLineItem(LineItem lineItem, OnDatabaseOperationCompleteListener listener) {
        //ensure that the database exists

        if (database != null) {
            //prepare the transaction information that will be saved to the database
            ContentValues values = new ContentValues();
            values.put(Constants.COLUMN_QUANTITY, lineItem.getQuantity());
            values.put(Constants.COLUMN_TRANSACTION_ID, lineItem.getTransactionId());
            values.put(Constants.COLUMN_PRODUCT_ID, lineItem.getProductId());
            values.put(Constants.COLUMN_DATE_CREATED, System.currentTimeMillis());
            values.put(Constants.COLUMN_LAST_UPDATED, System.currentTimeMillis());
            try {
                //Now update the this row with the information supplied
                int result =  database.update(Constants.LINEITEM_TABLE, values,
                        Constants.COLUMN_ID + " = " + lineItem.getId(), null);
                listener.onSQLOperationSucceded("LineItem Updated");
            } catch (SQLException e) {
                listener.onSQLOperationFailed(e.getCause() + " " + e.getMessage());
            }
        }

    }

    @Override
    public LineItem getLineItemById(long id) {
        //Get the cursor representing the Product
        Cursor cursor = database.rawQuery("SELECT * FROM " + Constants.LINEITEM_TABLE + " WHERE " +
                Constants.COLUMN_ID + " = '" + id + "'", null);

        //Create a variable of data type Product
        LineItem lineItem;
        if (cursor.moveToFirst()){
            lineItem = getLineItemFromCursor(cursor);
        }else {
            lineItem = null;
        }
        cursor.close();
        //Return result: either a valid product or null
        return  lineItem;
    }

    @Override
    public void deleteLineItem(long id, OnDatabaseOperationCompleteListener listener) {

        // Ensure database exists.
        if (database != null) {
            int result = database.delete(Constants.LINEITEM_TABLE, Constants.COLUMN_ID + " = " + id, null);

            if (result > 0) {
                listener.onSQLOperationSucceded("LineItem Deleted");
            } else {
                listener.onSQLOperationFailed("Unable to Delete Product");
            }

        }

    }
}

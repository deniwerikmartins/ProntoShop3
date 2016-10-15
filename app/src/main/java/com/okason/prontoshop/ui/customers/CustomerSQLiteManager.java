package com.okason.prontoshop.ui.customers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.okason.prontoshop.core.listeners.OnDatabaseOperationCompleteListener;
import com.okason.prontoshop.data.DatabaseHelper;
import com.okason.prontoshop.models.Customer;
import com.okason.prontoshop.ui.products.ProductSQLiteManager;
import com.okason.prontoshop.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valentine on 4/21/2016.
 */
public class CustomerSQLiteManager implements CustomerListContract.Repository {
    private final Context mContext;
    private SQLiteDatabase database;
    private DatabaseHelper DbHelper;
    private boolean DEBUG = false;
    private final static String LOG_TAG = ProductSQLiteManager.class.getSimpleName();

    public CustomerSQLiteManager(Context context) {
        mContext = context;
        DbHelper = DatabaseHelper.newInstance(mContext);
        database = DbHelper.getWritableDatabase();
    }

    @Override
    public List<Customer> getAllCustomers() {
        //initialize an empty list of customers
        List<Customer> customers = new ArrayList<>();

        //sql command to select all Customers;
        String selectQuery = "SELECT * FROM " + Constants.CUSTOMER_TABLE;

        //make sure the database is not empty
        if (database != null) {

            //get a cursor for all customers in the database
            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    //get each customer in the cursor
                    customers.add(Customer.getCustomerFromCursor(cursor));
                    cursor.moveToNext();
                }
            }
        }


        return customers;
    }

    @Override
    public Customer getCustomerById(long id) {
        //Get the cursor representing the Customer
        Cursor cursor = database.rawQuery("SELECT * FROM " + Constants.CUSTOMER_TABLE + " WHERE " +
                Constants.COLUMN_ID + " = '" + id + "'", null);

        //Create a variable of data type Customer
        Customer customer;
        if (cursor.moveToFirst()){
            customer = Customer.getCustomerFromCursor(cursor);
        }else {
            customer = null;
        }

        //Return result: either a valid customer or null
        return  customer;
    }

    @Override
    public void deleteCustomer(Customer customer, OnDatabaseOperationCompleteListener listener) {
        // Ensure database exists.
        if (database != null) {
            int result = database.delete(Constants.CUSTOMER_TABLE, Constants.COLUMN_ID + " = " + customer.getId(), null);

            if (result > 0) {
                listener.onSQLOperationSucceded("Customer Deleted");
            } else {
                listener.onSQLOperationFailed("Unable to Delete Customer");
            }

        }
    }

    @Override
    public void addCustomer(Customer customer, OnDatabaseOperationCompleteListener listener) {
        if (database != null){
            //prepare the transaction information that will be saved to the database
            ContentValues values = new ContentValues();
            values.put(Constants.COLUMN_NAME, customer.getCustomerName());
            values.put(Constants.COLUMN_EMAIL, customer.getEmailAddress());
            values.put(Constants.COLUMN_PHONE, customer.getPhoneNumber());
            values.put(Constants.COLUMN_IMAGE_PATH, customer.getProfileImagePath());
            values.put(Constants.COLUMN_STREET1, customer.getStreetAddress());
            values.put(Constants.COLUMN_STREET2, customer.getStreetAddress2());
            values.put(Constants.COLUMN_CITY, customer.getCity());
            values.put(Constants.COLUMN_STATE, customer.getCity());
            values.put(Constants.COLUMN_ZIP, customer.getPostalCode());
            values.put(Constants.COLUMN_NOTE, customer.getNote());
            values.put(Constants.COLUMN_DATE_CREATED, System.currentTimeMillis());
            values.put(Constants.COLUMN_LAST_UPDATED, System.currentTimeMillis());
            try {
                database.insertOrThrow(Constants.CUSTOMER_TABLE, null, values);
                listener.onSQLOperationSucceded("Customer Added");
                if (DEBUG){
                    Log.d(LOG_TAG, "Customer Added");
                }
            } catch (SQLException e) {
                listener.onSQLOperationFailed(e.getCause() + " " + e.getMessage());
            }
        }
    }

    @Override
    public void updateCustomer(Customer customer, OnDatabaseOperationCompleteListener listener) {
        if (database != null){
            //prepare the transaction information that will be saved to the database
            ContentValues values = new ContentValues();
            values.put(Constants.COLUMN_NAME, customer.getCustomerName());
            values.put(Constants.COLUMN_EMAIL, customer.getEmailAddress());
            values.put(Constants.COLUMN_PHONE, customer.getPhoneNumber());
            values.put(Constants.COLUMN_IMAGE_PATH, customer.getProfileImagePath());
            values.put(Constants.COLUMN_STREET1, customer.getStreetAddress());
            values.put(Constants.COLUMN_STREET2, customer.getStreetAddress2());
            values.put(Constants.COLUMN_CITY, customer.getCity());
            values.put(Constants.COLUMN_STATE, customer.getCity());
            values.put(Constants.COLUMN_ZIP, customer.getPostalCode());
            values.put(Constants.COLUMN_NOTE, customer.getNote());
            values.put(Constants.COLUMN_LAST_UPDATED, System.currentTimeMillis());
            //Now update the this row with the information supplied
            int result =  database.update(Constants.CUSTOMER_TABLE, values,
                    Constants.COLUMN_ID + " = " + customer.getId(), null);
            if (result == 1){
                listener.onSQLOperationSucceded("Customer Updated");
            }else{
                listener.onSQLOperationFailed("Customer Update Failed");
            }
        }
    }
}

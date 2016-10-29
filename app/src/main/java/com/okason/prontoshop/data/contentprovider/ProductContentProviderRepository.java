package com.okason.prontoshop.data.contentprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.okason.prontoshop.core.listeners.OnDatabaseOperationCompleteListener;
import com.okason.prontoshop.data.DatabaseHelper;
import com.okason.prontoshop.models.Category;
import com.okason.prontoshop.models.Product;
import com.okason.prontoshop.ui.products.ProductListContract;
import com.okason.prontoshop.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valentine on 10/24/2016.
 */

public class ProductContentProviderRepository implements ProductListContract.Repository{
    private final Context mContext;
    private DatabaseHelper DbHelper;
    private boolean DEBUG = false;
    private final static String LOG_TAG = ProductContentProviderRepository.class.getSimpleName();
    private SQLiteDatabase database;



    public ProductContentProviderRepository(Context context) {
        mContext = context;
        DbHelper = DatabaseHelper.newInstance(mContext);
        database = DbHelper.getWritableDatabase();
    }


    @Override
    public List<Product> getAllProducts() {
        //initialize an empty list of customers
        List<Product> products = new ArrayList<>();

        //sql command to select all Products;
        String selectQuery = "SELECT * FROM " + Constants.PRODUCT_TABLE;

        //make sure the database is not empty
        if (database != null) {

            //get a cursor for all products in the database
            Cursor cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    //get each product in the cursor
                    products.add(Product.getProductFromCursor(cursor));
                    cursor.moveToNext();
                }
            }
        }

        return products;
    }

    @Override
    public Product getProductById(long id) {
        //Get the cursor representing the Product
        Cursor cursor = database.rawQuery("SELECT * FROM " + Constants.PRODUCT_TABLE + " WHERE " +
                Constants.COLUMN_ID + " = '" + id + "'", null);

        //Create a variable of data type Product
        Product product;


        if (cursor.moveToFirst()){
            product = Product.getProductFromCursor(cursor);
        }else {
            product = null;
        }

        //Return result: either a valid product or null
        return  product;
    }

    @Override
    public void deleteProduct(Product product, OnDatabaseOperationCompleteListener listener) {

        // Ensure database exists.
        if (database != null) {
            int result = database.delete(Constants.PRODUCT_TABLE, Constants.COLUMN_ID + " = " + product.getId(), null);

            if (result > 0) {
                listener.onSQLOperationSucceded("Product Deleted");
            } else {
                listener.onSQLOperationFailed("Unable to Delete Product");
            }

        }

    }

    @Override
    public void addProduct(Product product, OnDatabaseOperationCompleteListener listener) {

        //ensure that the database exists
        if (database != null) {
            //prepare the transaction information that will be saved to the database
            ContentValues values = new ContentValues();
            values.put(Constants.COLUMN_NAME, product.getProductName());
            values.put(Constants.COLUMN_DESCRIPTION, product.getDescription());
            values.put(Constants.COLUMN_PRICE, product.getSalePrice());
            values.put(Constants.COLUMN_PURCHASE_PRICE, product.getPurchasePrice());
            values.put(Constants.COLUMN_IMAGE_PATH, product.getImagePath());
            values.put(Constants.COLUMN_CATEGORY_ID, createOrGetCategoryId(product.getCategoryName(), listener));
            values.put(Constants.COLUMN_CATEGORY_NAME, product.getCategoryName());
            values.put(Constants.COLUMN_DATE_CREATED, System.currentTimeMillis());
            values.put(Constants.COLUMN_LAST_UPDATED, System.currentTimeMillis());
            try {
                database.insertOrThrow(Constants.PRODUCT_TABLE, null, values);
                listener.onSQLOperationSucceded("Product Added");
                if (DEBUG){
                    Log.d(LOG_TAG, "Product Added");
                }
            } catch (SQLException e) {
                listener.onSQLOperationFailed(e.getCause() + " " + e.getMessage());
            }
        }

    }

    @Override
    public void updateProduct(Product product, OnDatabaseOperationCompleteListener listener) {
        //Ensure that the database exists
        if (database != null) {
            //Prepare the transaction information that will be saved to the database
            ContentValues values = new ContentValues();
            values.put(Constants.COLUMN_NAME, product.getProductName());
            values.put(Constants.COLUMN_DESCRIPTION, product.getDescription());
            values.put(Constants.COLUMN_PRICE, product.getSalePrice());
            values.put(Constants.COLUMN_PURCHASE_PRICE, product.getPurchasePrice());
            values.put(Constants.COLUMN_IMAGE_PATH, product.getImagePath());
            values.put(Constants.COLUMN_CATEGORY_ID, product.getCategoryId());
            values.put(Constants.COLUMN_CATEGORY_NAME, product.getCategoryName());
            values.put(Constants.COLUMN_LAST_UPDATED, System.currentTimeMillis());

            //Now update the this row with the information supplied
            int result =  database.update(Constants.PRODUCT_TABLE, values,
                    Constants.COLUMN_ID + " = " + product.getId(), null);
            if (result == 1){
                listener.onSQLOperationSucceded("Product Updated");
            }else{
                listener.onSQLOperationFailed("Product Update Failed");
            }
        }
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<Category>();

        //Command to select all Categories
        String selectQuery = "SELECT * FROM " + Constants.CATEGORY_TABLE;

        //Get a cursor for all Categories in the database
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                categories.add(Category.fromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return categories;
    }



    public long createOrGetCategoryId(final String category, OnDatabaseOperationCompleteListener listener) {
        Category foundCategory = getCategory(category);
        if(foundCategory == null) {
            foundCategory = addCategory(category, listener);
        }
        return foundCategory.getId();
    }

    private Category addCategory(final String categoryName, OnDatabaseOperationCompleteListener listener) {
        Category category = new Category();
        category.setCategoryName(categoryName);
        saveCategory(category, listener);
        return category;
    }

    private void saveCategory(Category category, OnDatabaseOperationCompleteListener listener) {

        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_NAME, category.getCategoryName());
        try {
            database.insertOrThrow(Constants.CATEGORY_TABLE, null, values);
        } catch (SQLException e) {
            listener.onSQLOperationFailed("Unable to save Category");

        }

    }

    private Category getCategory(final String categoryName) {

        Category category = null;
        Cursor cursor = database.rawQuery("SELECT * FROM " + Constants.CATEGORY_TABLE + " " +
                "WHERE " + Constants.COLUMN_NAME + " = '" + categoryName + "'", null);
        if (cursor.moveToFirst()){
            category = Category.fromCursor(cursor);
        }

        return category;
    }

}

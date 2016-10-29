package com.okason.prontoshop.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.okason.prontoshop.util.Constants;

import java.util.Arrays;
import java.util.HashSet;

public class ProntoShopContentProvider extends ContentProvider {

    private DatabaseHelper dbHelper;
    private Context mContext;

    private static final String BASE_PATH_TRANSACTION = "transactions";
    private static final String BASE_PATH_CATEGORY = "category";
    private static final String BASE_PATH_PRODUCT = "product";
    private static final String BASE_PATH_CUSTOMER = "customer";

    private static final int TRANSACTION = 100;
    private static final int TRANSACTIONS = 101;

    private static final int PRODUCT = 200;
    private static final int PRODUCTS = 201;

    private static final int CUSTOMER = 300;
    private static final int CUSTOMERS = 301;

    private static final int CATEGORY = 400;
    private static final int CATEGORIES = 401;

    private static final String AUTHORITY = "com.okason.prontoshop.data.provider";

    public static final Uri CONTENT_URI_INVOICE = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_TRANSACTION);
    public static final Uri CONTENT_URI_CAT = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_CATEGORY);
    public static final Uri CONTENT_URI_PRODUCT = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_PRODUCT);
    public static final Uri CONTENT_URI_CUSTOMER = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_CUSTOMER);

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH_TRANSACTION, TRANSACTIONS);
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH_TRANSACTION + "/#", TRANSACTION);
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH_CATEGORY, CATEGORIES);
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH_CATEGORY + "/#", CATEGORY);
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH_PRODUCT, PRODUCTS);
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH_PRODUCT + "/#", PRODUCT);
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH_CUSTOMER, CUSTOMERS);
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH_CUSTOMER + "/#", CUSTOMER);

    }


    private void checkColumns(String[] projection) {
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));

            HashSet<String> availableTransactionColumns = new HashSet<String>(Arrays.asList(Constants.COLUMNS_TRANSACTION));
            HashSet<String> availableCategoryColumns = new HashSet<String>(Arrays.asList(Constants.COLUMNS_CATEGORY));
            HashSet<String> availableCustomerColumns = new HashSet<String>(Arrays.asList(Constants.COLUMNS_CUSTOMER));
            HashSet<String> availableProductColumns = new HashSet<String>(Arrays.asList(Constants.COLUMNS_PRODUCT));

            if (!availableTransactionColumns.containsAll(requestedColumns) &&
                    !availableCustomerColumns.containsAll(requestedColumns) &&
                    !availableProductColumns.containsAll(requestedColumns) &&
                    !availableCategoryColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }





    @Override
    public boolean onCreate() {
        mContext = getContext();
        dbHelper = DatabaseHelper.newInstance(mContext);        ;
        return false;
    }

    public ProntoShopContentProvider() {

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int type = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int affectedRows;
        switch (type){
            case TRANSACTIONS:
                affectedRows = db.delete(Constants.TRANSACTION_TABLE, selection, selectionArgs);
                break;
            case TRANSACTION:
                String invoice_id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    affectedRows = db.delete(Constants.TRANSACTION_TABLE, selection, null);
                } else {
                    affectedRows = db.delete(Constants.TRANSACTION_TABLE, selection , selectionArgs );
                }
                break;
            case PRODUCT:
                affectedRows = db.delete(Constants.PRODUCT_TABLE, selection, selectionArgs);
                break;
            case PRODUCTS:
                String prodId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    affectedRows = db.delete(Constants.PRODUCT_TABLE, selection, null);
                } else {
                    affectedRows = db.delete(Constants.PRODUCT_TABLE, selection , selectionArgs );
                }
                break;
            case CUSTOMER:
                affectedRows = db.delete(Constants.CUSTOMER_TABLE, selection, null);
                break;
            case CUSTOMERS:
                String cust_id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    affectedRows = db.delete(Constants.CUSTOMER_TABLE, selection, null);
                } else {
                    affectedRows = db.delete(Constants.CUSTOMER_TABLE, selection , selectionArgs );
                }
                break;
            case CATEGORIES:
                affectedRows = db.delete(Constants.CATEGORY_TABLE, selection, selectionArgs);
                break;
            case CATEGORY:
                String catId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    affectedRows = db.delete(Constants.CATEGORY_TABLE, selection, null);
                } else {
                    affectedRows = db.delete(Constants.CATEGORY_TABLE, selection , selectionArgs );
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return affectedRows;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int type = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Long id;
        switch (type){
            case TRANSACTIONS:
                id = db.insert(Constants.TRANSACTION_TABLE, null, values);
                break;
            case CATEGORIES:
                id = db.insert(Constants.CATEGORY_TABLE, null, values);
                break;
            case CUSTOMERS:
                try {
                    id = db.insertOrThrow(Constants.CUSTOMER_TABLE, null, values);
                } catch (Exception e) {
                    id = -1L;
                    e.printStackTrace();
                }
                break;
            case PRODUCTS:
                id = db.insert(Constants.PRODUCT_TABLE, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.parse(BASE_PATH_TRANSACTION + "/" + id);
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        checkColumns(projection);

        int type = URI_MATCHER.match(uri);
        switch (type){
            case TRANSACTIONS:
                queryBuilder.setTables(Constants.TRANSACTION_TABLE);
                break;
            case TRANSACTION:
                queryBuilder.setTables(Constants.TRANSACTION_TABLE);
                queryBuilder.appendWhere(Constants.COLUMN_ID + " = " + uri.getLastPathSegment());
                break;
            case PRODUCTS:
                queryBuilder.setTables(Constants.PRODUCT_TABLE);
                break;
            case PRODUCT:
                queryBuilder.setTables(Constants.PRODUCT_TABLE);
                queryBuilder.appendWhere(Constants.COLUMN_ID + " = " + uri.getLastPathSegment());
                break;
            case CUSTOMERS:
                queryBuilder.setTables(Constants.CUSTOMER_TABLE);
                break;
            case CUSTOMER:
                queryBuilder.setTables(Constants.CUSTOMER_TABLE);
                queryBuilder.appendWhere(Constants.COLUMN_ID + " = " + uri.getLastPathSegment());
                break;
            case CATEGORIES:
                queryBuilder.setTables(Constants.CATEGORY_TABLE);
                break;
            case CATEGORY:
                queryBuilder.setTables(Constants.CATEGORY_TABLE);
                queryBuilder.appendWhere(Constants.COLUMN_ID + " = " + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int type = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int affectedRows;
        switch (type){
            case TRANSACTIONS:
                affectedRows = db.update(Constants.TRANSACTION_TABLE, values, selection, selectionArgs);
                break;
            case TRANSACTION:
                String invoice_id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    affectedRows = db.update(Constants.TRANSACTION_TABLE, values, Constants.COLUMN_ID + "=" + invoice_id, null);
                } else {
                    affectedRows = db.update(Constants.TRANSACTION_TABLE, values, selection , selectionArgs );
                }
                break;
            case PRODUCT:
                affectedRows = db.update(Constants.PRODUCT_TABLE, values, selection, selectionArgs);
                break;
            case PRODUCTS:
                String prodId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    affectedRows = db.update(Constants.PRODUCT_TABLE, values, Constants.COLUMN_ID + "=" + prodId, null);
                } else {
                    affectedRows = db.update(Constants.PRODUCT_TABLE, values, selection , selectionArgs );
                }
                break;
            case CUSTOMER:
                affectedRows = db.update(Constants.CUSTOMER_TABLE, values, selection, selectionArgs);
                break;
            case CUSTOMERS:
                String cust_id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    affectedRows = db.update(Constants.CUSTOMER_TABLE, values, null, null);
                } else {
                    affectedRows = db.update(Constants.CUSTOMER_TABLE, values, selection , selectionArgs );
                }
                break;
            case CATEGORIES:
                affectedRows = db.update(Constants.CATEGORY_TABLE, values, selection, selectionArgs);
                break;
            case CATEGORY:
                String catId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)){
                    affectedRows = db.update(Constants.CATEGORY_TABLE, values, Constants.COLUMN_ID + "=" + catId, null);
                } else {
                    affectedRows = db.update(Constants.CATEGORY_TABLE, values, selection , selectionArgs );
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return affectedRows;
    }
}

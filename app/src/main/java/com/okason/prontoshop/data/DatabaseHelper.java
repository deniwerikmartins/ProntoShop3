package com.okason.prontoshop.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.okason.prontoshop.util.Constants;

/**
 * Created by Valentine on 4/8/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    private final static String LOG_TAG = DatabaseHelper.class.getSimpleName();
    private final static int DATABASE_VERSION= 1;
    public static final String DATABASE_NAME = "pronto_shop.db";

    private static DatabaseHelper mDatabaseInstance = null;


    public static DatabaseHelper newInstance(Context context){
        //first check to see if the database helper
        //member data is null
        //create a new one if it is null

        if (mDatabaseInstance == null){
            mDatabaseInstance = new DatabaseHelper(context.getApplicationContext());
        }

        //either way we have to always return an instance of
        //our database class each time this method is called
        return mDatabaseInstance;
    }


    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_CUSTOMER_TABLE);
        db.execSQL(CREATE_TRANSACTION_TABLE);
        db.execSQL(CREATE_RETAILER_TABLE);
        db.execSQL(CREATE_PRODUCT_TABLE);
        Log.d(LOG_TAG, "Create Product: " + CREATE_PRODUCT_TABLE);
        Log.d(LOG_TAG, "Create Customer: " + CREATE_CUSTOMER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



    //String to create a customer table
    private static final String CREATE_CUSTOMER_TABLE =
            "CREATE TABLE " + Constants.CUSTOMER_TABLE + "("
                    + Constants.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Constants.COLUMN_NAME + " TEXT NOT NULL, "
                    + Constants.COLUMN_EMAIL + " TEXT, "
                    + Constants.COLUMN_IMAGE_PATH + " TEXT, "
                    + Constants.COLUMN_PHONE + " TEXT, "
                    + Constants.COLUMN_STREET1 + " TEXT, "
                    + Constants.COLUMN_STREET2 + " TEXT, "
                    + Constants.COLUMN_CITY + " TEXT, "
                    + Constants.COLUMN_STATE + " TEXT, "
                    + Constants.COLUMN_ZIP + " TEXT, "
                    + Constants.COLUMN_NOTE + " TEXT, "
                    + Constants.COLUMN_DATE_CREATED + " BIGINT, "
                    + Constants.COLUMN_LAST_UPDATED + " BIGINT " + ")";



    //String to create a product table
    private static final String CREATE_PRODUCT_TABLE =
            "CREATE TABLE " + Constants.PRODUCT_TABLE + "("
                    + Constants.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Constants.COLUMN_NAME + " TEXT NOT NULL, "
                    + Constants.COLUMN_DESCRIPTION + " TEXT, "
                    + Constants.COLUMN_PROMO_MESSAGE + " TEXT, "
                    + Constants.COLUMN_PRICE + " NUMERIC, "
                    + Constants.COLUMN_PURCHASE_PRICE + " NUMERIC, "
                    + Constants.COLUMN_IMAGE_PATH + " TEXT, "
                    + Constants.COLUMN_CATEGORY_ID + " INTEGER, "
                    + Constants.COLUMN_CATEGORY_NAME + " TEXT, "
                    + Constants.COLUMN_DATE_CREATED + " BIGINT, "
                    + Constants.COLUMN_LAST_UPDATED + " BIGINT, "
                    + "FOREIGN KEY(category_id) REFERENCES category(_id)" + ")";



    //String to create a retailer table
    private static final String CREATE_RETAILER_TABLE =
            "CREATE TABLE " + Constants.RETAILER_TABLE + "("
                    + Constants.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Constants.COLUMN_NAME + " TEXT NOT NULL, "
                    + Constants.COLUMN_EMAIL + " TEXT, "
                    + Constants.COLUMN_PHONE + " TEXT, "
                    + Constants.COLUMN_STREET1 + " TEXT, "
                    + Constants.COLUMN_STREET2 + " TEXT, "
                    + Constants.COLUMN_CITY + " TEXT, "
                    + Constants.COLUMN_STATE + " TEXT, "
                    + Constants.COLUMN_ZIP + " TEXT, "
                    + Constants.COLUMN_INDUSTRY + " TEXT, "
                    + Constants.COLUMN_DATE_CREATED + " BIGINT, "
                    + Constants.COLUMN_LAST_UPDATED + " BIGINT, "
                    + Constants.COLUMN_CONTACT_PERSON + " TEXT " + ")";



    //String to create a transaction table
    private static final String CREATE_TRANSACTION_TABLE =
            "CREATE TABLE " + Constants.TRANSACTION_TABLE + "("
                    + Constants.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Constants.COLUMN_CUSTOMER_ID + " INTEGER, "
                    + Constants.COLUMN_DATE_CREATED + " BIGINT, "
                    + Constants.COLUMN_SUB_TOTAL_AMOUNT + " NUMERIC, "
                    + Constants.COLUMN_LINE_ITEMS + " TEXT, "
                    + Constants.COLUMN_TAX_AMOUNT + " NUMERIC, "
                    + Constants.COLUMN_PAYMENT_STATUS + " INTEGER, "
                    + Constants.COLUMN_PAYMENT_TYPE + " TEXT, "
                    + Constants.COLUMN_TOTAL_AMOUNT + " NUMERIC, "
                    + Constants.COLUMN_LAST_UPDATED + " BIGINT, "
                    + "FOREIGN KEY(customer_id) REFERENCES customer(_id)" + ")";


    //String to create a category table
    private static final String CREATE_CATEGORY_TABLE =
            "CREATE TABLE " + Constants.CATEGORY_TABLE + "("
                    + Constants.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Constants.COLUMN_NAME + " TEXT NOT NULL, "
                    + Constants.COLUMN_IMAGE_PATH + " TEXT, "
                    + Constants.COLUMN_DATE_CREATED + " BIGINT, "
                    + Constants.COLUMN_LAST_UPDATED + " BIGINT "  + ")";

}

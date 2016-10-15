package com.okason.prontoshop.util;

/**
 * Created by Valentine on 4/9/2016.
 */
public class Constants {

    public final static int CART = 1;
    public final static int REPORT = 2;
    public final static int SETTINGS = 3;
    public final static int TRANSACTIONS = 4;
    public final static int PURCHASE = 5;


    public static final String PRODUCT_TABLE = "product";
    public static final String CUSTOMER_TABLE = "customer";
    public static final String RETAILER_TABLE = "retailer";
    public static final String CATEGORY_TABLE = "category";
    public static final String TRANSACTION_TABLE = "transactions";


    //Create constants for column names of Customer Table
    public final static String COLUMN_ID = "_id";
    public final static String COLUMN_NAME = "name";
    public final static String COLUMN_EMAIL = "email";
    public final static String COLUMN_PHONE = "phone";
    public final static String COLUMN_STREET1 = "street1";
    public final static String COLUMN_STREET2 = "street2";
    public final static String COLUMN_CITY = "city";
    public final static String COLUMN_STATE = "state";
    public final static String COLUMN_ZIP = "zip";
    public final static String COLUMN_NOTE = "note";
    public final static String COLUMN_DATE_CREATED = "create_date";
    public final static String COLUMN_LAST_UPDATED = "last_update_date";


    //Create constants for column names of SalesTransaction Table
    public final static String COLUMN_CUSTOMER_ID = "customer_id";
    public final static String COLUMN_SUB_TOTAL_AMOUNT = "sub_total_amount";
    public final static String COLUMN_TAX_AMOUNT = "tax_amount";
    public final static String COLUMN_TOTAL_AMOUNT = "total_amount";
    public final static String COLUMN_LINE_ITEMS = "items";


    //Create constants for column names of Product Table
    public final static String COLUMN_DESCRIPTION = "description";
    public final static String COLUMN_PRICE = "price";
    public final static String COLUMN_IMAGE_PATH = "image_path";
    public final static String COLUMN_CATEGORY_ID = "category_id";

    //Create constants for column names of RetailerService
    public final  static String COLUMN_INDUSTRY = "industry";
    public final  static String COLUMN_CONTACT_PERSON = "manager_name";

    public static final String COLUMN_PURCHASE_PRICE = "purchase_price";
    public static final String COLUMN_PROMO_MESSAGE = "promo_message";
    public static final String COLUMN_CATEGORY_NAME = "category_name";
    public static final String COLUMN_CHECKOUT_COMPLETED = "checkout_completed";
    public static final String PREFERENCE_FILE = "preference_file";
    public static final String FIRST_RUN = "first_run";
    public static final String OPEN_CART_EXITS = "open_cart_exists";
    public static final String SERIALIZED_CART_ITEMS = "serialized_cart_items";
    public static final String SERIALIZED_CUSTOMER = "serialized_customer";
    public static final String COLUMN_PAYMENT_TYPE = "payment_type";
    public static final String COLUMN_PAYMENT_STATUS = "payment_status";

}

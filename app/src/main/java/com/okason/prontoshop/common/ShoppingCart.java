package com.okason.prontoshop.common;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okason.prontoshop.core.ProntoShopApplication;
import com.okason.prontoshop.core.events.CustomerSelectedEvent;
import com.okason.prontoshop.core.events.UpdateToolbarEvent;
import com.okason.prontoshop.models.Customer;
import com.okason.prontoshop.models.LineItem;
import com.okason.prontoshop.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valentine on 4/18/2016.
 */
public class ShoppingCart {
    private final SharedPreferences sharedPreferences;
    private Customer selectedCustomer;
    private SharedPreferences.Editor editor;
    private List<LineItem> shoppingCart;

    private final static String LOG_TAG = ShoppingCart.class.getSimpleName();
    private static boolean DEBUG = true;

    public ShoppingCart(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        editor = sharedPreferences.edit();
        initShoppingCart();
    }


    private void initShoppingCart() {
        shoppingCart = new ArrayList<>();
        selectedCustomer = new Customer();
        Gson gson = new Gson();

        if (sharedPreferences.getBoolean(Constants.OPEN_CART_EXITS, false)){
            String serializedCartItems = sharedPreferences.getString(Constants.SERIALIZED_CART_ITEMS,"");
            if (DEBUG){
                Log.d(LOG_TAG, "Serialized Cart Items: " + serializedCartItems);
            }
            String serializedCustomer = sharedPreferences.getString(Constants.SERIALIZED_CUSTOMER,"");
            if (DEBUG){
                Log.d(LOG_TAG, "Serialized Customer: " + serializedCustomer);
            }
            if (!serializedCartItems.equals("")){
                shoppingCart = gson.<ArrayList<LineItem>>fromJson(serializedCartItems,
                        new TypeToken<ArrayList<LineItem>>(){}.getType());
            }

            if (!serializedCustomer.equals("")){
                selectedCustomer = gson.fromJson(serializedCustomer, Customer.class);
            }
        }
        populateToolbar();
        if (shoppingCart != null && shoppingCart.size() > 0 && selectedCustomer != null && selectedCustomer.getCustomerName() != null){
            if (DEBUG){
                Log.d(LOG_TAG, "Customer: " + selectedCustomer.getCustomerName());
            }
            setCustomer(selectedCustomer);
        }

    }

    public void addItemToCart(LineItem item) {
        boolean isItemInCart = false;
        int itemPosition = 0;
        for (LineItem tempItem : shoppingCart) {
            if (tempItem.getId() == item.getId()) {
                itemPosition = shoppingCart.indexOf(tempItem);
                LineItem selectedItem = shoppingCart.get(itemPosition);
                selectedItem.setQuantity(tempItem.getQuantity() + item.getQuantity());
                shoppingCart.set(itemPosition, selectedItem);
                isItemInCart = true;
                break;
            }
        }
        if (!isItemInCart) {
            shoppingCart.add(item);
        }
        populateToolbar();
    }

    public void clearShoppingCart(){
        if (DEBUG) {
            Log.d(LOG_TAG, "Clear Cart Called");
        }
        shoppingCart.clear();
        selectedCustomer = null;
        editor.putString(Constants.SERIALIZED_CART_ITEMS, "").commit();
        editor.putString(Constants.SERIALIZED_CUSTOMER, "").commit();
        editor.putBoolean(Constants.OPEN_CART_EXITS, false).commit();
        populateToolbar();
        ProntoShopApplication.getInstance()
                .getBus().post(new CustomerSelectedEvent(new Customer(), true));
    }

    public void removeItemFromCart(LineItem item){
        shoppingCart.remove(item);
        if (shoppingCart.size() == 0){
            ProntoShopApplication.getInstance()
                    .getBus().post(new CustomerSelectedEvent(new Customer(), true));
        }
        populateToolbar();
    }


    public void completeCheckout(){
        shoppingCart.clear();
        populateToolbar();
        ProntoShopApplication.getInstance()
                .getBus().post(new CustomerSelectedEvent(new Customer(), true));
    }

    private void populateToolbar() {
        ProntoShopApplication.getInstance().getBus()
                .post(new UpdateToolbarEvent(shoppingCart));

    }

    public List<LineItem> getShoppingCart() {
        return shoppingCart;
    }

    public Customer getSelectedCustomer(){
        return selectedCustomer;
    }

    public void setCustomer(Customer customer){
        selectedCustomer = customer;
        ProntoShopApplication.getInstance()
                .getBus().post(new CustomerSelectedEvent(customer, false));

    }

    public void saveCartToPreference(){
        if (shoppingCart != null) {
            Gson gson = new Gson();
            String serializedItems = gson.toJson(shoppingCart);
            if (DEBUG){
                Log.d(LOG_TAG, "Saving Serialized Cart Items: " + serializedItems);
            }
            String serializedCustomer = gson.toJson(selectedCustomer);
            if (DEBUG){
                Log.d(LOG_TAG, "Saving Serialized Customers: " + serializedCustomer);
            }

            editor.putString(Constants.SERIALIZED_CART_ITEMS, serializedItems).commit();
            editor.putString(Constants.SERIALIZED_CUSTOMER, serializedCustomer).commit();
            editor.putBoolean(Constants.OPEN_CART_EXITS, true).commit();
        }
    }

    public void updateItemQty(LineItem item, int qty) {
        boolean itemAlreadyInCart = false;
        int position = 0;

        for (LineItem tempItem : shoppingCart) {
            if (tempItem.getId() == item.getId()) {
                position = shoppingCart.indexOf(tempItem);
                LineItem itemInCart = shoppingCart.get(position);
                itemInCart.setQuantity(qty);
                shoppingCart.set(position, itemInCart);
                itemAlreadyInCart = true;
                break;
            }
        }

        if (!itemAlreadyInCart) {
            item.setQuantity(qty);
            shoppingCart.add(item);
        }
        populateToolbar();
    }


}

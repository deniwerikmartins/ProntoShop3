package com.okason.prontoshop.common;

import android.content.SharedPreferences;
import android.util.Log;

import com.okason.prontoshop.core.ProntoShopApplication;
import com.okason.prontoshop.core.events.CustomerSelectedEvent;
import com.okason.prontoshop.core.events.UpdateToolbarEvent;
import com.okason.prontoshop.models.Customer;
import com.okason.prontoshop.models.LineItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Valentine on 4/18/2016.
 */
public class ShoppingCart {

    private Customer selectedCustomer;
   // private List<LineItem> shoppingCart;

    private Map<LineItem, Integer> shoppingCartMap = new HashMap<>();
    private BigDecimal totalPrice = BigDecimal.ZERO;
    private int totalCartQuantity = 0;
    private final double taxRate;

    private final static String LOG_TAG = ShoppingCart.class.getSimpleName();
    private static boolean DEBUG = true;

    public ShoppingCart(SharedPreferences sharedPreferences) {

        this.taxRate = 10;
    }



    public void addItemToCart(LineItem item) {
        if (shoppingCartMap.containsKey(item)){
            shoppingCartMap.put(item, shoppingCartMap.get(item) + item.getQuantity());
        } else {
            shoppingCartMap.put(item, item.getQuantity());
        }

        totalPrice = totalPrice.add(item.getSumPrice());
        totalCartQuantity += item.getQuantity();
        populateToolbar();
    }

    public void clearShoppingCart(){
        if (DEBUG) {
            Log.d(LOG_TAG, "Clear Cart Called");
        }
        shoppingCartMap.clear();
        totalPrice = BigDecimal.ZERO;
        totalCartQuantity = 0;
        selectedCustomer = null;
        populateToolbar();
        ProntoShopApplication.getInstance()
                .getBus().post(new CustomerSelectedEvent(new Customer(), true));
    }

    public void removeItemFromCart(LineItem item){
        int quantity = shoppingCartMap.get(item);
        shoppingCartMap.remove(item);
        totalPrice = totalPrice.subtract(BigDecimal.valueOf(item.getSalePrice()).multiply(BigDecimal.valueOf(quantity)));
        totalCartQuantity -= quantity;

        if (shoppingCartMap.size() == 0){
            ProntoShopApplication.getInstance()
                    .getBus().post(new CustomerSelectedEvent(new Customer(), true));
        }
        populateToolbar();
    }


    public void completeCheckout(){
        shoppingCartMap.clear();
        populateToolbar();
        ProntoShopApplication.getInstance()
                .getBus().post(new CustomerSelectedEvent(new Customer(), true));
    }

    private void populateToolbar() {
        ProntoShopApplication.getInstance().getBus()
                .post(new UpdateToolbarEvent(totalPrice, totalCartQuantity));

    }

    public List<LineItem> getShoppingCart() {
        List<LineItem> items = new ArrayList<>();
        for (Map.Entry<LineItem, Integer> entry: shoppingCartMap.entrySet()){
            LineItem item = entry.getKey();
            int qty = entry.getValue();
            item.setQuantity(qty);
            items.add(item);
        }
        return items;
    }

    public Customer getSelectedCustomer(){
        return selectedCustomer;
    }

    public void setCustomer(Customer customer){
        selectedCustomer = customer;
        ProntoShopApplication.getInstance()
                .getBus().post(new CustomerSelectedEvent(customer, false));

    }



    public void updateItemQty(LineItem item, int newQty) {
        int currentQuantity = shoppingCartMap.get(item);
        BigDecimal sumPrice = BigDecimal.valueOf(item.getSalePrice()).multiply(BigDecimal.valueOf(currentQuantity));

        shoppingCartMap.put(item, currentQuantity);
        totalCartQuantity = totalCartQuantity - currentQuantity + newQty;
        totalPrice = totalPrice.subtract(sumPrice).add(BigDecimal.valueOf(item.getSalePrice()).multiply(BigDecimal.valueOf(currentQuantity)));
        populateToolbar();
    }

    public double getTaxAmount(){
        double taxAmount = totalPrice.doubleValue() * (taxRate /100);
        return taxAmount;
    }

    public double getSubTotalAmount(){
        return totalPrice.doubleValue();
    }

    public double getTotalAmount(){
        return getSubTotalAmount() + getTaxAmount();
    }






}

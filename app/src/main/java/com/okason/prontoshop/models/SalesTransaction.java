package com.okason.prontoshop.models;

import android.database.Cursor;

import com.okason.prontoshop.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valentine on 4/6/2016.
 */
public class SalesTransaction {
    private long id;
    private long customerId;
    private double subTotalAmount;
    private double taxAmount;
    private double totalAmount;
    private boolean paid;
    private String paymentType;
    private long transactionDate;
    private long modifiedDate;

    public SalesTransaction(){
        id = 0;
        customerId = 0;
        subTotalAmount = 0;
        taxAmount = 0;
        paid = false;
        transactionDate = System.currentTimeMillis();
        modifiedDate = System.currentTimeMillis();
        lineItems = new ArrayList<>();
    }

    //this property cannot be persisted
    private List<LineItem> lineItems;

    //the list of line items will be collapsed into this json
    //string before it can be saved to the database
   // private String jsonLineItems;

    public SalesTransaction (long _id, long custId, double subTotal, double tax, double total,
                             boolean completed, String payment, long dateOfTransaction, long dateModified){
        id = _id;
        customerId = custId;
        subTotalAmount = subTotal;
        taxAmount = tax;
        totalAmount = total;
        paid = completed;
        paymentType = payment;
        transactionDate = dateOfTransaction;
        modifiedDate = dateModified;
    };




    public static SalesTransaction getSalesTransactionFromCursor(Cursor cursor){
        long id = cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_ID));
        long customerId = cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_CUSTOMER_ID));
        double subTotal = cursor.getDouble(cursor.getColumnIndex(Constants.COLUMN_SUB_TOTAL_AMOUNT));
        double tax = cursor.getDouble(cursor.getColumnIndex(Constants.COLUMN_TOTAL_AMOUNT));
        double total = cursor.getDouble(cursor.getColumnIndex(Constants.COLUMN_TOTAL_AMOUNT));
        boolean completed = cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_PAYMENT_STATUS)) > 0;
        String payment = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_PAYMENT_TYPE));
        long dateOfTransaction = cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_DATE_CREATED));
        long dateModified = cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_LAST_UPDATED));

         SalesTransaction transaction = new SalesTransaction(id, customerId,
                 subTotal, tax, total, completed, payment, dateOfTransaction,dateModified){

         };
        return transaction;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(long transactionDate) {
        this.transactionDate = transactionDate;
    }

    public double getSubTotalAmount() {
        return subTotalAmount;
    }

    public void setSubTotalAmount(double subTotalAmount) {
        this.subTotalAmount = subTotalAmount;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }


    public long getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }


    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}

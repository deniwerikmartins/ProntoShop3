package com.okason.prontoshop.models;

import java.math.BigDecimal;

/**
 * Created by Valentine on 4/6/2016.
 */
public class LineItem extends Product {
    private long id;
    private int quantity;
    private long transactionId;
    private long productId;



    public LineItem() {
    }

    public LineItem(Product product, int quantity) {
        super(product);
        this.quantity = quantity;
        this.productId = product.getId();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getSumPrice() {
        return BigDecimal.valueOf(getSalePrice() * quantity);
    }
}

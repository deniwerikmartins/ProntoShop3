package com.okason.prontoshop.models;

import java.math.BigDecimal;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Valentine on 4/6/2016.
 */
public class LineItem extends RealmObject {
    @PrimaryKey
    private long id;
    private int quantity;
    private long transactionId;
    private long productId;
    private Product product;



    public LineItem() {
    }

    public LineItem(Product product, int quantity) {
        this.quantity = quantity;
        this.productId = product.getId();
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public long getId() {
        return id;
    }


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
        return BigDecimal.valueOf(product.getSalePrice() * quantity);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

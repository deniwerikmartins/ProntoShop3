package com.okason.prontoshop.core.events;

import java.math.BigDecimal;

/**
 * Created by Valentine on 4/13/2016.
 */
public class UpdateToolbarEvent {
    private final BigDecimal totalPrice;
    private final int totalQty;


    public UpdateToolbarEvent(BigDecimal totalPrice, int totalQty) {
        this.totalPrice = totalPrice;
        this.totalQty = totalQty;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public int getTotalQty() {
        return totalQty;
    }
}

package com.okason.prontoshop.core.events;

import com.okason.prontoshop.models.LineItem;

/**
 * Created by Valentine on 4/9/2016.
 */
public class ItemAddedToCartEvent {
    private final LineItem item;

    public ItemAddedToCartEvent(LineItem item) {
        this.item = item;
    }

    public LineItem getItem() {
        return item;
    }
}

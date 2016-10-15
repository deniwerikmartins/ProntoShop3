package com.okason.prontoshop.core.events;

import com.okason.prontoshop.models.LineItem;

/**
 * Created by Valentine on 4/9/2016.
 */
public class ItemQtyChangeEvent {
    private final LineItem item;


    public ItemQtyChangeEvent(LineItem item) {
        this.item = item;

    }

    public LineItem getItem() {
        return item;
    }


}

package com.okason.prontoshop.core.events;

import com.okason.prontoshop.models.LineItem;

import java.util.List;

/**
 * Created by Valentine on 4/13/2016.
 */
public class UpdateToolbarEvent {
    private final List<LineItem> mLineItems;


    public UpdateToolbarEvent(List<LineItem> lineItems) {
        mLineItems = lineItems;
    }

    public List<LineItem> getLineItems() {
        return mLineItems;
    }
}

package com.okason.prontoshop.core.events;

import com.okason.prontoshop.models.LineItem;

import java.util.List;

/**
 * Created by Valentine on 4/12/2016.
 */
public class UpdateTotalsEvent {
    private final List<LineItem> mLineItems;

    public UpdateTotalsEvent(List<LineItem> lineItems) {
        mLineItems = lineItems;
    }

    public List<LineItem> getLineItems() {
        return mLineItems;
    }
}

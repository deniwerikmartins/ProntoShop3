package com.okason.prontoshop.core.listeners;

import com.okason.prontoshop.models.LineItem;

/**
 * Created by Valentine on 4/13/2016.
 */
public interface CartActionsListener {
   void onItemDeleted(LineItem item);
   void onItemQtyChange(LineItem item, int qty);

}

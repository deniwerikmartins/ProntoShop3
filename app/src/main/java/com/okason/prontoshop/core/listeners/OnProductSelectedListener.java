package com.okason.prontoshop.core.listeners;

import com.okason.prontoshop.models.Product;

/**
 * Created by Valentine on 4/9/2016.
 */
public interface OnProductSelectedListener {
    void onSelectProduct(Product selectedProduct);
    void onLongClickProduct(Product clickedProduct);
}

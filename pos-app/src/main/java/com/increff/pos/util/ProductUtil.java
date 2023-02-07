package com.increff.pos.util;

import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.ProductPojo;

public class ProductUtil {

    public static void normalize(ProductForm productForm) {
        productForm.setName(productForm.getName().toLowerCase().trim());
        productForm.setBarcode(productForm.getBarcode().toLowerCase().trim());
    }
}

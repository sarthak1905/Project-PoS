package com.increff.pos.util;

import com.increff.pos.pojo.ProductPojo;

public class ProductUtil {

    public static void normalize(ProductPojo p) {
        p.setName(p.getName().toLowerCase().trim());
        p.setBarcode(p.getBarcode().toLowerCase().trim());
    }
}

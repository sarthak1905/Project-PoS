package com.increff.employee.util;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;

public class ProductUtil {

    public static void normalize(ProductPojo p) {
        p.setName(p.getName().toLowerCase().trim());
        p.setBarcode(p.getBarcode().toLowerCase().trim());
    }
}

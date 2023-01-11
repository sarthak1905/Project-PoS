package com.increff.employee.util;

import com.increff.employee.pojo.BrandPojo;

import java.util.Locale;

public class BrandUtil {

    public static void normalize(BrandPojo b) {
        b.setBrand(b.getBrand().toLowerCase().trim());
        b.setCategory(b.getCategory().toLowerCase().trim());
    }
}

package com.increff.pos.util;

import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;

public class BrandUtil {

    public static void normalize(BrandPojo brandPojo) {
        brandPojo.setBrand(brandPojo.getBrand().toLowerCase().trim());
        brandPojo.setCategory(brandPojo.getCategory().toLowerCase().trim());
    }
}

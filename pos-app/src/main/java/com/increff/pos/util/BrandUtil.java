package com.increff.pos.util;

import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;

public class BrandUtil {

    public static void normalize(BrandForm brandForm) {
        brandForm.setBrand(brandForm.getBrand().toLowerCase().trim());
        brandForm.setCategory(brandForm.getCategory().toLowerCase().trim());
    }
}

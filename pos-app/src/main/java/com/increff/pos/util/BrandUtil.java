package com.increff.pos.util;

import com.increff.pos.model.BrandForm;

public class BrandUtil {

    public static void normalize(BrandForm brandForm) {
        brandForm.setBrand(brandForm.getBrand().toLowerCase().trim());
        brandForm.setCategory(brandForm.getCategory().toLowerCase().trim());
    }
}

package com.increff.employee.util;

import com.increff.employee.model.BrandForm;

public class BrandUtil {

    public static void normalize(BrandForm brandForm) {
        brandForm.setBrand(brandForm.getBrand().toLowerCase().trim());
        brandForm.setCategory(brandForm.getCategory().toLowerCase().trim());
    }
}

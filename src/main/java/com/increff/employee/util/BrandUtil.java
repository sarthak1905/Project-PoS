package com.increff.employee.util;

import com.increff.employee.model.BrandForm;
import com.increff.employee.pojo.BrandPojo;

import java.util.Locale;

public class BrandUtil {

    public static void normalize(BrandForm brandForm) {
        brandForm.setBrand(brandForm.getBrand().toLowerCase().trim());
        brandForm.setCategory(brandForm.getCategory().toLowerCase().trim());
    }
}

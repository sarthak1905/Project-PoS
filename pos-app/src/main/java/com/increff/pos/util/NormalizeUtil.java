package com.increff.pos.util;

import com.increff.pos.model.BrandForm;
import com.increff.pos.model.ProductForm;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;

public class NormalizeUtil {

    public static void normalize(BrandForm brandForm) {
        brandForm.setBrand(brandForm.getBrand().toLowerCase().trim());
        brandForm.setCategory(brandForm.getCategory().toLowerCase().trim());
    }

    public static void normalize(ProductForm productForm) {
        productForm.setName(productForm.getName().toLowerCase().trim());
        productForm.setBarcode(productForm.getBarcode().toLowerCase().trim());
    }

    public static void normalize(UserForm userForm) {
        userForm.setEmail(userForm.getEmail().toLowerCase().trim());
    }

}

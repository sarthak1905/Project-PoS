package com.increff.pos.util;

import com.increff.pos.model.*;
import com.increff.pos.pojo.*;

import java.time.format.DateTimeFormatter;

// TODO Suggestion - Think of if there are ways to create a generic convert util function which assigns values
//  if the member variable names match.
public class ConvertUtil {

    public static UserData convertUserPojoToUserData(UserPojo userPojo) {
        UserData userData = new UserData();
        userData.setEmail(userPojo.getEmail());
        userData.setRole(userPojo.getRole());
        userData.setId(userPojo.getId());
        return userData;
    }

    public static UserPojo convertUserFormToUserPojo(UserForm userForm) {
        UserPojo userPojo = new UserPojo();
        userPojo.setEmail(userForm.getEmail());
        userPojo.setRole(userForm.getRole());
        userPojo.setPassword(userForm.getPassword());
        return userPojo;
    }

    public static BrandData convertBrandPojoToData(BrandPojo brandPojo){
        BrandData brandData = new BrandData();
        brandData.setBrand(brandPojo.getBrand());
        brandData.setCategory(brandPojo.getCategory());
        brandData.setId(brandPojo.getId());
        return brandData;
    }

    public static BrandPojo convertBrandFormToPojo(BrandForm brandForm){
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(brandForm.getBrand());
        brandPojo.setCategory(brandForm.getCategory());
        return brandPojo;
    }

    public static InventoryData convertInventoryPojoToData(InventoryPojo inventoryPojo, String barcode) {
        InventoryData inventoryData = new InventoryData();
        inventoryData.setQuantity(inventoryPojo.getQuantity());
        inventoryData.setBarcode(barcode);
        inventoryData.setId(inventoryPojo.getId());
        return inventoryData;
    }

    public static InventoryPojo convertInventoryFormToPojo(InventoryForm inventoryForm, int productId) {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setId(productId);
        inventoryPojo.setQuantity(inventoryForm.getQuantity());
        return inventoryPojo;
    }

    public static ProductData convertProductPojoToData(ProductPojo productPojo, String brand, String category) {
        ProductData productData = new ProductData();
        productData.setId(productPojo.getId());
        productData.setName(productPojo.getName());
        productData.setBrand(brand);
        productData.setCategory(category);
        productData.setBarcode(productPojo.getBarcode());
        productData.setMrp(productPojo.getMrp());
        return productData;
    }

    public static ProductPojo convertProductFormToPojo(ProductForm productForm, int brandId) {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBrandCategory(brandId);
        productPojo.setBarcode(productForm.getBarcode());
        productPojo.setName(productForm.getName());
        productPojo.setMrp(productForm.getMrp());
        return productPojo;
    }

    public static OrderData convertOrderPojoToData(OrderPojo orderPojo) {
        OrderData orderData = new OrderData();
        orderData.setId(orderPojo.getId());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        orderData.setDateTime(orderPojo.getDateTime().format(dateTimeFormatter));
        orderData.setInvoiced(orderPojo.isInvoiced());
        orderData.setOrderTotal(orderPojo.getOrderTotal());
        return orderData;
    }

    public static OrderItemData convertOrderItemPojoToData(OrderItemPojo orderItemPojo, String barcode, String productName) {
        OrderItemData orderItemData = new OrderItemData();
        orderItemData.setQuantity(orderItemPojo.getQuantity());
        orderItemData.setId(orderItemPojo.getId());
        orderItemData.setProductId(orderItemPojo.getProductId());
        orderItemData.setOrderId(orderItemPojo.getOrderId());
        orderItemData.setSellingPrice(orderItemPojo.getSellingPrice());
        orderItemData.setBarcode(barcode);
        orderItemData.setProductName(productName);
        return orderItemData;
    }

    public static OrderItemPojo convertOrderItemFormToPojo(OrderItemForm orderItemForm, int productId){
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setQuantity(orderItemForm.getQuantity());
        orderItemPojo.setSellingPrice(orderItemForm.getSellingPrice());
        orderItemPojo.setProductId(productId);
        return orderItemPojo;
    }

}

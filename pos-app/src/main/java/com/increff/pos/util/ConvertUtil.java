package com.increff.pos.util;

import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import com.increff.pos.pojo.DaySalesPojo;

import java.time.format.DateTimeFormatter;

public class ConvertUtil {

    public static UserData convertUserPojoToUserData(UserPojo userPojo, String role) {
        UserData userData = new UserData();
        userData.setEmail(userPojo.getEmail());
        userData.setRole(role);
        userData.setId(userPojo.getId());
        return userData;
    }

    public static UserPojo convertUserFormToUserPojo(UserForm userForm) {
        UserPojo userPojo = new UserPojo();
        userPojo.setEmail(userForm.getEmail());
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

    public static InventoryData convertInventoryPojoToData(InventoryPojo inventoryPojo, String barcode, String name) {
        InventoryData inventoryData = new InventoryData();
        inventoryData.setQuantity(inventoryPojo.getQuantity());
        inventoryData.setBarcode(barcode);
        inventoryData.setName(name);
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
        orderData.setDateTime(orderPojo.getOrderDate().format(dateTimeFormatter));
        orderData.setInvoiced(orderPojo.getIsInvoiced());
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

    public static com.increff.pos.model.DaySalesData convertDaySalesPojoToData(DaySalesPojo daySalesPojo) {
        com.increff.pos.model.DaySalesData daySalesData = new com.increff.pos.model.DaySalesData();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        daySalesData.setDate(daySalesPojo.getDate().format(dateTimeFormatter));
        daySalesData.setTotalRevenue(daySalesPojo.getTotalRevenue());
        daySalesData.setInvoicedItemsCount(daySalesPojo.getInvoicedItemsCount());
        daySalesData.setInvoicedOrdersCount(daySalesPojo.getInvoicedOrdersCount());
        return daySalesData;
    }

}

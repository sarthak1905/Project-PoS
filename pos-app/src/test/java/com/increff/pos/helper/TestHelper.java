package com.increff.pos.helper;

import com.increff.pos.model.LoginForm;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.model.ProductForm;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.*;
import org.h2.engine.User;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public class TestHelper {
    public static ProductForm createNewProductForm(String productName, String productBarcode, String productBrand, String productCategory, Double productMrp) {
        ProductForm productForm = new ProductForm();
        productForm.setName(productName);
        productForm.setBrand(productBrand);
        productForm.setCategory(productCategory);
        productForm.setMrp(productMrp);
        productForm.setBarcode(productBarcode);
        return productForm;
    }

    public static BrandPojo createBrand(String brand, String category) {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(brand);
        brandPojo.setCategory(category);
        return brandPojo;
    }

    public static ProductPojo createProduct(String name, String barcode, Integer brandCategory, Double mrp) {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setName(name);
        productPojo.setMrp(mrp);
        productPojo.setBarcode(barcode);
        productPojo.setBrandCategory(brandCategory);
        return productPojo;
    }

    public static InventoryPojo createInventoryPojo(Integer productId, Integer quantity){
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setId(productId);
        inventoryPojo.setQuantity(quantity);
        return inventoryPojo;
    }

    public static OrderItemPojo createOrderItemPojo(Integer orderId, Integer productId, Integer quantity, Double sellingPrice){
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setOrderId(orderId);
        orderItemPojo.setProductId(productId);
        orderItemPojo.setQuantity(quantity);
        orderItemPojo.setSellingPrice(sellingPrice);
        return orderItemPojo;
    }

    public static OrderItemForm createOrderItemForm(String barcode, Integer quantity, Double sellingPrice) {
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setBarcode(barcode);
        orderItemForm.setQuantity(quantity);
        orderItemForm.setSellingPrice(sellingPrice);
        return orderItemForm;
    }

    public static OrderPojo createOrderPojo(Integer orderId, ZonedDateTime orderTime, Double orderTotal, boolean invoiced) {
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setId(orderId);
        orderPojo.setOrderDate(orderTime);
        orderPojo.setOrderTotal(orderTotal);
        orderPojo.setIsInvoiced(invoiced);
        return orderPojo;
    }

    public static OrderItemPojo convertOrderItemFormToPojo(OrderItemForm orderItemForm, int productId){
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setQuantity(orderItemForm.getQuantity());
        orderItemPojo.setSellingPrice(orderItemForm.getSellingPrice());
        orderItemPojo.setProductId(productId);
        return orderItemPojo;
    }

    public static InvoicePojo createInvoicePojo(Integer orderId, ZonedDateTime orderTime, String tempPath) {
        InvoicePojo invoicePojo = new InvoicePojo();
        invoicePojo.setOrderId(orderId);
        invoicePojo.setInvoiceDate(orderTime);
        invoicePojo.setPath(tempPath);
        return invoicePojo;
    }

    public static UserPojo createUserPojo(String email, String password) {
        UserPojo userPojo = new UserPojo();
        userPojo.setEmail(email);
        userPojo.setPassword(password);
        return userPojo;
    }

    public static UserForm createUserForm(String email, String password) {
        UserForm userForm = new UserForm();
        userForm.setEmail(email);
        userForm.setPassword(password);
        return userForm;
    }

    public static LoginForm createLoginForm(String operatorEmail1, String password) {
        LoginForm loginForm = new LoginForm();
        loginForm.setEmail(operatorEmail1);
        loginForm.setPassword(password);
        return loginForm;
    }

    public static DaySalesPojo createDaySalesPojo(LocalDate date, Integer invoicedOrdersCount, Integer invoicedItemsCount, Double totalRevenue) {
        DaySalesPojo daySalesPojo = new DaySalesPojo();
        daySalesPojo.setDate(date);
        daySalesPojo.setInvoicedOrdersCount(invoicedOrdersCount);
        daySalesPojo.setInvoicedItemsCount(invoicedItemsCount);
        daySalesPojo.setTotalRevenue(totalRevenue);
        return daySalesPojo;
    }
}

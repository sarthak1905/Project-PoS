package com.increff.pos.helper;

import com.increff.pos.model.OrderItemForm;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.*;

import java.time.LocalDateTime;

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

    public static OrderPojo createOrderPojo(Integer orderId, LocalDateTime orderTime, Double orderTotal, boolean invoiced) {
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setId(orderId);
        orderPojo.setOrderDate(orderTime);
        orderPojo.setOrderTotal(orderTotal);
        orderPojo.setInvoiced(invoiced);
        return orderPojo;
    }
}

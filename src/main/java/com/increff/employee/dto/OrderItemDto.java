package com.increff.employee.dto;

import com.increff.employee.model.OrderItemData;
import com.increff.employee.model.OrderItemForm;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderItemDto {

    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;


    public void add(OrderItemForm orderItemForm) throws ApiException {
        OrderPojo orderPojo = new OrderPojo();
        OrderItemPojo b = convertFormToPojo(orderItemForm);
        List<OrderItemPojo> orderItemPojos = new ArrayList<>();
        orderItemPojos.add(b);
        orderService.add(orderPojo, orderItemPojos);
    }

    public OrderItemData get(int id) throws ApiException{
        OrderItemPojo b = orderItemService.get(id);
        return convertPojoToData(b);
    }

    public List<OrderItemData> getAll(){
        List<OrderItemPojo> orderItemList = orderItemService.getAll();
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        for(OrderItemPojo b: orderItemList){
            orderItemDataList.add(convertPojoToData(b));
        }
        return orderItemDataList;
    }

    public void update(int id, OrderItemForm orderItemForm) throws ApiException{
        OrderItemPojo orderItemPojo = convertUpdateFormToPojo(id, orderItemForm);
        orderItemService.update(id, orderItemPojo);
    }

    public void delete(int id) throws ApiException{
        orderItemService.delete(id);
    }

    public OrderItemData convertPojoToData(OrderItemPojo orderItemPojo) {
        OrderItemData d = new OrderItemData();
        d.setQuantity(orderItemPojo.getQuantity());
        d.setId(orderItemPojo.getId());
        d.setProductId(orderItemPojo.getProductId());
        d.setOrderId(orderItemPojo.getOrderId());
        d.setSellingPrice(orderItemPojo.getSellingPrice());
        d.setBarcode(productService.getBarcodeFromProductId(orderItemPojo.getProductId()));
        return d;
    }

    public OrderItemPojo convertFormToPojo(OrderItemForm orderItemForm) throws ApiException {
        OrderItemPojo b = new OrderItemPojo();
        b.setQuantity(orderItemForm.getQuantity());
        b.setSellingPrice(orderItemForm.getSellingPrice());
        b.setProductId(productService.getProductIdFromBarcode(orderItemForm.getBarcode()));
        return b;
    }

    public OrderItemPojo convertDataToPojo(OrderItemData orderItemData) throws ApiException {
        OrderItemPojo b = new OrderItemPojo();
        b.setQuantity(orderItemData.getQuantity());
        b.setSellingPrice(orderItemData.getSellingPrice());
        b.setOrderId(orderItemData.getOrderId());
        b.setProductId(productService.getProductIdFromBarcode(orderItemData.getBarcode()));
        return b;
    }

    private OrderItemPojo convertUpdateFormToPojo(int id, OrderItemForm orderItemForm) throws ApiException {
        OrderItemPojo orderItemPojo = orderItemService.get(id);
        int newProductId = productService.getProductIdFromBarcode(orderItemForm.getBarcode());
        inventoryService.checkInventory(newProductId, orderItemForm.getQuantity());
        orderItemPojo.setQuantity(orderItemForm.getQuantity());
        orderItemPojo.setSellingPrice(orderItemForm.getSellingPrice());
        orderItemPojo.setProductId(newProductId);
        return orderItemPojo;
    }
}

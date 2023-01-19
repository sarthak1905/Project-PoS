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
        OrderItemPojo orderItemPojo = convertFormToPojo(orderItemForm);
        List<OrderItemPojo> orderItemPojos = new ArrayList<>();
        orderItemPojos.add(orderItemPojo);
        orderService.add(orderPojo, orderItemPojos);
    }

    public OrderItemData get(int id) throws ApiException{
        OrderItemPojo orderItemPojo = orderItemService.get(id);
        return convertPojoToData(orderItemPojo);
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
        OrderItemData orderItemData = new OrderItemData();
        orderItemData.setQuantity(orderItemPojo.getQuantity());
        orderItemData.setId(orderItemPojo.getId());
        orderItemData.setProductId(orderItemPojo.getProductId());
        orderItemData.setOrderId(orderItemPojo.getOrderId());
        orderItemData.setSellingPrice(orderItemPojo.getSellingPrice());
        orderItemData.setBarcode(productService.getBarcodeFromProductId(orderItemPojo.getProductId()));
        return orderItemData;
    }

    public OrderItemPojo convertFormToPojo(OrderItemForm orderItemForm) throws ApiException {
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setQuantity(orderItemForm.getQuantity());
        orderItemPojo.setSellingPrice(orderItemForm.getSellingPrice());
        orderItemPojo.setProductId(productService.getProductIdFromBarcode(orderItemForm.getBarcode()));
        return orderItemPojo;
    }

    public OrderItemPojo convertDataToPojo(OrderItemData orderItemData) throws ApiException {
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setQuantity(orderItemData.getQuantity());
        orderItemPojo.setSellingPrice(orderItemData.getSellingPrice());
        orderItemPojo.setOrderId(orderItemData.getOrderId());
        orderItemPojo.setProductId(productService.getProductIdFromBarcode(orderItemData.getBarcode()));
        return orderItemPojo;
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

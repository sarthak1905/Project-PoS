package com.increff.employee.dto;

import com.increff.employee.model.OrderData;
import com.increff.employee.model.OrderItemData;
import com.increff.employee.model.OrderItemForm;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class OrderDto {

    @Autowired
    private OrderItemDto orderItemDto;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;

    public void add(List<OrderItemForm> orderItemForms) throws ApiException {
        validateInputForms(orderItemForms);
        OrderPojo orderPojo = new OrderPojo();
        List<OrderItemPojo> orderItemPojos = new ArrayList<>();
        for(OrderItemForm orderItemForm: orderItemForms){
            orderItemPojos.add(orderItemDto.convertFormToPojo(orderItemForm));
        }
        orderService.add(orderPojo, orderItemPojos);
    }

    public OrderData get(int id) throws ApiException{
        OrderPojo b = orderService.get(id);
        return convertPojoToData(b);
    }

    public List<OrderData> getAll(){
        List<OrderPojo> orderList = orderService.getAll();
        List<OrderData> orderDataList = new ArrayList<>();
        for(OrderPojo b: orderList){
            orderDataList.add(convertPojoToData(b));
        }
        return orderDataList;
    }

    public void update(int id, List<OrderItemData> orderItemData) throws ApiException{
        validateUpdateData(orderItemData);
        HashMap<Integer,OrderItemData> map= new HashMap<>();
        for(OrderItemData data: orderItemData){
            map.put(data.getId(),data);
        }
        List<OrderItemPojo> orderItems = orderItemService.getByOrderId(id);
        for(OrderItemPojo orderItem: orderItems){
            if(map.containsKey(orderItem.getId())){
                orderItemService.update(orderItem.getId(), orderItemDto.convertDataToPojo(map.get(orderItem.getId())));
            }
            else{
                orderItemService.delete(orderItem.getId());
            }
        }
    }

    public void delete(int id) throws ApiException{
        orderService.delete(id);
    }

    private OrderData convertPojoToData(OrderPojo orderPojo) {
        OrderData orderData = new OrderData();
        orderData.setId(orderPojo.getId());
        orderData.setDateTime(orderPojo.getDateTime());
        return orderData;
    }

    private OrderPojo convertFormToPojo(int orderId) {
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setId(orderId);
        return orderPojo;
    }

    private void validateInputForms(List<OrderItemForm> orderItemForms) throws ApiException {
        for(OrderItemForm orderItemForm: orderItemForms){
            if(orderItemForm.getQuantity() <= 0){
                throw new ApiException("Quantity cannot be below 1 for product with barcode:"
                        + orderItemForm.getBarcode());
            }
            int productId = productService.getProductIdFromBarcode(orderItemForm.getBarcode());
            inventoryService.checkInventory(productId, orderItemForm.getQuantity());
        }
    }

    private void validateUpdateData(List<OrderItemData> orderItemDatas) throws ApiException {
        for(OrderItemData orderItemData: orderItemDatas){
            if(orderItemData.getQuantity() <= 0){
                throw new ApiException("Quantity cannot be below 1 for product with barcode:"
                        + orderItemData.getBarcode());
            }
            int productId = productService.getProductIdFromBarcode(orderItemData.getBarcode());
            inventoryService.checkInventory(productId, orderItemData.getQuantity());
            orderService.validateSellingPrice(productId, orderItemData.getSellingPrice());
        }
    }

}

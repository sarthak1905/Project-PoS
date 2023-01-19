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
    private OrderItemService orderItemService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;

    public void add(List<OrderItemForm> orderItemForms) throws ApiException {
        validateOrderItemInputForms(orderItemForms);
        OrderPojo orderPojo = new OrderPojo();
        List<OrderItemPojo> orderItemPojos = new ArrayList<>();
        for(OrderItemForm orderItemForm: orderItemForms){
            orderItemPojos.add(convertOrderItemFormToPojo(orderItemForm));
        }
        orderService.add(orderPojo, orderItemPojos);
    }

    public OrderData get(Integer id) throws ApiException{
        OrderPojo b = orderService.get(id);
        return convertOrderPojoToData(b);
    }

    public List<OrderData> getAll(){
        List<OrderPojo> orderList = orderService.getAll();
        List<OrderData> orderDataList = new ArrayList<>();
        for(OrderPojo b: orderList){
            orderDataList.add(convertOrderPojoToData(b));
        }
        return orderDataList;
    }

    public void update(Integer orderId, List<OrderItemForm> orderItemForms) throws ApiException{
        validateOrderItemUpdateData(orderItemForms);
        List<OrderItemPojo> existingOrderItems = orderItemService.getByOrderId(orderId);
        HashMap<String,OrderItemPojo> existingOrderItemMap = new HashMap<>();
        for(OrderItemPojo orderItemPojo: existingOrderItems){
            existingOrderItemMap.put(productService.getBarcodeFromProductId(orderItemPojo.getProductId()), orderItemPojo);
        }
        List<OrderItemPojo> newOrderItemPojos = new ArrayList<>();
        for(OrderItemForm form: orderItemForms){
            OrderItemPojo orderItemPojo = getExistingPojoOrAddNew(form, orderId, existingOrderItemMap);
            newOrderItemPojos.add(orderItemPojo);
        }

    }

    public void delete(Integer id) throws ApiException{
        orderService.delete(id);
    }

    private OrderItemPojo getExistingPojoOrAddNew(OrderItemForm orderItemForm, Integer orderId,
                                                  HashMap<String, OrderItemPojo> existingOrderItems)
            throws ApiException {
        // TODO Finish function
        OrderItemPojo newOrderItemPojo = convertOrderItemFormToPojo(orderItemForm);
        newOrderItemPojo.setOrderId(orderId);
        if(existingOrderItems.containsKey(orderItemForm.getBarcode())){
            OrderItemPojo existingOrderItemPojo = existingOrderItems.get(orderItemForm.getBarcode());
            newOrderItemPojo.setId(existingOrderItemPojo.getId());
        }
        else{
            orderItemService.add(newOrderItemPojo);
        }
        return newOrderItemPojo;
    }

    private void validateOrderItemInputForms(List<OrderItemForm> orderItemForms) throws ApiException {
        for(OrderItemForm orderItemForm: orderItemForms){
            if(orderItemForm.getQuantity() <= 0){
                throw new ApiException("Quantity cannot be below 1 for product with barcode:"
                        + orderItemForm.getBarcode());
            }
            int productId = productService.getProductIdFromBarcode(orderItemForm.getBarcode());
            inventoryService.checkInventory(productId, orderItemForm.getQuantity());
        }
    }

    private void validateOrderItemUpdateData(List<OrderItemForm> orderItemForms) throws ApiException {
        for(OrderItemForm orderItemForm: orderItemForms){
            if(orderItemForm.getQuantity() <= 0){
                throw new ApiException("Quantity cannot be below 1 for product with barcode:"
                        + orderItemForm.getBarcode());
            }
            int productId = productService.getProductIdFromBarcode(orderItemForm.getBarcode());
            inventoryService.checkInventory(productId, orderItemForm.getQuantity());
            orderService.validateSellingPrice(productId, orderItemForm.getSellingPrice());
        }
    }

    private OrderData convertOrderPojoToData(OrderPojo orderPojo) {
        OrderData orderData = new OrderData();
        orderData.setId(orderPojo.getId());
        orderData.setDateTime(orderPojo.getDateTime());
        return orderData;
    }

    public OrderItemData convertOrderItemPojoToData(OrderItemPojo orderItemPojo) {
        OrderItemData orderItemData = new OrderItemData();
        orderItemData.setQuantity(orderItemPojo.getQuantity());
        orderItemData.setId(orderItemPojo.getId());
        orderItemData.setProductId(orderItemPojo.getProductId());
        orderItemData.setOrderId(orderItemPojo.getOrderId());
        orderItemData.setSellingPrice(orderItemPojo.getSellingPrice());
        orderItemData.setBarcode(productService.getBarcodeFromProductId(orderItemPojo.getProductId()));
        return orderItemData;
    }

    public OrderItemPojo convertOrderItemFormToPojo(OrderItemForm orderItemForm) throws ApiException {
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setQuantity(orderItemForm.getQuantity());
        orderItemPojo.setSellingPrice(orderItemForm.getSellingPrice());
        orderItemPojo.setProductId(productService.getProductIdFromBarcode(orderItemForm.getBarcode()));
        return orderItemPojo;
    }

    public OrderItemPojo convertOrderItemDataToPojo(OrderItemData orderItemData) throws ApiException {
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

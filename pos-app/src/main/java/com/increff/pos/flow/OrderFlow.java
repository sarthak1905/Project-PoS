package com.increff.pos.flow;

import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderFlow {

    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private OrderService orderService;

    public void add(OrderPojo orderPojo, List<OrderItemPojo> orderItemPojoList) throws ApiException {
        ValidationUtil.checkPojo(orderPojo);
        validateOrderItemPojoList(orderItemPojoList);
        Double orderTotal = calculateOrderTotal(orderItemPojoList);
        orderPojo.setOrderTotal(orderTotal);
        orderPojo.setOrderDate(java.time.ZonedDateTime.now());
        orderPojo.setOrderStatus("placed");
        orderService.add(orderPojo);
        for (OrderItemPojo orderItemPojo: orderItemPojoList){
            orderItemPojo.setOrderId(orderPojo.getId());
            inventoryService.reduceInventory(orderItemPojo.getProductId(), orderItemPojo.getQuantity());
            Double itemTotal = orderItemPojo.getSellingPrice() * orderItemPojo.getQuantity();
            orderItemService.add(orderItemPojo);
        }
    }

    public OrderPojo get(Integer orderId) throws ApiException {
        ValidationUtil.checkId(orderId);
        return orderService.get(orderId);
    }

    public List<OrderPojo> getAllInvoiced() {
        return orderService.getAllInvoiced();
    }

    public List<OrderPojo> getAllBetweenDates(ZonedDateTime startDate, ZonedDateTime endDate) {
        return orderService.getAllBetweenDates(startDate, endDate);
    }

    public void update(Integer orderId, List<OrderItemPojo> orderItemPojoList) throws ApiException {
        ValidationUtil.checkId(orderId);
        Double orderTotal = calculateOrderTotal(orderItemPojoList);
        List<OrderItemPojo> existingOrderItems = orderItemService.getByOrderId(orderId);
        HashMap<Integer,OrderItemPojo> existingOrderItemMapByID = new HashMap<>();
        HashMap<String,OrderItemPojo> existingOrderItemMapByBarcode = new HashMap<>();

        for(OrderItemPojo orderItemPojo: existingOrderItems){
            String barcode = productService.getBarcodeFromProductId(orderItemPojo.getProductId());
            existingOrderItemMapByID.put(orderItemPojo.getProductId(), orderItemPojo);
            existingOrderItemMapByBarcode.put(barcode, orderItemPojo);
        }

        List<OrderItemPojo> updatedOrderItemPojos = new ArrayList<>();
        for(OrderItemPojo orderItemPojo: orderItemPojoList){
            ValidationUtil.checkPojo(orderItemPojo);
            OrderItemPojo addedOrderItemPojo = getExistingPojoOrAddNew(orderItemPojo, orderId, existingOrderItemMapByBarcode);
            updatedOrderItemPojos.add(addedOrderItemPojo);
        }

        for(OrderItemPojo orderItemPojo: updatedOrderItemPojos ){
            if (existingOrderItemMapByID.containsKey(orderItemPojo.getProductId())){
                OrderItemPojo existingOrderItemPojo = orderItemService.get(orderItemPojo.getId());
                existingOrderItemPojo.setQuantity(orderItemPojo.getQuantity());
                existingOrderItemPojo.setSellingPrice(orderItemPojo.getSellingPrice());
            }
        }
        orderService.update(orderId, orderTotal);
    }

    public void setInvoicedTrue(Integer orderId) throws ApiException {
        ValidationUtil.checkId(orderId);
        OrderPojo orderPojo = orderService.get(orderId);
        orderPojo.setOrderStatus("invoiced");
    }

    public void validateOrderStatus(Integer orderId) throws ApiException {
        ValidationUtil.checkId(orderId);
        OrderPojo orderPojo = orderService.get(orderId);
        if(orderPojo.getOrderStatus().equals("invoiced")){
            throw new ApiException("Invoiced order cannot be updated!");
        }
        if(orderPojo.getOrderStatus().equals("cancelled")){
            throw new ApiException("Cancelled order cannot be updated!");
        }
    }

    public List<OrderItemPojo> getByOrderId(Integer orderId) throws ApiException {
        ValidationUtil.checkId(orderId);
        return orderItemService.getByOrderId(orderId);
    }

    public List<OrderPojo> getBeforeEndDate(ZonedDateTime endDate) {
        return orderService.getBeforeEndDate(endDate);
    }

    public List<OrderPojo> getAfterStartDate(ZonedDateTime startDate) {
        return orderService.getAfterStartDate(startDate);
    }

    public void cancel(Integer id) throws ApiException {
        OrderPojo orderPojo = orderService.get(id);
        if(orderPojo.getOrderStatus().equals("invoiced")){
            throw new ApiException("Invoiced order cannot be cancelled!");
        }
        orderPojo.setOrderStatus("cancelled");
        List<OrderItemPojo> orderItemPojoList = orderItemService.getByOrderId(id);
        for(OrderItemPojo orderItemPojo: orderItemPojoList){
            inventoryService.increaseInventory(orderItemPojo.getProductId(), orderItemPojo.getQuantity());
        }
    }

    private double calculateOrderTotal(List<OrderItemPojo> orderItemPojos) {
        Double orderTotal = 0.0;
        for(OrderItemPojo orderItemPojo: orderItemPojos){
            orderTotal += orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice();
        }
        return orderTotal;
    }
    private void validateOrderItemPojoList(List<OrderItemPojo> orderItemPojoList) throws ApiException {
        for(OrderItemPojo orderItemPojo: orderItemPojoList){
            Integer productId = orderItemPojo.getProductId();
            inventoryService.checkInventory(productId, orderItemPojo.getQuantity());
            validateSellingPrice(productId, orderItemPojo.getSellingPrice());
        }
    }

    private void validateSellingPrice(Integer productId, Double sellingPrice) throws ApiException {
        ProductPojo productPojo = productService.get(productId);
        if(productPojo.getMrp() < sellingPrice){
            throw new ApiException("Selling price of order item cannot be greater than MRP!");
        }
    }

    private OrderItemPojo getExistingPojoOrAddNew(OrderItemPojo orderItemPojo, Integer orderId,
                                                  HashMap<String, OrderItemPojo> existingOrderItems)
            throws ApiException {
        orderItemPojo.setOrderId(orderId);
        String barcode = productService.getBarcodeFromProductId(orderItemPojo.getProductId());
        if(existingOrderItems.containsKey(barcode)){
            OrderItemPojo existingOrderItemPojo = existingOrderItems.get(barcode);
            orderItemPojo.setId(existingOrderItemPojo.getId());
        }
        else{
            orderItemService.add(orderItemPojo);
        }
        return orderItemPojo;
    }


}

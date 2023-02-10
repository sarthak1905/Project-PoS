package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private InvoiceService invoiceService;

    public void add(OrderPojo orderPojo, List<OrderItemPojo> orderItemPojos) throws ApiException {
        orderPojo.setOrderDate(java.time.LocalDateTime.now());
        orderPojo.setInvoiced(false);
        orderDao.insert(orderPojo);
        double orderTotal = 0.0;
        for (OrderItemPojo orderItemPojo: orderItemPojos){
            orderItemPojo.setOrderId(orderPojo.getId());
            inventoryService.reduceInventory(orderItemPojo.getProductId(), orderItemPojo.getQuantity());
            double itemTotal = orderItemPojo.getSellingPrice() * orderItemPojo.getQuantity();
            orderItemService.add(orderItemPojo);
            orderTotal += itemTotal;
        }
        orderPojo.setOrderTotal(orderTotal);
    }


    public OrderPojo get(int id) throws ApiException{
        return getCheck(id);
    }

    public List<OrderPojo> getAll(){
        return orderDao.selectAll();
    }

    public void update(List<OrderItemPojo> newOrderItemPojos,
                       HashMap<Integer, OrderItemPojo> existingOrderItemMapByID,
                       int orderId, double orderTotal) throws ApiException {
        OrderPojo orderPojo = get(orderId);
        orderPojo.setOrderTotal(orderTotal);
        for(OrderItemPojo newOrderItemPojo: newOrderItemPojos ){
            if (existingOrderItemMapByID.containsKey(newOrderItemPojo.getProductId())){
                OrderItemPojo existingOrderItemPojo = orderItemService.get(newOrderItemPojo.getId());
                existingOrderItemPojo.setQuantity(newOrderItemPojo.getQuantity());
                existingOrderItemPojo.setSellingPrice(newOrderItemPojo.getSellingPrice());
            }
        }
    }

    public void delete(int id) throws ApiException{
        OrderPojo orderPojo = getCheck(id);
        orderDao.delete(id);
    }

    private OrderPojo getCheck(int id) throws ApiException {
        OrderPojo orderPojo = orderDao.select_id(id);
        if (orderPojo == null) {
            throw new ApiException("Order with given ID: " + id + " does not exist!");
        }
        return orderPojo;
    }

    public void validateSellingPrice(int productId, double sellingPrice) throws ApiException {
        ProductPojo productPojo = productService.getCheck(productId);
        if(productPojo.getMrp() < sellingPrice){
            throw new ApiException("Selling price of order item cannot be greater than MRP!");
        }
    }

    public void setInvoicedTrue(Integer id) throws ApiException {
        OrderPojo orderPojo = getCheck(id);
        orderPojo.setInvoiced(true);
    }

    public void validateOrderInvoiceStatus(Integer orderId) throws ApiException {
        OrderPojo orderPojo = get(orderId);
        if(orderPojo.isInvoiced()){
            throw new ApiException("Invoiced order cannot be edited!");
        }
    }

    public List<OrderPojo> getBeforeEndDate(LocalDateTime endDate) {
        return orderDao.selectBeforeEndDate(endDate);
    }

    public List<OrderPojo> getAfterStartDate(LocalDateTime startDate) {
        return orderDao.selectAfterStartDate(startDate);
    }

    public List<OrderPojo> getBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return orderDao.selectBetweenDates(startDate, endDate);
    }

}

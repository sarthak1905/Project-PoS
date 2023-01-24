package com.increff.employee.service;


import com.increff.employee.dao.OrderDao;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.util.OrderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    public void add(OrderPojo orderPojo, List<OrderItemPojo> orderItemPojos) throws ApiException {
        orderPojo.setDateTime(OrderUtil.getCurrentTime());
        orderDao.insert(orderPojo);
        for (OrderItemPojo orderItemPojo: orderItemPojos){
            orderItemPojo.setOrderId(orderPojo.getId());
            inventoryService.reduceInventory(orderItemPojo.getProductId(), orderItemPojo.getQuantity());
            orderItemService.add(orderItemPojo);
        }
    }


    public OrderPojo get(int id) throws ApiException{
        return getCheck(id);
    }

    public List<OrderPojo> getAll(){
        return orderDao.selectAll();
    }

    public void update(List<OrderItemPojo> newOrderItemPojos, HashMap<Integer, OrderItemPojo> existingOrderItemMapByID) throws ApiException {
        for(OrderItemPojo newOrderItemPojo: newOrderItemPojos ){
            if (existingOrderItemMapByID.containsKey(newOrderItemPojo.getProductId())){
                OrderItemPojo existingOrderItemPojo = orderItemService.get(newOrderItemPojo.getId());
                existingOrderItemPojo.setQuantity(newOrderItemPojo.getQuantity());
                existingOrderItemPojo.setSellingPrice(newOrderItemPojo.getSellingPrice());
            }
        }
    }

    public void delete(int id) throws ApiException{
        OrderPojo p = getCheck(id);
        orderDao.delete(id);
    }

    public OrderPojo getCheck(int id) throws ApiException {
        OrderPojo p = orderDao.select_id(id);
        if (p == null) {
            throw new ApiException("Order with given ID: " + id + " does not exist!");
        }
        return p;
    }

    public void validateSellingPrice(int productId, double sellingPrice) throws ApiException {
        ProductPojo productPojo = productService.getCheck(productId);
        if(productPojo.getMrp() < sellingPrice){
            throw new ApiException("Selling price of order item cannot be greater than MRP!");
        }
    }

}

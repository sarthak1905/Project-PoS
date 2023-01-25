package com.increff.employee.service;

import com.increff.employee.dao.OrderItemDao;
import com.increff.employee.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderItemService {

    @Autowired
    private OrderItemDao orderItemDao;

    public void add(OrderItemPojo orderItemPojo) throws ApiException {
        Integer productId = orderItemPojo.getProductId();
        Integer orderId = orderItemPojo.getOrderId();
        OrderItemPojo existingOrderItem = orderItemDao.selectProductIdOrderId(productId, orderId);
        if(existingOrderItem != null){
            throw new ApiException("Order item with id:" + orderItemPojo.getId() +
                    " already exists for order with id: " + orderItemPojo.getOrderId());
        }
        orderItemDao.insert(orderItemPojo);
    }

    public OrderItemPojo get(int id) throws ApiException{
        return getCheck(id);
    }

    public List<OrderItemPojo> getAll(){
        return orderItemDao.selectAll();
    }

    public void update(int id, OrderItemPojo p) throws ApiException {
        OrderItemPojo px = getCheck(id);
        px.setProductId(p.getProductId());
        px.setQuantity(p.getQuantity());
        px.setSellingPrice(p.getSellingPrice());
    }

    public void delete(int id) throws ApiException{
        OrderItemPojo p = getCheck(id);
        orderItemDao.delete(id);
    }

    public OrderItemPojo getCheck(int id) throws ApiException {
        OrderItemPojo p = orderItemDao.select_id(id);
        if (p == null) {
            throw new ApiException("OrderItem with given ID:" + id + " does not exist");
        }
        return p;
    }

    public List<OrderItemPojo> getByOrderId(int orderId) {
        return orderItemDao.selectByOrderId(orderId);
    }
}

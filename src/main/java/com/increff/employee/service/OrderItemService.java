package com.increff.employee.service;

import com.increff.employee.dao.OrderItemDao;
import com.increff.employee.model.OrderItemData;
import com.increff.employee.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemDao orderItemDao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(OrderItemPojo p) throws ApiException {
        OrderItemPojo px = orderItemDao.select_id(p.getId());
        if(px != null){
            throw new ApiException("OrderItem already exists! Please update instead");
        }
        orderItemDao.insert(p);
    }

    @Transactional(rollbackOn = ApiException.class)
    public OrderItemPojo get(int id) throws ApiException{
        return getCheck(id);
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<OrderItemPojo> getAll(){
        return orderItemDao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(int id, OrderItemPojo p) throws ApiException {
        OrderItemPojo px = getCheck(id);
        px.setProductId(p.getProductId());
        px.setQuantity(p.getQuantity());
        px.setSellingPrice(p.getSellingPrice());
    }

    @Transactional
    public void delete(int id) throws ApiException{
        OrderItemPojo p = getCheck(id);
        orderItemDao.delete(id);
    }

    @Transactional
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

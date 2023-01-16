package com.increff.employee.service;

import com.increff.employee.dao.OrderDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.util.OrderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(OrderPojo p) throws ApiException {
        OrderPojo px = orderDao.select_id(p.getId());
        if(px != null){
            throw new ApiException("Order already exists! Please update instead");
        }
        p.setDateTime(OrderUtil.getCurrentTime());
        orderDao.insert(p);
    }

    @Transactional(rollbackOn = ApiException.class)
    public OrderPojo get(int id) throws ApiException{
        return getCheck(id);
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<OrderPojo> getAll(){
        return orderDao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(int id, OrderPojo p) throws ApiException {
        OrderPojo px = getCheck(id);
        px.setDateTime(p.getDateTime());
    }

    @Transactional
    public void delete(int id) throws ApiException{
        OrderPojo p = getCheck(id);
        orderDao.delete(id);
    }

    @Transactional
    public OrderPojo getCheck(int id) throws ApiException{
        OrderPojo p = orderDao.select_id(id);
        if(p == null){
            throw new ApiException("Order with given ID does not exist");
        }
        return p;
    }

}

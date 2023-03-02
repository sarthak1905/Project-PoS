package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    public void add(OrderPojo orderPojo) throws ApiException {
        ValidationUtil.checkPojo(orderPojo);
        orderPojo.setOrderDate(java.time.ZonedDateTime.now());
        orderPojo.setIsInvoiced(false);
        orderDao.insert(orderPojo);
    }

    public OrderPojo get(Integer id) throws ApiException{
        ValidationUtil.checkId(id);
        return getCheck(id);
    }

    public List<OrderPojo> getAll(){
        return orderDao.selectAll();
    }

    public void update(Integer orderId, Double orderTotal) throws ApiException {
        ValidationUtil.checkId(orderId);
        OrderPojo orderPojo = get(orderId);
        orderPojo.setOrderTotal(orderTotal);
    }

    private OrderPojo getCheck(Integer id) throws ApiException {
        ValidationUtil.checkId(id);
        OrderPojo orderPojo = orderDao.selectId(id);
        if (orderPojo == null) {
            throw new ApiException("Order with given ID: " + id + " does not exist!");
        }
        return orderPojo;
    }

    public void setInvoicedTrue(Integer id) throws ApiException {
        ValidationUtil.checkId(id);
        OrderPojo orderPojo = getCheck(id);
        orderPojo.setIsInvoiced(true);
    }

    public void validateOrderInvoiceStatus(Integer orderId) throws ApiException {
        ValidationUtil.checkId(orderId);
        OrderPojo orderPojo = get(orderId);
        if(orderPojo.getIsInvoiced()){
            throw new ApiException("Invoiced order cannot be edited!");
        }
    }

    public List<OrderPojo> getBeforeEndDate(ZonedDateTime endDate) {
        return orderDao.selectBeforeEndDate(endDate);
    }

    public List<OrderPojo> getAfterStartDate(ZonedDateTime startDate) {
        return orderDao.selectAfterStartDate(startDate);
    }

    public List<OrderPojo> getBetweenDates(ZonedDateTime startDate, ZonedDateTime endDate) {
        return orderDao.selectBetweenDates(startDate, endDate);
    }

}

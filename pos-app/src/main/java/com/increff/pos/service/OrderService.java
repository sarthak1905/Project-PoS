package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    public void add(OrderPojo orderPojo) throws ApiException {
        ValidationUtil.checkPojo(orderPojo);
        orderDao.insert(orderPojo);
    }

    public OrderPojo get(Integer id) throws ApiException{
        ValidationUtil.checkId(id);
        return getCheck(id);
    }

    public List<OrderPojo> getAllInvoiced(){
        return orderDao.selectAllByStatus("invoiced");
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

    public List<OrderPojo> getBeforeEndDate(ZonedDateTime endDate) {
        return orderDao.selectBeforeEndDate(endDate);
    }

    public List<OrderPojo> getAfterStartDate(ZonedDateTime startDate) {
        return orderDao.selectAfterStartDate(startDate);
    }

    public List<OrderPojo> getBetweenDates(ZonedDateTime startDate, ZonedDateTime endDate) {
        return orderDao.selectBetweenDates(startDate, endDate);
    }

    public List<OrderPojo> getAllBetweenDates(ZonedDateTime startDate, ZonedDateTime endDate) {
        return orderDao.selectAllBetweenDates(startDate, endDate);
    }
}

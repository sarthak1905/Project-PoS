package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.helper.TestHelper;
import com.increff.pos.pojo.OrderItemPojo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class OrderItemServiceTest extends AbstractUnitTest {

    private static final int orderId = 999;
    private static final int productId = 999;
    private static final int quantity  = 1;
    private static final double sellingPrice = 99.99;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderItemDao orderItemDao;

    @Test
    public void testAdd() throws ApiException {
        OrderItemPojo orderItemPojo = TestHelper.createOrderItemPojo(orderId + 1, productId, quantity, sellingPrice);
        orderItemService.add(orderItemPojo);
        orderItemPojo = orderItemDao.selectProductIdOrderId(productId, orderId + 1);
        assertEquals(orderItemPojo.getOrderId(), (Integer)(orderId + 1));
        assertEquals(orderItemPojo.getProductId(), (Integer)productId);
        assertEquals(orderItemPojo.getQuantity(), (Integer) quantity);
        assertEquals(orderItemPojo.getSellingPrice(), sellingPrice);
    }

    @Test(expected = ApiException.class)
    public void testRepeatedAdd() throws ApiException {
        OrderItemPojo orderItemPojo = TestHelper.createOrderItemPojo(orderId, productId, quantity, sellingPrice);
        orderItemService.add(orderItemPojo);
    }

    @Test(expected = ApiException.class)
    public void testInvalidGet() throws ApiException {
        OrderItemPojo orderItemPojo = orderItemService.get(orderId + 1);
    }

    @Before
    public void initOrderItemService() throws ApiException {
        OrderItemPojo orderItemPojo = TestHelper.createOrderItemPojo(orderId, productId, quantity, sellingPrice);
        orderItemService.add(orderItemPojo);
    }
}

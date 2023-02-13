package com.increff.pos.dao;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class OrderItemDaoTest extends AbstractUnitTest {
    
    private static final Integer productId = 1;
    private static final Integer orderId = 1;
    private static final Double sellingPrice = 99.99;
    private static final Integer quantity = 100;

    @Autowired
    private OrderItemDao orderItemDao;

    @Test
    public void testInsert(){
        List<OrderItemPojo> orderItemPojoList = orderItemDao.selectByOrderId(orderId);
        OrderItemPojo orderItemPojo = null;
        try {
            orderItemPojo = orderItemPojoList.get(0);
        }
        catch (ArrayIndexOutOfBoundsException e){
            fail();
        }
        assertOrderItemPojo(orderItemPojo);
    }

    @Test
    public void testSelectId(){
        List<OrderItemPojo> orderItemPojoList = orderItemDao.selectByOrderId(orderId);
        if(orderItemPojoList.size() == 0){
            fail();
        }
        OrderItemPojo orderItemPojo = orderItemPojoList.get(0);
        if(orderItemPojo == null){
            fail();
        }
        int id = orderItemPojo.getId();
        OrderItemPojo retrievedOrderItemPojo = orderItemDao.selectId(id);
        assertOrderItemPojo(retrievedOrderItemPojo);
    }

    @Test
    public void testSelectByProductIdOrderId(){
        OrderItemPojo orderItemPojo = orderItemDao.selectProductIdOrderId(productId, orderId);
        if(orderItemPojo == null){
            fail();
        }
        assertOrderItemPojo(orderItemPojo);
    }

    @Before
    public void initOrderItem(){
        insertOrderItem(productId, quantity, orderId, sellingPrice);
    }

    private void insertOrderItem(Integer productId, Integer quantity, Integer orderId, Double sellingPrice){
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setProductId(productId);
        orderItemPojo.setOrderId(orderId);
        orderItemPojo.setQuantity(quantity);
        orderItemPojo.setSellingPrice(sellingPrice);
        orderItemDao.insert(orderItemPojo);
    }

    private void assertOrderItemPojo(OrderItemPojo orderItemPojo){
        assertEquals(quantity, orderItemPojo.getQuantity());
        assertEquals(productId, orderItemPojo.getProductId());
        assertEquals(orderId, orderItemPojo.getOrderId());
        assertEquals(sellingPrice, orderItemPojo.getSellingPrice());
    }

}

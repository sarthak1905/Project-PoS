package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.dao.InventoryDao;
import com.increff.pos.dao.OrderDao;
import com.increff.pos.dao.ProductDao;
import com.increff.pos.flow.OrderFlow;
import com.increff.pos.helper.TestHelper;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.*;

public class OrderFlowTest extends AbstractUnitTest {

    private static final Boolean invoiced = false;
    private static final ZonedDateTime orderTime = ZonedDateTime.now();
    private static final double orderTotal = 199.99;
    private static final int orderId = 999;
    private static final int quantity = 1;
    private static final double sellingPrice = 199.99;
    private static final int productQty = 999;
    private static final String productName = "productName";
    private static final String barcode = "barcode";
    private static final int brandCategory = 1;
    private static final double mrp = 200.00;
    @Autowired
    private OrderFlow orderFlow;
    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private OrderDao orderDao;

    @Test
    public void testAdd(){
        List<OrderPojo> orderPojoList = orderDao.selectAll();
        if(orderPojoList.size() != 1){
            fail();
        }
        OrderPojo orderPojo = orderPojoList.get(0);
        assertEquals(orderPojo.getOrderTotal(), orderTotal);
        assertEquals(orderPojo.getOrderStatus(), "placed");
    }

    @Test
    public void testGet() throws ApiException {
        List<OrderPojo> orderPojoList = orderDao.selectAll();
        if(orderPojoList.size() != 1){
            fail();
        }
        OrderPojo orderPojo = orderPojoList.get(0);
        orderPojo = orderFlow.get(orderPojo.getId());
        assertEquals(orderPojo.getOrderTotal(), orderTotal);
        assertEquals(orderPojo.getOrderStatus(), "placed");
    }

    @Test(expected = ApiException.class)
    public void testInvalidGet() throws ApiException {
        List<OrderPojo> orderPojoList = orderDao.selectAll();
        if(orderPojoList.size() != 1){
            fail();
        }
        OrderPojo orderPojo = orderPojoList.get(0);
        orderPojo = orderFlow.get(orderPojo.getId() + 1);
    }

    @Test
    public void testSetInvoicedTrue() throws ApiException {
        List<OrderPojo> orderPojoList = orderDao.selectAll();
        if(orderPojoList.size() != 1){
            fail();
        }
        OrderPojo orderPojo = orderPojoList.get(0);
        orderFlow.setInvoicedTrue(orderPojo.getId());
        orderPojoList = orderDao.selectAll();
        if(orderPojoList.size() != 1){
            fail();
        }
        orderPojo = orderPojoList.get(0);
        assertEquals(orderPojo.getOrderStatus(), "invoiced");
    }

    @Test(expected = ApiException.class)
    public void testValidateOrderInvoiceStatus() throws ApiException {
        List<OrderPojo> orderPojoList = orderDao.selectAll();
        if(orderPojoList.size() != 1){
            fail();
        }
        OrderPojo orderPojo = orderPojoList.get(0);
        orderFlow.setInvoicedTrue(orderPojo.getId());
        orderPojoList = orderDao.selectAll();
        if(orderPojoList.size() != 1){
            fail();
        }
        orderPojo = orderPojoList.get(0);
        orderFlow.validateOrderStatus(orderPojo.getId());
    }


/*
    @Test(expected = ApiException.class)
    public void testValidateSellingPrice() throws ApiException {
        ProductPojo productPojo = productDao.selectBarcode(barcode);
        if(productPojo == null){
            fail();
        }
        orderService.validateSellingPrice(productPojo.getId(), mrp + 1);
    }
*/

    @Before
    public void initOrderService() throws ApiException {
        List<OrderItemPojo> orderItemPojoList = createTemplateOrderItemPojoList();
        OrderPojo orderPojo = TestHelper.createOrderPojo(orderId, orderTime, orderTotal, invoiced);
        orderFlow.add(orderPojo, orderItemPojoList);
    }

    private List<OrderItemPojo> createTemplateOrderItemPojoList() {
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        ProductPojo productPojo = TestHelper.createProduct(productName, barcode, brandCategory, mrp);
        productDao.insert(productPojo);
        productPojo = productDao.selectBarcode(barcode);
        OrderItemPojo orderItemPojo = TestHelper.createOrderItemPojo(orderId, productPojo.getId(), quantity, sellingPrice);
        createInventory(productPojo.getId());
        orderItemPojoList.add(orderItemPojo);
        return orderItemPojoList;
    }

    private void createInventory(int productId) {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setId(productId);
        inventoryPojo.setQuantity(productQty);
        inventoryDao.insert(inventoryPojo);
    }

}

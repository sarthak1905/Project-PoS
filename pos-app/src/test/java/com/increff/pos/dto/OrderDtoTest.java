package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.dao.*;
import com.increff.pos.helper.TestHelper;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.*;
import com.increff.pos.service.ApiException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class OrderDtoTest extends AbstractUnitTest {

    private static final String productName = "name";
    private static final String productBarcode = "barcode";
    private static final String productBrand = "brand";
    private static final String productCategory = "category";
    private static final Double productMrp = 99.99;
    private static final Double sellingPrice = 98.99;
    private static final Integer quantity = 100;
    private static final Integer orderId = 999;
    private static final int orderItemsCount = 3;
    private static final LocalDateTime orderTime = LocalDateTime.now();
    private static final Double orderTotal = 199.99;
    private static final boolean invoiced = false;
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderDto orderDto;


    @Test
    public void testGet() throws ApiException {
        List<OrderData> orderDataList = orderDto.getAll();
        if(orderDataList.size() == 0){
            fail();
        }
        OrderData orderData = orderDataList.get(0);
        OrderData orderData1 = orderDto.get(orderData.getId());
        assertEquals(orderData.getOrderTotal(), orderData1.getOrderTotal());
        assertEquals(orderData.getDateTime(), orderData1.getDateTime());
        assertEquals(orderData.getId(), orderData1.getId());
    }

    @Test
    public void testGetAll(){
        OrderPojo orderPojo = TestHelper.createOrderPojo(orderId, orderTime, orderTotal, invoiced);
        orderDao.insert(orderPojo);
        List<OrderData> orderDataList = orderDto.getAll();
        if(orderDataList.size() != 2){
            fail();
        }
        OrderData orderData = orderDataList.get(1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        assertEquals(orderTime.format(dateTimeFormatter), orderData.getDateTime());
        assertEquals(orderTotal, orderData.getOrderTotal());
    }

    @Test
    public void testGetOrderItems() throws ApiException {
        int indexVal = 3;
        List<OrderItemForm> orderItemFormList = createOrderItemList(indexVal, false);
        orderDto.add(orderItemFormList);
        List<OrderData> orderDataList = orderDto.getAll();
        if(orderDataList.size() != 2){
            fail();
        }
        OrderData orderData = orderDataList.get(1);
        List<OrderItemData> orderItemDataList = orderDto.getOrderItems(orderData.getId());
        if(orderItemDataList.size() != orderItemFormList.size()){
            fail();
        }
        int endIndex = indexVal + orderItemsCount;
        int i = indexVal;
        while(i < endIndex){
            OrderItemForm orderItemForm = orderItemFormList.get(i - indexVal);
            OrderItemData orderItemData = orderItemDataList.get(i - indexVal);
            assertEquals(orderItemData.getBarcode(), orderItemForm.getBarcode());
            assertEquals(orderItemData.getQuantity(), orderItemForm.getQuantity());
            assertEquals(orderItemData.getSellingPrice(), orderItemForm.getSellingPrice());
            ++i;
        }
    }

    @Test
    public void testGetOrderInvoice() throws IOException, ApiException {
        List<OrderPojo> orderPojoList = orderDao.selectAll();
        if(orderPojoList.size()!= 1){
            fail();
        }
        OrderPojo orderPojo1 = orderPojoList.get(0);
        ResponseEntity<byte[]> responseEntity = orderDto.getOrderInvoice(orderPojo1.getId());
//        ResponseEntity<byte[]> expectedResponseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        assertEquals(expectedResponseEntity, responseEntity);
    }

    @Test
    public void testUpdate() throws ApiException {
        List<OrderPojo> orderPojoList = orderDao.selectAll();
        if(orderPojoList.size()!= 1){
            fail();
        }
        OrderPojo orderPojo = orderPojoList.get(0);
        int indexVal = 3;
        List<OrderItemForm> orderItemFormList = createOrderItemList(0, true);
        orderDto.update(orderPojo.getId(), orderItemFormList);
        List<OrderItemData> orderItemDataList = orderDto.getOrderItems(orderPojo.getId());
        if(orderItemDataList.size() != orderItemFormList.size()){
            fail();
        }
        int endIndex = indexVal + orderItemsCount;
        int i = indexVal;
        while(i < endIndex){
            OrderItemForm orderItemForm = orderItemFormList.get(i - indexVal);
            OrderItemData orderItemData = orderItemDataList.get(i - indexVal);
            assertEquals(orderItemData.getBarcode(), orderItemForm.getBarcode());
            assertEquals(orderItemData.getQuantity(), orderItemForm.getQuantity());
            assertEquals(orderItemData.getSellingPrice(), orderItemForm.getSellingPrice());
            ++i;
        }
    }

    @Before
    public void initOrder() throws ApiException {
        List<OrderItemForm> orderItemFormList = createOrderItemList(0, false);
        orderDto.add(orderItemFormList);
    }

    private List<OrderItemForm> createOrderItemList(int indexVal, boolean isUpdate) {
        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        for(int i = indexVal; i < indexVal + orderItemsCount; ++i){
            if(!isUpdate) {
                ProductPojo productPojo = createTemplateProductPojo(productName + i, productCategory + i,
                        productBrand + i, productBarcode + i, productMrp + i, quantity + i);
            }
            OrderItemForm orderItemForm = TestHelper.createOrderItemForm(productBarcode + i, 1, sellingPrice);
            orderItemFormList.add(orderItemForm);
        }
        return orderItemFormList;
    }

    private ProductPojo createTemplateProductPojo(String name, String category, String brand, String barcode, Double mrp, Integer quantity){
        ProductPojo productPojo = createNewProductPojo(name, barcode, brand, category, mrp);
        productDao.insert(productPojo);
        productPojo = productDao.selectBarcode(barcode);
        int productId = productPojo.getId();
        InventoryPojo inventoryPojo = createTemplateInventoryPojo(productId, quantity);
        return productPojo;
    }

    private InventoryPojo createTemplateInventoryPojo(int productId, Integer quantity) {
        InventoryPojo inventoryPojo = TestHelper.createInventoryPojo(productId, quantity);
        inventoryDao.insert(inventoryPojo);
        return inventoryPojo;
    }

    private ProductPojo createNewProductPojo(String name, String barcode, String brand, String category, Double mrp) {
        BrandPojo brandPojo = TestHelper.createBrand(brand, category);
        brandDao.insert(brandPojo);
        brandPojo = brandDao.selectBrandCategory(brand, category);
        int brandId = brandPojo.getId();
        return TestHelper.createProduct(name, barcode, brandId, mrp);
    }

}

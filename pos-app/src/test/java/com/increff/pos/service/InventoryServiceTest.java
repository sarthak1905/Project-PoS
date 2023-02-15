package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.helper.TestHelper;
import com.increff.pos.pojo.InventoryPojo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class InventoryServiceTest extends AbstractUnitTest {

    private static final Integer productId = 1;
    private static final Integer quantity = 100;
    @Autowired
    private InventoryService inventoryService;

    @Test
    public void testGet() throws ApiException {
        List<InventoryPojo> inventoryPojoList = inventoryService.getAll();
        if(inventoryPojoList.size() != 1){
            fail();
        }
        InventoryPojo inventoryPojo = inventoryPojoList.get(0);
        inventoryPojo = inventoryService.get(inventoryPojo.getId());
        assertEquals(productId, inventoryPojo.getId());
        assertEquals(quantity, inventoryPojo.getQuantity());
    }

    @Test(expected = ApiException.class)
    public void testInvalidGet() throws ApiException {
        List<InventoryPojo> inventoryPojoList = inventoryService.getAll();
        if(inventoryPojoList.size() != 1){
            fail();
        }
        InventoryPojo inventoryPojo = inventoryService.get(999);
    }

    @Test(expected = ApiException.class)
    public void testInvalidInventory() throws ApiException {
        List<InventoryPojo> inventoryPojoList = inventoryService.getAll();
        if(inventoryPojoList.size() != 1){
            fail();
        }
        InventoryPojo inventoryPojo = inventoryPojoList.get(0);
        inventoryService.checkInventory(inventoryPojo.getId(), inventoryPojo.getQuantity() + 1);
    }

    @Before
    public void initInventory() throws ApiException {
        InventoryPojo inventoryPojo = TestHelper.createInventoryPojo(productId, quantity);
        inventoryService.add(inventoryPojo);
    }

}

package com.increff.pos.dao;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.InventoryPojo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class InventoryDaoTest extends AbstractUnitTest {
    
    private static final Integer productId = 1;
    private static final Integer quantity = 100;

    @Autowired
    private InventoryDao inventoryDao;

    @Test
    public void testInsert(){
        List<InventoryPojo> inventoryPojoList = inventoryDao.selectAll();
        InventoryPojo inventoryPojo = null;
        try {
            inventoryPojo = inventoryPojoList.get(0);
        }
        catch (ArrayIndexOutOfBoundsException e){
            fail();
        }
        assertInventoryPojo(inventoryPojo);
    }

    @Test
    public void testSelectId(){
        List<InventoryPojo> inventoryPojoList = inventoryDao.selectAll();
        if(inventoryPojoList.size() == 0){
            fail();
        }
        InventoryPojo inventoryPojo = inventoryPojoList.get(0);
        if(inventoryPojo == null){
            fail();
        }
        int id = inventoryPojo.getId();
        InventoryPojo retrievedInventoryPojo = inventoryDao.selectId(id);
        assertInventoryPojo(retrievedInventoryPojo);
    }

    @Test
    public void testSelectAll(){
        Integer productId2 = 2;
        Integer quantity2 = 200;
        insertInventory(productId2, quantity2);
        List<InventoryPojo> inventoryPojoList = inventoryDao.selectAll();
        if(inventoryPojoList.size() != 2){
            fail();
        }
        InventoryPojo retrievedInventoryPojo1 = inventoryPojoList.get(0);
        assertInventoryPojo(retrievedInventoryPojo1);

        InventoryPojo retrievedInventoryPojo2 = inventoryPojoList.get(1);
        assertEquals(productId2, retrievedInventoryPojo2.getId());
        assertEquals(quantity2, retrievedInventoryPojo2.getQuantity());
    }

    @Before
    public void initInventory(){
        insertInventory(productId, quantity);
    }

    private void insertInventory(Integer productId, Integer quantity){
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setId(productId);
        inventoryPojo.setQuantity(quantity);
        inventoryDao.insert(inventoryPojo);
    }

    private void assertInventoryPojo(InventoryPojo inventoryPojo){
        assertEquals(quantity, inventoryPojo.getQuantity());
        assertEquals(productId, inventoryPojo.getId());
    }

}

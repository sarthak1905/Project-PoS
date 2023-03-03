package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class InventoryService {

    @Autowired
    private InventoryDao inventoryDao;

    public void add(InventoryPojo inventoryPojo) throws ApiException {
        ValidationUtil.checkPojo(inventoryPojo);
        inventoryDao.insert(inventoryPojo);
    }

    public InventoryPojo get(Integer id) throws ApiException{
        ValidationUtil.checkId(id);
        return getCheck(id);
    }

    public List<InventoryPojo> getAll(){
        return inventoryDao.selectAll();
    }

    public void update(Integer id, InventoryPojo inventoryPojo) throws ApiException {
        ValidationUtil.checkPojo(inventoryPojo);
        ValidationUtil.checkId(id);
        InventoryPojo existingInventoryPojo = getCheck(id);
        existingInventoryPojo.setQuantity(inventoryPojo.getQuantity());
    }

    public void reduceInventory(Integer productId, Integer quantity) throws ApiException {
        ValidationUtil.checkId(productId);
        if(quantity == null){
            throw new ApiException("Quantity cannot be null!");
        }
        InventoryPojo inventoryPojo = getCheck(productId);
        inventoryPojo.setQuantity(inventoryPojo.getQuantity() - quantity);
    }

    private InventoryPojo getCheck(Integer id) throws ApiException{
        ValidationUtil.checkId(id);
        InventoryPojo inventoryPojo = inventoryDao.selectId(id);
        if(inventoryPojo == null){
            throw new ApiException("Inventory with given ID does not exist!");
        }
        return inventoryPojo;
    }

    public void checkInventory(Integer id, Integer quantity) throws ApiException {
        InventoryPojo inventoryPojo = get(id);
        if(inventoryPojo.getQuantity() < quantity){
            throw new ApiException("Insufficient inventory for product with id:" + id +
                    ". Max available = " + inventoryPojo.getQuantity() + ". Order placed for = " + quantity);
        }
    }

    public void increaseInventory(Integer productId, Integer quantity) {
        InventoryPojo inventoryPojo = inventoryDao.selectId(productId);
        inventoryPojo.setQuantity(inventoryPojo.getQuantity() + quantity);
    }
}

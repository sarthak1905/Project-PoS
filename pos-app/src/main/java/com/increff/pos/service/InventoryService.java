package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
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
        inventoryDao.insert(inventoryPojo);
    }

    public InventoryPojo get(int id) throws ApiException{
        return getCheck(id);
    }

    public List<InventoryPojo> getAll(){
        return inventoryDao.selectAll();
    }

    public void update(int id, InventoryPojo inventoryPojo) throws ApiException {
        InventoryPojo px = getCheck(id);
        px.setQuantity(inventoryPojo.getQuantity());
    }

    public void delete(int id) throws ApiException{
        InventoryPojo p = getCheck(id);
        inventoryDao.delete(id);
    }

    public InventoryPojo getCheck(int id) throws ApiException{
        InventoryPojo inventoryPojo = inventoryDao.select_id(id);
        if(inventoryPojo == null){
            throw new ApiException("Product with given ID does not exist");
        }
        return inventoryPojo;
    }

    public void checkInventory(int id, int quantity) throws ApiException {
        InventoryPojo inventoryPojo = getCheck(id);
        if(inventoryPojo.getQuantity() < quantity){
            throw new ApiException("Insufficient inventory for product with id:" + id +
                    ". Max available = " + inventoryPojo.getQuantity() + ". Order placed for = " + quantity);
        }
    }

    public void reduceInventory(int productId, int quantity) throws ApiException {
        InventoryPojo inventoryPojo = getCheck(productId);
        inventoryPojo.setQuantity(inventoryPojo.getQuantity() - quantity);
    }
}

package com.increff.pos.service;

import com.increff.pos.dao.InventoryDao;
import com.increff.pos.pojo.InventoryPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private InventoryDao inventoryDao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(InventoryPojo p) throws ApiException {
        inventoryDao.insert(p);
    }

    @Transactional(rollbackOn = ApiException.class)
    public InventoryPojo get(int id) throws ApiException{
        return getCheck(id);
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<InventoryPojo> getAll(){
        return inventoryDao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(int id, InventoryPojo p) throws ApiException {
        InventoryPojo px = getCheck(id);
        px.setQuantity(p.getQuantity());
    }

    @Transactional
    public void delete(int id) throws ApiException{
        InventoryPojo p = getCheck(id);
        inventoryDao.delete(id);
    }

    @Transactional
    public InventoryPojo getCheck(int id) throws ApiException{
        InventoryPojo p = inventoryDao.select_id(id);
        if(p == null){
            throw new ApiException("Product with given ID does not exist");
        }
        return p;
    }

    public void checkInventory(int id, int quantity) throws ApiException {
        InventoryPojo inventoryPojo = getCheck(id);
        if(inventoryPojo.getQuantity() < quantity){
            throw new ApiException("Insufficient inventory for product with id:" + id +
                    ". Max available = " + inventoryPojo.getQuantity() + ". Order placed for = " + quantity);
        }
    }

    // Add @Transactional
    public void reduceInventory(int productId, int quantity) throws ApiException {
        InventoryPojo inventoryPojo = getCheck(productId);
        inventoryPojo.setQuantity(inventoryPojo.getQuantity() - quantity);
    }
}

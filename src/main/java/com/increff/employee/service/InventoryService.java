package com.increff.employee.service;

import com.increff.employee.dao.InventoryDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private ProductDao productDao;

    @Transactional(rollbackOn = ApiException.class)
    public void add(InventoryPojo p) throws ApiException {
        ProductPojo product = productDao.selectId(p.getId());
        if (product == null){
            throw new ApiException("Product does not exist!");
        }
        InventoryPojo px = inventoryDao.select_id(p.getId());
        if(px != null){
            throw new ApiException("Product inventory already exists! Please update instead");
        }
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

}

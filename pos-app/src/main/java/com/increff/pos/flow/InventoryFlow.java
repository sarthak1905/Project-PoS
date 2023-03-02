package com.increff.pos.flow;

import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class InventoryFlow {

    @Autowired
    private InventoryService inventoryService;


    public InventoryPojo get(Integer id) throws ApiException {
        ValidationUtil.checkId(id);
        return inventoryService.get(id);
    }

    public List<InventoryPojo> getAll() {
        return inventoryService.getAll();
    }

    public void update(Integer id, InventoryPojo inventoryPojo) throws ApiException {
        ValidationUtil.checkId(id);
        ValidationUtil.checkPojo(inventoryPojo);
        inventoryService.update(id, inventoryPojo);
    }

}

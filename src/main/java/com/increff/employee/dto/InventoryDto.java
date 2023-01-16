package com.increff.employee.dto;

import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InventoryDto {

    @Autowired
    private InventoryService inventoryService;

    public void add(InventoryForm form) throws ApiException {
        InventoryPojo b = convert(form);
        inventoryService.add(b);
    }

    public InventoryData get(int id) throws ApiException{
        InventoryPojo b = inventoryService.get(id);
        return convert(b);
    }

    public List<InventoryData> getAll() {
        List<InventoryPojo> inventoryList = inventoryService.getAll();
        List<InventoryData> inventoryDataList = new ArrayList<>();
        for(InventoryPojo b: inventoryList){
            inventoryDataList.add(convert(b));
        }
        return inventoryDataList;
    }

    public void update(int id, InventoryForm form) throws ApiException{
        InventoryPojo b = convert(form);
        inventoryService.update(id, b);
    }

    public void delete(int id) throws ApiException{
        inventoryService.delete(id);
    }

    private InventoryData convert(InventoryPojo b) {
        InventoryData d = new InventoryData();
        d.setQuantity(b.getQuantity());
        d.setId(b.getId());
        return d;
    }

    private static InventoryPojo convert(InventoryForm f){
        InventoryPojo b = new InventoryPojo();
        b.setId(f.getId());
        b.setQuantity(f.getQuantity());
        return b;
    }


    public boolean isValidInventory(int id, int quantity) throws ApiException {
        InventoryPojo p = inventoryService.get(id);
        if(p.getQuantity() < quantity){
            return false;
        }
        return true;
    }
}

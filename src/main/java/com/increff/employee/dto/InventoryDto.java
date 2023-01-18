package com.increff.employee.dto;

import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InventoryDto {

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductService productService;

    public void add(InventoryForm form) throws ApiException {
        InventoryPojo inventoryPojo = convertFormToPojo(form);
        inventoryService.add(inventoryPojo);
    }

    public InventoryData get(int id) throws ApiException{
        InventoryPojo b = inventoryService.get(id);
        return convertPojoToData(b);
    }

    public List<InventoryData> getAll() {
        List<InventoryPojo> inventoryList = inventoryService.getAll();
        List<InventoryData> inventoryDataList = new ArrayList<>();
        for(InventoryPojo b: inventoryList){
            inventoryDataList.add(convertPojoToData(b));
        }
        return inventoryDataList;
    }

    public void update(int id, InventoryForm inventoryForm) throws ApiException{
        InventoryPojo b = convertFormToPojo(inventoryForm);
        validateForm(inventoryForm);
        inventoryService.update(id, b);
    }

    public void delete(int id) throws ApiException{
        inventoryService.delete(id);
    }

    private InventoryData convertPojoToData(InventoryPojo b) {
        InventoryData d = new InventoryData();
        d.setQuantity(b.getQuantity());
        d.setBarcode(productService.getBarcodeFromProductId(b.getId()));
        d.setId(b.getId());
        return d;
    }

    private InventoryPojo convertFormToPojo(InventoryForm f) throws ApiException {
        InventoryPojo b = new InventoryPojo();
        b.setId(productService.getProductIdFromBarcode(f.getBarcode()));
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

    private void validateForm(InventoryForm inventoryForm) throws ApiException {
        if(inventoryForm.getQuantity() < 0){
            throw new ApiException("Quantity cannot be negative!");
        }
    }
}

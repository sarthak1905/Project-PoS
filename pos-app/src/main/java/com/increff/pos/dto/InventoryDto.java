package com.increff.pos.dto;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
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
        InventoryPojo inventoryPojo = inventoryService.get(id);
        return convertPojoToData(inventoryPojo);
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
        InventoryPojo inventoryPojo = convertFormToPojo(inventoryForm);
        validateForm(inventoryForm);
        inventoryService.update(id, inventoryPojo);
    }

    public void updateByBarcode(String barcode, InventoryForm inventoryForm) throws ApiException{
        InventoryPojo inventoryPojo = convertFormToPojo(inventoryForm);
        validateForm(inventoryForm);
        Integer productId = productService.getProductIdFromBarcode(barcode);
        inventoryService.update(productId, inventoryPojo);
    }

    public void delete(int id) throws ApiException{
        inventoryService.delete(id);
    }

    private InventoryData convertPojoToData(InventoryPojo b) {
        InventoryData inventoryData = new InventoryData();
        inventoryData.setQuantity(b.getQuantity());
        inventoryData.setBarcode(productService.getBarcodeFromProductId(b.getId()));
        inventoryData.setId(b.getId());
        return inventoryData;
    }

    private InventoryPojo convertFormToPojo(InventoryForm f) throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setId(productService.getProductIdFromBarcode(f.getBarcode()));
        inventoryPojo.setQuantity(f.getQuantity());
        return inventoryPojo;
    }

    public boolean isValidInventory(int id, int quantity) throws ApiException {
        InventoryPojo inventoryPojo = inventoryService.get(id);
        if(inventoryPojo.getQuantity() < quantity){
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

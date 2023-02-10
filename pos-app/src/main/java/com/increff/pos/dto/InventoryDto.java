package com.increff.pos.dto;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.ValidationUtil;
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

    public void add(InventoryForm inventoryForm) throws ApiException {
        ValidationUtil.validateForms(inventoryForm);
        int productId = productService.getProductIdFromBarcode(inventoryForm.getBarcode());
        InventoryPojo inventoryPojo = ConvertUtil.convertInventoryFormToPojo(inventoryForm, productId);
        inventoryService.add(inventoryPojo);
    }

    public InventoryData get(int id) throws ApiException{
        InventoryPojo inventoryPojo = inventoryService.get(id);
        String barcode = productService.getBarcodeFromProductId(inventoryPojo.getId());
        return ConvertUtil.convertInventoryPojoToData(inventoryPojo, barcode);
    }

    public List<InventoryData> getAll() {
        List<InventoryPojo> inventoryList = inventoryService.getAll();
        List<InventoryData> inventoryDataList = new ArrayList<>();
        for(InventoryPojo inventoryPojo: inventoryList){
            String barcode = productService.getBarcodeFromProductId(inventoryPojo.getId());
            inventoryDataList.add(ConvertUtil.convertInventoryPojoToData(inventoryPojo, barcode));
        }
        return inventoryDataList;
    }

    public void update(int id, InventoryForm inventoryForm) throws ApiException{
        ValidationUtil.validateForms(inventoryForm);
        int productId = productService.getProductIdFromBarcode(inventoryForm.getBarcode());
        InventoryPojo inventoryPojo = ConvertUtil.convertInventoryFormToPojo(inventoryForm, productId);
        inventoryService.update(id, inventoryPojo);
    }

    public void updateByBarcode(String barcode, InventoryForm inventoryForm) throws ApiException{
        ValidationUtil.validateForms(inventoryForm);
        int productId = productService.getProductIdFromBarcode(inventoryForm.getBarcode());
        InventoryPojo inventoryPojo = ConvertUtil.convertInventoryFormToPojo(inventoryForm, productId);
        inventoryService.update(productId, inventoryPojo);
    }

    public void delete(int id) throws ApiException{
        inventoryService.delete(id);
    }

    // TODO Remove if not required
    public boolean isValidInventory(int id, int quantity) throws ApiException {
        InventoryPojo inventoryPojo = inventoryService.get(id);
        if(inventoryPojo.getQuantity() < quantity){
            return false;
        }
        return true;
    }

}

package com.increff.pos.dto;

import com.increff.pos.flow.InventoryFlow;
import com.increff.pos.flow.ProductFlow;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryDto {

    @Autowired
    private InventoryFlow inventoryFlow;
    @Autowired
    private ProductFlow productFlow;

    public InventoryData get(Integer id) throws ApiException{
        checkId(id);
        InventoryPojo inventoryPojo = inventoryFlow.get(id);
        String barcode = productFlow.getBarcodeFromProductId(inventoryPojo.getId());
        String name = productFlow.getProductNameFromProductId(inventoryPojo.getId());
        return ConvertUtil.convertInventoryPojoToData(inventoryPojo, barcode, name);
    }

    public List<InventoryData> getAll() throws ApiException {
        List<InventoryPojo> inventoryList = inventoryFlow.getAll();
        List<InventoryData> inventoryDataList = new ArrayList<>();
        for(InventoryPojo inventoryPojo: inventoryList){
            String barcode = productFlow.getBarcodeFromProductId(inventoryPojo.getId());
            String name = productFlow.getProductNameFromProductId(inventoryPojo.getId());
            inventoryDataList.add(ConvertUtil.convertInventoryPojoToData(inventoryPojo, barcode, name));
        }
        return inventoryDataList;
    }

    public void update(Integer id, InventoryForm inventoryForm) throws ApiException{
        ValidationUtil.validateForms(inventoryForm);
        Integer productId = productFlow.getProductIdFromBarcode(inventoryForm.getBarcode());
        InventoryPojo inventoryPojo = ConvertUtil.convertInventoryFormToPojo(inventoryForm, productId);
        inventoryFlow.update(id, inventoryPojo);
    }

    public void updateByBarcode(String barcode, InventoryForm inventoryForm) throws ApiException{
        ValidationUtil.validateForms(inventoryForm);
        int productId = productFlow.getProductIdFromBarcode(inventoryForm.getBarcode());
        InventoryPojo inventoryPojo = ConvertUtil.convertInventoryFormToPojo(inventoryForm, productId);
        inventoryFlow.update(productId, inventoryPojo);
    }

    private void checkId(Integer id) throws ApiException {
        if(id == null){
            throw new ApiException("ID provided is null!");
        }
    }

}

package com.increff.pos.flow;

import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class ProductFlow {

    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;

    public void add(ProductPojo productPojo) throws ApiException {
        ValidationUtil.checkPojo(productPojo);
        String barcode = productPojo.getBarcode();
        productService.add(productPojo);
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setId(productService.getProductIdFromBarcode(barcode));
        inventoryPojo.setQuantity(0);
        inventoryService.add(inventoryPojo);
    }

    public ProductPojo get(Integer id) throws ApiException {
        return productService.get(id);
    }

    public List<ProductPojo> getAll() {
        return productService.getAll();
    }

    public void update(Integer id, ProductPojo productPojo) throws ApiException {
        ValidationUtil.checkId(id);
        ValidationUtil.checkPojo(productPojo);
        productService.update(id, productPojo);
    }

    public Boolean isValidBarcode(String barcode) {
        return productService.isValidBarcode(barcode);
    }

    public String getBarcodeFromProductId(Integer id) throws ApiException {
        ValidationUtil.checkId(id);
        return productService.getBarcodeFromProductId(id);
    }

    public String getProductNameFromProductId(Integer id) throws ApiException {
        ValidationUtil.checkId(id);
        return productService.getProductNameFromProductId(id);
    }

    public Integer getProductIdFromBarcode(String barcode) throws ApiException {
        return productService.getProductIdFromBarcode(barcode);
    }

}



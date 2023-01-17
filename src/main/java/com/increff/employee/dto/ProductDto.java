package com.increff.employee.dto;

import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.ProductService;
import com.increff.employee.util.ProductUtil;
import com.increff.employee.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDto {

    @Autowired
    private InventoryDto inventoryDto;

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    public void add(ProductForm form) throws ApiException {
        ProductPojo p = convertToPojo(form);
        String barcode = p.getBarcode();
        ProductUtil.normalize(p);
        validateData(p);
        productService.add(p);
        InventoryData inventoryData = new InventoryData();
        inventoryData.setId(productService.getProductIdFromBarcode(barcode));
        inventoryData.setBarcode(barcode);
        inventoryData.setQuantity(0);
        inventoryDto.add(inventoryData);
    }


    public ProductData get(int id) throws ApiException{
        ProductPojo p = productService.get(id);
        return convertToData(p);
    }

    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> productList = productService.getAll();
        List<ProductData> productDataList = new ArrayList<>();
        for(ProductPojo p: productList){
            productDataList.add(convertToData(p));
        }
        return productDataList;
    }

    public void update(int id, ProductForm form) throws ApiException{
        ProductPojo p = convertToPojo(form);
        productService.update(id, p);
    }

    public void delete(int id) throws ApiException{
        productService.delete(id);
    }

    private ProductData convertToData(ProductPojo p) throws ApiException {
        ProductData d = new ProductData();
        BrandPojo b = brandService.get(p.getBrand_category());
        d.setId(p.getId());
        d.setName(p.getName());
        d.setBrand(b.getBrand());
        d.setCategory(b.getCategory());
        d.setBarcode(p.getBarcode());
        d.setMrp(p.getMrp());
        return d;
    }

    private ProductPojo convertToPojo(ProductForm f) throws ApiException {
        ProductPojo p = new ProductPojo();
        BrandPojo b = brandService.getBrandCategory(f.getBrand(), f.getCategory());
        brandService.checkIfNull(b);
        p.setBrand_category(b.getId());
        p.setBarcode(f.getBarcode());
        p.setName(f.getName());
        p.setMrp(f.getMrp());
        return p;
    }

    private void validateData(ProductPojo p) throws ApiException {
        if (StringUtil.isEmpty(p.getBarcode())) {
            throw new ApiException("Barcode cannot be empty!");
        }
        if (StringUtil.isEmpty(p.getName())) {
            throw new ApiException("Name cannot be empty!");
        }
        if (!productService.isValidBarcode(p)) {
            throw new ApiException("Barcode already exists!");
        }
    }
}

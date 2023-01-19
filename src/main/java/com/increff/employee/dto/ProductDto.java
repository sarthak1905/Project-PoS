package com.increff.employee.dto;

import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.InventoryService;
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
    private InventoryService inventoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;

    public void add(ProductForm productForm) throws ApiException {
        validateForm(productForm);
        ProductPojo productPojo = convertToPojo(productForm);
        String barcode = productPojo.getBarcode();
        ProductUtil.normalize(productPojo);
        productService.add(productPojo);
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setId(productService.getProductIdFromBarcode(barcode));
        inventoryPojo.setQuantity(0);
        inventoryService.add(inventoryPojo);
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

    public void update(int id, ProductForm productForm) throws ApiException{
        validateForm(productForm);
        ProductPojo productPojo = convertToPojo(productForm);
        productService.update(id, productPojo);
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

    private ProductPojo convertToPojo(ProductForm productForm) throws ApiException {
        ProductPojo productPojo = new ProductPojo();
        String brand = productForm.getBrand();
        String category = productForm.getCategory();
        validateBrandCategoryNames(brand, category);
        BrandPojo brandPojo = brandService.getBrandCategory(brand, category);
        brandService.checkIfNull(brandPojo);
        productPojo.setBrand_category(brandPojo.getId());
        productPojo.setBarcode(productForm.getBarcode());
        productPojo.setName(productForm.getName());
        productPojo.setMrp(productForm.getMrp());
        return productPojo;
    }

    private void validateForm(ProductForm productForm) throws ApiException {
        if (StringUtil.isEmpty(productForm.getBarcode())) {
            throw new ApiException("Barcode cannot be empty!");
        }
        if (StringUtil.isEmpty(productForm.getName())) {
            throw new ApiException("Name cannot be empty!");
        }
        if (!productService.isValidBarcode(productForm.getBarcode())) {
            throw new ApiException("Barcode already exists!");
        }
    }

    private void validateBrandCategoryNames(String brand, String category) throws ApiException {
        brand = brand.toLowerCase().trim();
        category = category.toLowerCase().trim();
        if (StringUtil.isEmpty(brand)){
            throw new ApiException("Brand name cannot be empty!");
        }
        if (StringUtil.isEmpty(category)){
            throw new ApiException("Brand category cannot be empty!");
        }
    }
}

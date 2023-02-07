package com.increff.pos.dto;

import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ProductUtil;
import com.increff.pos.util.StringUtil;
import com.increff.pos.util.ValidationUtil;
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
        validateForm(productForm, false);
        ProductUtil.normalize(productForm);
        ProductPojo productPojo = convertToPojo(productForm);
        String barcode = productPojo.getBarcode();
        productService.add(productPojo);
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setId(productService.getProductIdFromBarcode(barcode));
        inventoryPojo.setQuantity(0);
        inventoryService.add(inventoryPojo);
    }

    public ProductData get(int id) throws ApiException{
        ProductPojo productPojo = productService.get(id);
        return convertToData(productPojo);
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
        validateForm(productForm, true);
        ProductUtil.normalize(productForm);
        ProductPojo productPojo = convertToPojo(productForm);
        productService.update(id, productPojo);
    }

    public void delete(int id) throws ApiException{
        productService.delete(id);
    }

    private ProductData convertToData(ProductPojo productPojo) throws ApiException {
        ProductData productData = new ProductData();
        BrandPojo brandPojo = brandService.get(productPojo.getBrandCategory());
        productData.setId(productPojo.getId());
        productData.setName(productPojo.getName());
        productData.setBrand(brandPojo.getBrand());
        productData.setCategory(brandPojo.getCategory());
        productData.setBarcode(productPojo.getBarcode());
        productData.setMrp(productPojo.getMrp());
        return productData;
    }

    private ProductPojo convertToPojo(ProductForm productForm) throws ApiException {
        ProductPojo productPojo = new ProductPojo();
        String brand = productForm.getBrand();
        String category = productForm.getCategory();
        validateBrandCategoryNames(brand, category);
        BrandPojo brandPojo = brandService.getBrandCategory(brand, category);
        if (brandPojo == null) {
            throw new ApiException("Brand-category combination does not exist!");
        }
        productPojo.setBrandCategory(brandPojo.getId());
        productPojo.setBarcode(productForm.getBarcode());
        productPojo.setName(productForm.getName());
        productPojo.setMrp(productForm.getMrp());
        return productPojo;
    }

    private void validateForm(ProductForm productForm, boolean update) throws ApiException {
        ValidationUtil.validateForms(productForm);
        if (!update) {
            if (!productService.isValidBarcode(productForm.getBarcode())) {
                throw new ApiException("Barcode already exists!");
            }
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

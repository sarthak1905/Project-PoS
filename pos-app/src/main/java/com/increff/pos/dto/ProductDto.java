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
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.NormalizeUtil;
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
        NormalizeUtil.normalize(productForm);
        String brand = productForm.getBrand();
        String category = productForm.getCategory();
        BrandPojo brandPojo = brandService.getByBrandCategory(brand, category);
        checkBrandPojo(brandPojo);

        int brandId = brandPojo.getId();
        ProductPojo productPojo = ConvertUtil.convertProductFormToPojo(productForm, brandId);
        String barcode = productPojo.getBarcode();
        productService.add(productPojo);
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setId(productService.getProductIdFromBarcode(barcode));
        inventoryPojo.setQuantity(0);
        inventoryService.add(inventoryPojo);
    }

    public ProductData get(int id) throws ApiException{
        ProductPojo productPojo = productService.get(id);
        BrandPojo brandPojo = brandService.get(productPojo.getBrandCategory());
        return ConvertUtil.convertProductPojoToData(productPojo, brandPojo.getBrand(), brandPojo.getCategory());
    }

    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> productList = productService.getAll();
        List<ProductData> productDataList = new ArrayList<>();
        for(ProductPojo productPojo: productList){
            BrandPojo brandPojo = brandService.get(productPojo.getBrandCategory());
            String brand = brandPojo.getBrand();
            String category = brandPojo.getCategory();
            productDataList.add(ConvertUtil.convertProductPojoToData(productPojo, brand, category));
        }
        return productDataList;
    }

    public void update(int id, ProductForm productForm) throws ApiException{
        validateForm(productForm, true);
        NormalizeUtil.normalize(productForm);
        String brand = productForm.getBrand();
        String category = productForm.getCategory();
        BrandPojo brandPojo = brandService.getByBrandCategory(brand, category);
        checkBrandPojo(brandPojo);
        int brandId = brandPojo.getId();
        ProductPojo productPojo = ConvertUtil.convertProductFormToPojo(productForm, brandId);
        productService.update(id, productPojo);
    }

    private void checkBrandPojo(BrandPojo brandPojo) throws ApiException {
        if (brandPojo == null) {
            throw new ApiException("Brand-category combination does not exist!");
        }
    }


    private void validateForm(ProductForm productForm, boolean update) throws ApiException {
        ValidationUtil.validateForms(productForm);
        if (!update) {
            if (!productService.isValidBarcode(productForm.getBarcode())) {
                throw new ApiException("Barcode already exists!");
            }
        }
    }

}

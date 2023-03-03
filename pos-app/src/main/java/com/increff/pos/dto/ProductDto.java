package com.increff.pos.dto;

import com.increff.pos.flow.BrandFlow;
import com.increff.pos.flow.ProductFlow;
import com.increff.pos.model.ProductData;
import com.increff.pos.model.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.NormalizeUtil;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductDto {

    @Autowired
    private ProductFlow productFlow;
    @Autowired
    private BrandFlow brandFlow;

    public void add(ProductForm productForm) throws ApiException {
        validateForm(productForm, false);
        NormalizeUtil.normalize(productForm);
        BrandPojo brandPojo = brandFlow.getByBrandCategory(productForm.getBrand(), productForm.getCategory());
        ProductPojo productPojo = ConvertUtil.convertProductFormToPojo(productForm, brandPojo.getId());
        productFlow.add(productPojo);
    }

    public ProductData get(Integer id) throws ApiException{
        checkId(id);
        ProductPojo productPojo = productFlow.get(id);
        BrandPojo brandPojo = brandFlow.get(productPojo.getBrandCategory());
        return ConvertUtil.convertProductPojoToData(productPojo, brandPojo.getBrand(), brandPojo.getCategory());
    }

    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> productList = productFlow.getAll();
        List<ProductData> productDataList = new ArrayList<>();
        for(ProductPojo productPojo: productList){
            BrandPojo brandPojo = brandFlow.get(productPojo.getBrandCategory());
            String brand = brandPojo.getBrand();
            String category = brandPojo.getCategory();
            productDataList.add(ConvertUtil.convertProductPojoToData(productPojo, brand, category));
        }
        return productDataList;
    }

    public void update(Integer id, ProductForm productForm) throws ApiException{
        checkId(id);
        validateForm(productForm, true);
        NormalizeUtil.normalize(productForm);
        BrandPojo brandPojo = brandFlow.getByBrandCategory(productForm.getBrand(), productForm.getCategory());
        Integer brandId = brandPojo.getId();
        ProductPojo productPojo = ConvertUtil.convertProductFormToPojo(productForm, brandId);
        productFlow.update(id, productPojo);
    }

    private void validateForm(ProductForm productForm, Boolean isUpdate) throws ApiException {
        ValidationUtil.validateForms(productForm);
        if (isUpdate) {
            return;
        }
        if (productFlow.isValidBarcode(productForm.getBarcode())) {
            return;
        }
        throw new ApiException("Barcode already exists!");
    }

    private void checkId(Integer id) throws ApiException {
        if(id == null){
            throw new ApiException("ID provided is null");
        }
    }
}

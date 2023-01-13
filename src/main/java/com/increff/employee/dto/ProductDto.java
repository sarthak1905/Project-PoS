package com.increff.employee.dto;

import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDto {

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    public void add(ProductForm form) throws ApiException {
        ProductPojo p = convert(form);
        productService.add(p);
    }

    public ProductData get(int id) throws ApiException{
        ProductPojo p = productService.get(id);
        return convert(p);
    }

    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> productList = productService.getAll();
        List<ProductData> productDataList = new ArrayList<>();
        for(ProductPojo p: productList){
            productDataList.add(convert(p));
        }
        return productDataList;
    }

    public void update(int id, ProductForm form) throws ApiException{
        ProductPojo p = convert(form);
        productService.update(id, p);
    }

    public void delete(int id) throws ApiException{
        productService.delete(id);
    }

    private ProductData convert(ProductPojo p) throws ApiException {
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

    private ProductPojo convert(ProductForm f) throws ApiException {
        ProductPojo p = new ProductPojo();
        BrandPojo b = brandService.getBrandCategory(f.getBrand(), f.getCategory());
        if (b == null) {
            throw new ApiException("Brand-category combination does not exist!");
        }
        p.setBrand_category(b.getId());
        p.setBarcode(f.getBarcode());
        p.setName(f.getName());
        p.setMrp(f.getMrp());
        return p;
    }
}

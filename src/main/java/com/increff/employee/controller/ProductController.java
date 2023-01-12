package com.increff.employee.controller;

import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @ApiOperation(value = "Adds a product")
    @RequestMapping(path = "/api/product", method = RequestMethod.POST)
    public void add(@RequestBody ProductForm form) throws ApiException{
        ProductPojo p = convert(form);
        productService.add(p);
    }

    @ApiOperation(value = "Gets a product")
    @RequestMapping(path = "/api/product/{id}", method = RequestMethod.GET)
    public ProductData get(@PathVariable int id) throws ApiException{
        ProductPojo p = productService.get(id);
        return convert(p);
    }

    @ApiOperation(value = "Gets all products")
    @RequestMapping(path = "/api/product", method = RequestMethod.GET)
    public List<ProductData> getAll() throws ApiException {
        List<ProductPojo> productList = productService.getAll();
        List<ProductData> productDataList = new ArrayList<>();
        for(ProductPojo p: productList){
            productDataList.add(convert(p));
        }
        return productDataList;
    }

    @ApiOperation(value = "Updates a product")
    @RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody ProductForm f) throws ApiException {
        ProductPojo p = convert(f);
        productService.update(id, p);
    }

    @ApiOperation(value = "Deletes a product")
    @RequestMapping(path = "/api/product/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) throws ApiException{
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

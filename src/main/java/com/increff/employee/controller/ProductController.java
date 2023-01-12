package com.increff.employee.controller;

import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
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
    private ProductService service;

    @ApiOperation(value = "Adds a product")
    @RequestMapping(path = "/api/product", method = RequestMethod.POST)
    public void add(@RequestBody ProductForm form) throws ApiException{
        ProductPojo p = convert(form);
        service.add(p);
    }

    @ApiOperation(value = "Gets a product")
    @RequestMapping(path = "/api/product/{id}", method = RequestMethod.GET)
    public ProductData get(@PathVariable int id) throws ApiException{
        ProductPojo p = service.get(id);
        return convert(p);
    }

    @ApiOperation(value = "Gets all products")
    @RequestMapping(path = "/api/product", method = RequestMethod.GET)
    public List<ProductData> getAll(){
        List<ProductPojo> brandList = service.getAll();
        List<ProductData> brandDataList = new ArrayList<ProductData>();
        for(ProductPojo p: brandList){
            brandDataList.add(convert(p));
        }
        return brandDataList;
    }

    @ApiOperation(value = "Updates a product")
    @RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody ProductForm f) throws ApiException {
        ProductPojo p = convert(f);
        service.update(id, p);
    }

    @ApiOperation(value = "Deletes a product")
    @RequestMapping(path = "/api/product/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) throws ApiException{
        service.delete(id);
    }

    private static ProductData convert(ProductPojo p){
        ProductData d = new ProductData();
        d.setId(p.getId());
        d.setName(p.getName());
        d.setBrand_category(p.getBrand_category());
        d.setBarcode(p.getBarcode());
        d.setMrp(p.getMrp());
        return d;
    }

    private static ProductPojo convert(ProductForm f){
        ProductPojo p = new ProductPojo();
        p.setBrand_category(f.getBrand_category());
        p.setBarcode(f.getBarcode());
        p.setName(f.getName());
        p.setMrp(f.getMrp());
        return p;
    }

}

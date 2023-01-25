package com.increff.employee.controller;

import com.increff.employee.dto.ProductDto;
import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(path = "/api/products")
public class ProductController {

    @Autowired
    private ProductDto productDto;

    @ApiOperation(value = "Adds a product")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public void add(@RequestBody ProductForm form) throws ApiException{
        productDto.add(form);
    }

    @ApiOperation(value = "Gets a product")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ProductData get(@PathVariable int id) throws ApiException{
        return productDto.get(id);
    }

    @ApiOperation(value = "Gets all products")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<ProductData> getAll() throws ApiException {
        return productDto.getAll();
    }

    @ApiOperation(value = "Updates a product")
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody ProductForm form) throws ApiException {
        productDto.update(id, form);
    }

    @ApiOperation(value = "Deletes a product")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) throws ApiException{
        productDto.delete(id);
    }

}

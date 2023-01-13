package com.increff.employee.controller;

import com.increff.employee.dto.BrandDto;
import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api
@RestController
public class BrandController {

    @Autowired
    private BrandDto brandDto;

    @ApiOperation(value = "Adds a Brand")
    @RequestMapping(path = "/api/brand", method = RequestMethod.POST)
    public void add(@RequestBody BrandForm form) throws ApiException {
        brandDto.add(form);
    }

    @ApiOperation(value = "Gets a Brand")
    @RequestMapping(path = "/api/brand/{id}", method = RequestMethod.GET)
    public BrandData get(@PathVariable int id) throws ApiException{
        return brandDto.get(id);
    }

    @ApiOperation(value = "Gets all Brands")
    @RequestMapping(path = "/api/brand", method = RequestMethod.GET)
    public List<BrandData> getAll(){
        return brandDto.getAll();
    }

    @ApiOperation(value = "Updates a Brand")
    @RequestMapping(path = "/api/brand/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody BrandForm form) throws ApiException {
        brandDto.update(id, form);
    }

    @ApiOperation(value = "Deletes a Brand")
    @RequestMapping(path = "/api/brand/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) throws ApiException{
        brandDto.delete(id);
    }

}

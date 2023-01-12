package com.increff.employee.controller;

import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.EmployeePojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Api
@RestController
public class BrandController {

    @Autowired
    private BrandService service;

    @ApiOperation(value = "Adds a Brand")
    @RequestMapping(path = "/api/brand", method = RequestMethod.POST)
    public void add(@RequestBody BrandForm form) throws ApiException {
        BrandPojo b = convert(form);
        service.add(b);
    }

    @ApiOperation(value = "Gets a Brand")
    @RequestMapping(path = "/api/brand/{id}", method = RequestMethod.GET)
    public BrandForm get(@PathVariable int id) throws ApiException{
        BrandPojo b = service.get(id);
        return convert(b);
    }

    private static BrandData convert(BrandPojo b){
        BrandData d = new BrandData();
        d.setBrand(b.getBrand());
        d.setCategory(b.getCategory());
        d.setId(b.getId());
        return d;
    }

    private static BrandPojo convert(BrandForm f){
        BrandPojo b = new BrandPojo();
        b.setBrand(f.getBrand());
        b.setCategory(f.getCategory());
        return b;
    }

}

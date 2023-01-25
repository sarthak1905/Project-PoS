package com.increff.pos.controller;

import com.increff.pos.dto.BrandDto;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(path = "/api/brands")
public class BrandController {

    @Autowired
    private BrandDto brandDto;

    @ApiOperation(value = "Adds a Brand")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public void add(@RequestBody BrandForm form) throws ApiException {
        brandDto.add(form);
    }

    @ApiOperation(value = "Gets a Brand")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public BrandData get(@PathVariable int id) throws ApiException{
        return brandDto.get(id);
    }

    @ApiOperation(value = "Gets all Brands")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<BrandData> getAll(){
        return brandDto.getAll();
    }

    @ApiOperation(value = "Updates a Brand")
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody BrandForm form) throws ApiException {
        brandDto.update(id, form);
    }

    @ApiOperation(value = "Deletes a Brand")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) throws ApiException{
        brandDto.delete(id);
    }

}

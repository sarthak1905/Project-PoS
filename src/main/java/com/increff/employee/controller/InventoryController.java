package com.increff.employee.controller;

import com.increff.employee.dto.InventoryDto;
import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api
@RestController
public class InventoryController {

    @Autowired
    private InventoryDto inventoryDto;

    @ApiOperation(value = "Adds an inventory")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.POST)
    public void add(@RequestBody InventoryForm form) throws ApiException {
        inventoryDto.add(form);
    }

    @ApiOperation(value = "Gets an inventory")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
    public InventoryData get(@PathVariable int id) throws ApiException{
        return inventoryDto.get(id);
    }

    @ApiOperation(value = "Gets all inventory")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
    public List<InventoryData> getAll(){
        return inventoryDto.getAll();
    }

    @ApiOperation(value = "Updates an inventory")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody InventoryForm form) throws ApiException {
        inventoryDto.update(id, form);
    }

    @ApiOperation(value = "Deletes an inventory")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) throws ApiException{
        inventoryDto.delete(id);
    }

}

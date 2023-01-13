package com.increff.employee.controller;

import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.InventoryService;
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
    private InventoryService inventoryService;

    @ApiOperation(value = "Adds an inventory")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.POST)
    public void add(@RequestBody InventoryForm form) throws ApiException {
        InventoryPojo b = convert(form);
        inventoryService.add(b);
    }

    @ApiOperation(value = "Gets an inventory")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
    public InventoryData get(@PathVariable int id) throws ApiException{
        InventoryPojo b = inventoryService.get(id);
        return convert(b);
    }

    @ApiOperation(value = "Gets all inventory")
    @RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
    public List<InventoryData> getAll(){
        List<InventoryPojo> inventoryList = inventoryService.getAll();
        List<InventoryData> inventoryDataList = new ArrayList<>();
        for(InventoryPojo b: inventoryList){
            inventoryDataList.add(convert(b));
        }
        return inventoryDataList;
    }

    @ApiOperation(value = "Updates an inventory")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody InventoryForm f) throws ApiException {
        InventoryPojo b = convert(f);
        inventoryService.update(id, b);
    }

    @ApiOperation(value = "Deletes an inventory")
    @RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) throws ApiException{
        inventoryService.delete(id);
    }

    private InventoryData convert(InventoryPojo b) {
        InventoryData d = new InventoryData();
        d.setQuantity(b.getQuantity());
        d.setId(b.getId());
        return d;
    }

    private static InventoryPojo convert(InventoryForm f){
        InventoryPojo b = new InventoryPojo();
        b.setId(f.getId());
        b.setQuantity(f.getQuantity());
        return b;
    }

}

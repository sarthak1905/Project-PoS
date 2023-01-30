package com.increff.pos.controller;

import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(path = "/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryDto inventoryDto;

    @ApiOperation(value = "Adds an inventory")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public void add(@RequestBody InventoryForm form) throws ApiException {
        inventoryDto.add(form);
    }

    @ApiOperation(value = "Gets an inventory")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public InventoryData get(@PathVariable Integer id) throws ApiException{
        return inventoryDto.get(id);
    }

    @ApiOperation(value = "Gets all inventory")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<InventoryData> getAll(){
        return inventoryDto.getAll();
    }

    @ApiOperation(value = "Updates an inventory")
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody InventoryForm form) throws ApiException {
        inventoryDto.update(id, form);
    }

    @ApiOperation(value = "Deletes an inventory")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Integer id) throws ApiException{
        inventoryDto.delete(id);
    }

}

package com.increff.employee.controller;

import com.increff.employee.dto.OrderItemDto;
import com.increff.employee.model.OrderItemData;
import com.increff.employee.model.OrderItemForm;
import com.increff.employee.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class OrderItemController {

    @Autowired
    private OrderItemDto orderItemDto;

    @ApiOperation(value = "Adds an orderitem")
    @RequestMapping(path = "/api/orderitem", method = RequestMethod.POST)
    public void add(@RequestBody OrderItemForm form) throws ApiException {
        orderItemDto.add(form);
    }

    @ApiOperation(value = "Gets an orderitem")
    @RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.GET)
    public OrderItemData get(@PathVariable int id) throws ApiException{
        return orderItemDto.get(id);
    }

    @ApiOperation(value = "Gets all orderitem")
    @RequestMapping(path = "/api/orderitem", method = RequestMethod.GET)
    public List<OrderItemData> getAll(){
        return orderItemDto.getAll();
    }

    @ApiOperation(value = "Updates an orderitem")
    @RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody OrderItemForm orderItemForm) throws ApiException {
        orderItemDto.update(id, orderItemForm);
    }

    @ApiOperation(value = "Deletes an orderitem")
    @RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) throws ApiException{
        orderItemDto.delete(id);
    }
}

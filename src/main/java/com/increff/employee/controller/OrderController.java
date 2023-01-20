package com.increff.employee.controller;

import com.increff.employee.dto.OrderDto;
import com.increff.employee.model.OrderData;
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
public class OrderController {

    @Autowired
    private OrderDto orderDto;

    @ApiOperation(value = "Adds an order")
    @RequestMapping(path = "/api/order", method = RequestMethod.POST)
    public void add(@RequestBody List<OrderItemForm> forms) throws ApiException {
        orderDto.add(forms);
    }

    @ApiOperation(value = "Gets an order")
    @RequestMapping(path = "/api/order/{id}", method = RequestMethod.GET)
    public OrderData get(@PathVariable int id) throws ApiException{
        return orderDto.get(id);
    }

    @ApiOperation(value = "Gets all order")
    @RequestMapping(path = "/api/order", method = RequestMethod.GET)
    public List<OrderData> getAll(){
        return orderDto.getAll();
    }

    @ApiOperation(value = "Gets all order items of an order")
    @RequestMapping(path = "/api/order/{id}/items", method = RequestMethod.GET)
    public List<OrderItemData> getOrderItems(@PathVariable Integer id){
        return orderDto.getOrderItems(id);
    }

    @ApiOperation(value = "Updates an order")
    @RequestMapping(path = "/api/order/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody List<OrderItemForm> orderItemForms) throws ApiException {
        orderDto.update(id, orderItemForms);
    }

    @ApiOperation(value = "Deletes an order")
    @RequestMapping(path = "/api/order/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) throws ApiException{
        orderDto.delete(id);
    }
}

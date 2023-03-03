package com.increff.pos.controller;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.*;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Api
@RestController
@RequestMapping(path = "/api/orders")
public class OrderController {

    @Autowired
    private OrderDto orderDto;

    @ApiOperation(value = "Adds an order")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public void add(@RequestBody List<OrderItemForm> forms) throws ApiException {
        orderDto.add(forms);
    }

    @ApiOperation(value = "Gets an order")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public OrderData get(@PathVariable Integer id) throws ApiException{
        return orderDto.get(id);
    }

    @ApiOperation(value = "Gets orders based on input dates")
    @RequestMapping(path = "/filtered", method = RequestMethod.POST)
    public List<OrderData> getFilteredOrders(@RequestBody OrderFilterForm orderFilterForm) throws ApiException {
        return orderDto.getFilteredOrders(orderFilterForm);
    }

    @ApiOperation(value = "Gets all order items of an order")
    @RequestMapping(path = "/{id}/items", method = RequestMethod.GET)
    public List<OrderItemData> getOrderItems(@PathVariable Integer id) throws ApiException {
        return orderDto.getOrderItems(id);
    }

    @ApiOperation(value = "Generates order invoice")
    @RequestMapping(path = "/{id}/invoice", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getOrderInvoice(@PathVariable Integer id) throws ApiException, IOException {
        return orderDto.getOrderInvoice(id);
    }

    @ApiOperation(value = "Updates an order")
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody List<OrderItemForm> orderItemForms) throws ApiException {
        orderDto.update(id, orderItemForms);
    }

    @ApiOperation(value = "Cancels an order")
    @RequestMapping(path = "/{id}/cancel", method = RequestMethod.GET)
    public void cancel(@PathVariable Integer id) throws ApiException {
        orderDto.cancel(id);
    }

}

package com.increff.invoice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@Api
@RestController
@RequestMapping(path = "/api")
public class InvoiceController {

    @ApiOperation(value = "Gets an order")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public String get(){
        return "Reaching here!";
    }
}

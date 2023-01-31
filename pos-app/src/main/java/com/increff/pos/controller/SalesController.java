package com.increff.pos.controller;

import com.increff.pos.dto.SalesDto;
import com.increff.pos.dto.SchedulerDto;
import com.increff.pos.model.SalesData;
import com.increff.pos.model.SalesForm;
import com.increff.pos.model.SchedulerData;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
@RequestMapping(path = "/api/sales")
public class SalesController {

    @Autowired
    private SalesDto salesDto;

    @ApiOperation(value = "Gets the scheduler table")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<SalesData> getAll(@RequestBody SalesForm salesForm) throws ApiException {
        return salesDto.getAll(salesForm);
    }


}

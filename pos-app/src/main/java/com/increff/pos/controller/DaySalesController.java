package com.increff.pos.controller;

import com.increff.pos.dto.DaySalesDto;
import com.increff.pos.model.DaySalesData;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
@RequestMapping(path = "/api/sales")
public class DaySalesController {

    @Autowired
    private DaySalesDto daySalesDto;

    @ApiOperation(value = "Gets the scheduler table")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<DaySalesData> getAll() throws ApiException {
        return daySalesDto.getAll();
    }


}

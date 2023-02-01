package com.increff.pos.controller;

import com.increff.pos.dto.DaySalesDto;
import com.increff.pos.model.DaySalesData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(path = "/api/scheduler")
public class SchedulerController {

    @Autowired
    private DaySalesDto daySalesDto;

    @ApiOperation(value = "Gets the scheduler table")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<DaySalesData> getAll(){
        return daySalesDto.getAll();
    }

}

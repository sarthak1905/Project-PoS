package com.increff.pos.controller;

import com.increff.pos.dto.ReportDto;
import com.increff.pos.model.*;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Api
@RestController
@RequestMapping(path = "/api/reports")
public class ReportController {

    @Autowired
    private ReportDto reportDto;

    @ApiOperation(value = "Gets the pos-day-sales table")
    @RequestMapping(path = "/day-sales", method = RequestMethod.POST)
    public List<DaySalesData> getFilteredDaySalesReport(@RequestBody DaySalesForm daySalesForm) throws ApiException {
        return reportDto.getFilteredDaySalesReport(daySalesForm);
    }

    @ApiOperation(value = "Gets sales report")
    @RequestMapping(path = "/sales", method = RequestMethod.POST)
    public List<SalesReportData> getSalesReport(@RequestBody SalesReportFilterForm salesReportFilterForm) throws ApiException {
        return reportDto.getSalesReport(salesReportFilterForm);
    }

    @ApiOperation(value = "Gets brand report")
    @RequestMapping(path = "/brand", method = RequestMethod.POST)
    public List<BrandData> getBrandReport(@RequestBody BrandReportFilterForm brandReportFilterForm){
        return reportDto.getBrandReport(brandReportFilterForm);
    }

    @ApiOperation(value = "Gets inventory report")
    @RequestMapping(path = "/inventory", method = RequestMethod.POST)
    public List<InventoryReportData> getFilteredInventoryReport(@RequestBody InventoryReportFilterForm inventoryReportFilterForm) throws ApiException {
        return reportDto.getFilteredInventoryReport(inventoryReportFilterForm);
    }

}

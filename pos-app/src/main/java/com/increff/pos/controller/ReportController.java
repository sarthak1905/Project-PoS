package com.increff.pos.controller;

import com.increff.pos.dto.ReportDto;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.SalesReportData;
import com.increff.pos.model.SalesReportFilterForm;
import com.increff.pos.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(path = "/api/reports")
public class ReportController {

    @Autowired
    private ReportDto reportDto;

    @ApiOperation(value = "Gets sales report")
    @RequestMapping(path = "/sales", method = RequestMethod.POST)
    public List<SalesReportData> getSalesReport(@RequestBody SalesReportFilterForm salesReportFilterForm) throws ApiException {
        return reportDto.getSalesReport(salesReportFilterForm);
    }

    @ApiOperation(value = "Gets brand report")
    @RequestMapping(path = "/brand", method = RequestMethod.GET)
    public List<BrandData> getAll(){
        return reportDto.getBrandReport();
    }

    @ApiOperation(value = "Gets inventory report")
    @RequestMapping(path = "/inventory", method = RequestMethod.GET)
    public List<InventoryReportData> getInventoryReport() throws ApiException {
        return reportDto.getInventoryReport();
    }

}

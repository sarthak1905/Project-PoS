package com.increff.pos.dto;

import com.increff.pos.model.SalesData;
import com.increff.pos.model.SalesForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SalesDto {

    @Autowired
    private SalesService salesService;

    public List<SalesData> getAll(SalesForm salesForm) throws ApiException {
        if(salesForm.getStartDate().isAfter(salesForm.getEndDate())){
            throw new ApiException("Start date cannot be after end date!");
        }
        return salesService.generateSalesDataList(salesForm);
    }
}

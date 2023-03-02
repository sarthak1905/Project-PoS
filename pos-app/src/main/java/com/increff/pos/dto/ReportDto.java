package com.increff.pos.dto;

import com.increff.pos.flow.DaySalesFlow;
import com.increff.pos.flow.ReportFlow;
import com.increff.pos.model.*;
import com.increff.pos.pojo.DaySalesPojo;
import com.increff.pos.service.*;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReportDto {

    @Autowired
    private DaySalesFlow daySalesFlow;
    @Autowired
    private ReportFlow reportFlow;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void createDailyReport() throws ApiException {
        LocalDate localDate = java.time.LocalDate.now().minus(1, ChronoUnit.DAYS);
        daySalesFlow.createDaySalesEntry(localDate);
    }

    public List<DaySalesData> getFilteredDaySalesReport(DaySalesForm daySalesForm) throws ApiException {
        ValidationUtil.validateForms(daySalesForm);
        String filteredStartDate = daySalesForm.getStartDate();
        String filteredEndDate = daySalesForm.getEndDate();
        LocalDate startDate = LocalDate.parse(filteredStartDate, DATE_TIME_FORMATTER);
        LocalDate endDate = LocalDate.parse(filteredEndDate, DATE_TIME_FORMATTER);
        ValidationUtil.validateDaySalesDates(startDate, endDate);

        List<DaySalesPojo> daySalesPojoList = daySalesFlow.getBetweenDates(startDate, endDate);
        List<DaySalesData> daySalesDataList = new ArrayList<>();
        for(DaySalesPojo daySalesPojo : daySalesPojoList){
            daySalesDataList.add(ConvertUtil.convertDaySalesPojoToData(daySalesPojo));
        }
        return daySalesDataList;
    }

    public List<SalesReportData> getSalesReport(SalesReportFilterForm salesReportFilterForm) throws ApiException {
        String filterBrandName = salesReportFilterForm.getBrand();
        if(filterBrandName == null){salesReportFilterForm.setBrand("");}
        String filterCategoryName = salesReportFilterForm.getCategory();
        if(filterCategoryName == null){salesReportFilterForm.setCategory("");}

        return reportFlow.generateSalesReportDataList(salesReportFilterForm);
    }

    public List<BrandData> getBrandReport(BrandReportFilterForm brandReportFilterForm) {
        String filterBrandName = brandReportFilterForm.getBrand();
        if(filterBrandName == null){brandReportFilterForm.setBrand("");}
        String filterCategoryName = brandReportFilterForm.getCategory();
        if(filterCategoryName == null){brandReportFilterForm.setCategory("");}

        return reportFlow.getBrandReport(brandReportFilterForm);
    }

    public List<InventoryReportData> getFilteredInventoryReport(InventoryReportFilterForm inventoryReportFilterForm) throws ApiException {
        String filterBrandName = inventoryReportFilterForm.getBrand();
        if(filterBrandName == null){inventoryReportFilterForm.setBrand("");}
        String filterCategoryName = inventoryReportFilterForm.getCategory();
        if(filterCategoryName == null){inventoryReportFilterForm.setCategory("");}

        return reportFlow.getInventoryReport(inventoryReportFilterForm);
    }

    public void refreshDaySalesEntity() throws ApiException {
        reportFlow.refreshDaySalesEntity();
    }

}

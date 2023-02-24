package com.increff.pos.dto;

import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import com.increff.pos.pojo.DaySalesPojo;
import com.increff.pos.service.*;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ReportDto {

    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private DaySalesService daySalesService;

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void createDailyReport() throws ApiException {
        LocalDate localDate = java.time.LocalDate.now().minus(1, ChronoUnit.DAYS);
        daySalesService.createDaySalesEntry(localDate);
    }

    public List<DaySalesData> getFilteredDaySalesReport(DaySalesForm daySalesForm) throws ApiException {
        ValidationUtil.validateForms(daySalesForm);
        String filteredStartDate = daySalesForm.getStartDate();
        String filteredEndDate = daySalesForm.getEndDate();
        LocalDate startDate = LocalDate.parse(filteredStartDate, dateTimeFormatter);
        LocalDate endDate = LocalDate.parse(filteredEndDate, dateTimeFormatter);
        if(startDate.isAfter(endDate)){
            throw new ApiException("Start date cannot be after end date!");
        }
        if(ChronoUnit.DAYS.between(startDate, endDate) > 90){
            throw new ApiException("Max duration between start date and end date is 90 days");
        }

        List<DaySalesPojo> daySalesPojoList = daySalesService.getBetweenDates(startDate, endDate);
        List<DaySalesData> daySalesDataList = new ArrayList<>();
        for(DaySalesPojo daySalesPojo : daySalesPojoList){
            daySalesDataList.add(ConvertUtil.convertDaySalesPojoToData(daySalesPojo));
        }
        return daySalesDataList;
    }

    public List<SalesReportData> getSalesReport(SalesReportFilterForm salesReportFilterForm) throws ApiException {
        List<OrderPojo> orderPojoList = getOrderPojoListByDates(salesReportFilterForm);

        String filterBrandName = salesReportFilterForm.getBrand();
        if(filterBrandName == null){filterBrandName = "";}
        String filterCategoryName = salesReportFilterForm.getCategory();
        if(filterCategoryName == null){filterCategoryName = "";}

        List<OrderItemPojo> allOrderItemList = new ArrayList<>();
        for(OrderPojo orderPojo: orderPojoList) {
            List<OrderItemPojo> orderItemPojoList = orderItemService.getByOrderId(orderPojo.getId());
            allOrderItemList.addAll(orderItemPojoList);
        }

        return generateSalesReportDataList(allOrderItemList, filterBrandName, filterCategoryName);
    }

    public List<BrandData> getBrandReport(BrandReportFilterForm brandReportFilterForm) {
        String filterBrandName = brandReportFilterForm.getBrand();
        if(filterBrandName == null){filterBrandName = "";}
        String filterCategoryName = brandReportFilterForm.getCategory();
        if(filterCategoryName == null){filterCategoryName = "";}

        List<BrandPojo> brandPojoList = brandService.getAll();
        List<BrandData> brandDataList = new ArrayList<>();
        for(BrandPojo brandPojo: brandPojoList){
            if(brandPojo.getBrand().equals(filterBrandName) || filterBrandName.equals("") || filterBrandName.equals("---All---")) {
                if (brandPojo.getCategory().equals(filterCategoryName) || filterCategoryName.equals("") || filterCategoryName.equals("---All---")) {
                    brandDataList.add(ConvertUtil.convertBrandPojoToData(brandPojo));
                }
            }
        }
        return brandDataList;
    }

    public List<InventoryReportData> getFilteredInventoryReport(InventoryReportFilterForm inventoryReportFilterForm) throws ApiException {
        String filterBrandName = inventoryReportFilterForm.getBrand();
        if(filterBrandName == null){filterBrandName = "";}
        String filterCategoryName = inventoryReportFilterForm.getCategory();
        if(filterCategoryName == null){filterCategoryName = "";}

        List<InventoryReportData> inventoryReportDataList = new ArrayList<>();
        List<ProductPojo> productPojoList = productService.getAll();
        HashMap<Integer, Integer> brandIdToQuantityMap = new HashMap<>();
        for(ProductPojo productPojo: productPojoList){
            BrandPojo brandPojo = brandService.get(productPojo.getBrandCategory());
            if(brandPojo.getBrand().equals(filterBrandName) || filterBrandName.equals("") || filterBrandName.equals("---All---")) {
                if (brandPojo.getCategory().equals(filterCategoryName) || filterCategoryName.equals("") || filterCategoryName.equals("---All---")) {
                    InventoryPojo inventoryPojo = inventoryService.get(productPojo.getId());
                    Integer quantity = inventoryPojo.getQuantity();
                    Integer brandId = brandPojo.getId();
                    brandIdToQuantityMap.put(brandId, brandIdToQuantityMap.getOrDefault(brandId, 0) + quantity);
                }
            }
        }

        for(Map.Entry<Integer, Integer> entry : brandIdToQuantityMap.entrySet()){
            InventoryReportData inventoryReportData = new InventoryReportData();
            BrandPojo brandPojo = brandService.get(entry.getKey());
            inventoryReportData.setBrand(brandPojo.getBrand());
            inventoryReportData.setCategory(brandPojo.getCategory());
            inventoryReportData.setQuantity(entry.getValue());
            inventoryReportDataList.add(inventoryReportData);
        }
        return inventoryReportDataList;
    }

    public void refreshDaySalesEntity() throws ApiException {
        LocalDate currentDate = java.time.LocalDate.now();
        LocalDate startDate = daySalesService.getLastDate();
        if(startDate == null){
            LocalDateTime firstOrder = invoiceService.getFirstOrderDateTime();
            if(firstOrder == null){
                return;
            }
            startDate = firstOrder.toLocalDate();
        }
        while (!startDate.isAfter(currentDate)){
            daySalesService.createDaySalesEntry(startDate);
            startDate = startDate.plusDays(1);
        }
    }

    private List<OrderPojo> getOrderPojoListByDates(SalesReportFilterForm salesReportFilterForm) throws ApiException {
        LocalDateTime startDate;
        LocalDateTime endDate;
        String filterStartDate = salesReportFilterForm.getStartDate();
        String filterEndDate = salesReportFilterForm.getEndDate();

        List<OrderPojo> orderPojoList = new ArrayList<>();
        if(checkIfNullOrEmpty(filterStartDate) && checkIfNullOrEmpty(filterEndDate)){
            orderPojoList = orderService.getAll();
        }

        if(checkIfNullOrEmpty(filterStartDate) && !checkIfNullOrEmpty(filterEndDate)){
            endDate = LocalDateTime.of(LocalDate.parse(filterEndDate, dateTimeFormatter), LocalTime.MAX);
            orderPojoList = orderService.getBeforeEndDate(endDate);
        }

        if(checkIfNullOrEmpty(filterEndDate) && !checkIfNullOrEmpty(filterStartDate)){
            startDate = LocalDate.parse(filterStartDate, dateTimeFormatter).atStartOfDay();
            orderPojoList = orderService.getAfterStartDate(startDate);
        }

        if(!checkIfNullOrEmpty(filterStartDate) && !checkIfNullOrEmpty(filterEndDate)){
            startDate = LocalDate.parse(filterStartDate, dateTimeFormatter).atStartOfDay();
            endDate = LocalDateTime.of(LocalDate.parse(filterEndDate, dateTimeFormatter), LocalTime.MAX);
            if(startDate.isAfter(endDate)){
                throw new ApiException("Start date cannot be after end date!");
            }
            if(Duration.between(startDate, endDate).compareTo(Duration.ofDays(90)) > 0){
                throw new ApiException("Max duration between start date and end date is 90 days");
            }
            orderPojoList = orderService.getBetweenDates(startDate, endDate);
        }
        return orderPojoList;
    }

    private List<SalesReportData> generateSalesReportDataList(List<OrderItemPojo> allOrderItemList, String filterBrandName, String filterCategoryName) throws ApiException {
        HashMap<Integer, Integer> brandIdToQuantityMap = new HashMap<>();
        HashMap<Integer, Double> brandIdToRevenueMap = new HashMap<>();
        List<SalesReportData> salesReportDataList = new ArrayList<>();
        for(OrderItemPojo orderItemPojo: allOrderItemList){
            ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
            BrandPojo brandPojo = brandService.get(productPojo.getBrandCategory());
            if(brandPojo.getBrand().equals(filterBrandName) || filterBrandName.equals("") || filterBrandName.equals("---All---")) {
                if (brandPojo.getCategory().equals(filterCategoryName) || filterCategoryName.equals("") || filterCategoryName.equals("---All---")) {
                    Integer brandId = brandPojo.getId();
                    Integer quantity = orderItemPojo.getQuantity();
                    Double revenue = orderItemPojo.getSellingPrice() * orderItemPojo.getQuantity();
                    brandIdToQuantityMap.put(brandId, brandIdToQuantityMap.getOrDefault(brandId, 0) + quantity);
                    brandIdToRevenueMap.put(brandId, brandIdToRevenueMap.getOrDefault(brandId, 0.0) + revenue);
                }
            }
        }
        for(Map.Entry<Integer, Integer> entry : brandIdToQuantityMap.entrySet()){
            SalesReportData salesReportData = new SalesReportData();
            BrandPojo brandPojo = brandService.get(entry.getKey());
            salesReportData.setBrand(brandPojo.getBrand());
            salesReportData.setCategory(brandPojo.getCategory());
            salesReportData.setQuantity(entry.getValue());
            salesReportData.setRevenue(brandIdToRevenueMap.get(entry.getKey()));
            salesReportDataList.add(salesReportData);
        }
        return salesReportDataList;
    }

    private boolean checkIfNullOrEmpty(String inputString) {
        return inputString == null || inputString.equals("");
    }

}

package com.increff.pos.dto;

import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public void createDailyReport() throws ApiException {
        LocalDate localDate = java.time.LocalDate.now().minus(1, ChronoUnit.DAYS);
        daySalesService.createDaySalesEntry(localDate);
    }

    public List<DaySalesData> getDaySalesReport() {
        List<DaySalesPojo> daySalesPojoList = daySalesService.getAll();
        List<DaySalesData> daySalesDataList = new ArrayList<>();
        for(DaySalesPojo daySalesPojo : daySalesPojoList){
            daySalesDataList.add(convertDaySalesPojoToData(daySalesPojo));
        }
        return daySalesDataList;
    }

    private DaySalesData convertDaySalesPojoToData(DaySalesPojo daySalesPojo) {
        DaySalesData daySalesData = new DaySalesData();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        daySalesData.setDate(daySalesPojo.getDate().format(dateTimeFormatter));
        daySalesData.setTotalRevenue(daySalesPojo.getTotalRevenue());
        daySalesData.setInvoicedItemsCount(daySalesPojo.getInvoicedItemsCount());
        daySalesData.setInvoicedOrdersCount(daySalesPojo.getInvoicedOrdersCount());
        return daySalesData;
    }

    public List<SalesReportData> getSalesReport(SalesReportFilterForm salesReportFilterForm) throws ApiException {
        List<SalesReportData> salesReportDataList = new ArrayList<>();
        List<OrderPojo> orderPojoList = new ArrayList<>();
        String filterStartDate = salesReportFilterForm.getStartDate();
        String filterEndDate = salesReportFilterForm.getEndDate();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String filterBrandName = salesReportFilterForm.getBrand();
        if(filterBrandName == null){filterBrandName = "";}

        String filterCategoryName = salesReportFilterForm.getCategory();
        if(filterCategoryName == null){filterCategoryName = "";}

        LocalDateTime startDate;
        LocalDateTime endDate;

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
            orderPojoList = orderService.getBetweenDates(startDate, endDate);
        }

        List<OrderItemPojo> allOrderItemList = new ArrayList<>();
        for(OrderPojo orderPojo: orderPojoList) {
            List<OrderItemPojo> orderItemPojoList = orderItemService.getByOrderId(orderPojo.getId());
            allOrderItemList.addAll(orderItemPojoList);
        }

        HashMap<Integer, Integer> brandIdToQuantityMap = new HashMap<>();
        HashMap<Integer, Double> brandIdToRevenueMap = new HashMap<>();
        for(OrderItemPojo orderItemPojo: allOrderItemList){
            ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
            BrandPojo brandPojo = brandService.get(productPojo.getBrandCategory());
            if(brandPojo.getBrand().equals(filterBrandName) || filterBrandName.equals("")) {
                if (brandPojo.getCategory().equals(filterCategoryName) || filterCategoryName.equals("")) {
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

    public List<BrandData> getBrandReport() {
        List<BrandPojo> brandPojoList = brandService.getAll();
        List<BrandData> brandDataList = new ArrayList<>();
        for(BrandPojo brandPojo: brandPojoList){
            brandDataList.add(convertBrandPojoToData(brandPojo));
        }
        return brandDataList;
    }

    public List<InventoryReportData> getInventoryReport() throws ApiException {
        List<InventoryReportData> inventoryReportDataList = new ArrayList<>();
        List<ProductPojo> productPojoList = productService.getAll();
        HashMap<Integer, Integer> brandIdToQuantityMap = new HashMap<>();
        for(ProductPojo productPojo: productPojoList){
            BrandPojo brandPojo = brandService.get(productPojo.getBrandCategory());
            InventoryPojo inventoryPojo = inventoryService.get(productPojo.getId());
            Integer quantity = inventoryPojo.getQuantity();
            Integer brandId = brandPojo.getId();
            brandIdToQuantityMap.put(brandId, brandIdToQuantityMap.getOrDefault(brandId, 0) + quantity);
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

    private static BrandData convertBrandPojoToData(BrandPojo brandPojo){
        BrandData brandData = new BrandData();
        brandData.setBrand(brandPojo.getBrand());
        brandData.setCategory(brandPojo.getCategory());
        brandData.setId(brandPojo.getId());
        return brandData;
    }

}

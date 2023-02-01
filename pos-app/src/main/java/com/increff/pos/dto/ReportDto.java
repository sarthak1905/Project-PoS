package com.increff.pos.dto;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.SalesReportData;
import com.increff.pos.model.SalesReportFilterForm;
import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    private OrderItemService orderItemService;

    public List<SalesReportData> getSalesReport(SalesReportFilterForm salesReportFilterForm) throws ApiException {
        List<SalesReportData> salesReportDataList = new ArrayList<>();
        List<OrderPojo> orderPojoList = new ArrayList<>();
        String filterStartDate = salesReportFilterForm.getStartDate();
        String filterEndDate = salesReportFilterForm.getEndDate();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        String filterBrandName = salesReportFilterForm.getBrand();
        if(filterBrandName == null){filterBrandName = "";}

        String filterCategoryName = salesReportFilterForm.getCategory();
        if(filterCategoryName == null){filterCategoryName = "";}

        LocalDateTime startDate;
        LocalDateTime endDate;

        if(filterStartDate == null && filterEndDate == null){
            orderPojoList = orderService.getAll();
        }

        if(filterStartDate == null && filterEndDate != null){
            endDate = LocalDateTime.of(LocalDate.parse(filterEndDate, dateTimeFormatter), LocalTime.MAX);
            orderPojoList = orderService.getBeforeEndDate(endDate);
        }

        if(filterEndDate == null && filterStartDate != null){
            startDate = LocalDate.parse(filterStartDate, dateTimeFormatter).atStartOfDay();
            orderPojoList = orderService.getAfterStartDate(startDate);
        }

        if(filterStartDate != null && filterEndDate != null){
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

    private static BrandData convertBrandPojoToData(BrandPojo brandPojo){
        BrandData brandData = new BrandData();
        brandData.setBrand(brandPojo.getBrand());
        brandData.setCategory(brandPojo.getCategory());
        brandData.setId(brandPojo.getId());
        return brandData;
    }

}

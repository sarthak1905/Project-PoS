package com.increff.pos.flow;

import com.increff.pos.model.*;
import com.increff.pos.pojo.*;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.ValidationUtil;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackOn = ApiException.class)
public class ReportFlow {

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

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<SalesReportData> generateSalesReportDataList(SalesReportFilterForm salesReportFilterForm) throws ApiException {
        List<OrderPojo> orderPojoList = getOrderPojoListByDates(salesReportFilterForm);
        String filterBrandName = salesReportFilterForm.getBrand();
        String filterCategoryName = salesReportFilterForm.getCategory();
        List<OrderItemPojo> allOrderItemList = new ArrayList<>();
        for(OrderPojo orderPojo: orderPojoList) {
            List<OrderItemPojo> orderItemPojoList = orderItemService.getByOrderId(orderPojo.getId());
            allOrderItemList.addAll(orderItemPojoList);
        }

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

    public List<BrandData> getBrandReport(BrandReportFilterForm brandReportFilterForm) {
        String filterBrandName = brandReportFilterForm.getBrand();
        String filterCategoryName = brandReportFilterForm.getCategory();
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

    public List<InventoryReportData> getInventoryReport(InventoryReportFilterForm inventoryReportFilterForm) throws ApiException {
        String filterBrandName = inventoryReportFilterForm.getBrand();
        String filterCategoryName = inventoryReportFilterForm.getCategory();

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
            ZonedDateTime firstOrder = invoiceService.getFirstOrderDateTime();
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
        ZonedDateTime startDate;
        ZonedDateTime endDate;
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        String filterStartDate = salesReportFilterForm.getStartDate();
        String filterEndDate = salesReportFilterForm.getEndDate();

        List<OrderPojo> orderPojoList = new ArrayList<>();
        if(ValidationUtil.checkIfNullOrEmpty(filterStartDate) && ValidationUtil.checkIfNullOrEmpty(filterEndDate)){
            orderPojoList = orderService.getAll();
        }

        if(ValidationUtil.checkIfNullOrEmpty(filterStartDate) && !ValidationUtil.checkIfNullOrEmpty(filterEndDate)){
            endDateTime = LocalDateTime.of(LocalDate.parse(filterEndDate, DATE_TIME_FORMATTER), LocalTime.MAX);
            endDate = endDateTime.atZone(ZoneId.systemDefault());
            orderPojoList = orderService.getBeforeEndDate(endDate);
        }

        if(ValidationUtil.checkIfNullOrEmpty(filterEndDate) && !ValidationUtil.checkIfNullOrEmpty(filterStartDate)){
            startDateTime = LocalDate.parse(filterStartDate, DATE_TIME_FORMATTER).atStartOfDay();
            startDate = startDateTime.atZone(ZoneId.systemDefault());
            orderPojoList = orderService.getAfterStartDate(startDate);
        }

        if(!ValidationUtil.checkIfNullOrEmpty(filterStartDate) && !ValidationUtil.checkIfNullOrEmpty(filterEndDate)){
            startDateTime = LocalDate.parse(filterStartDate, DATE_TIME_FORMATTER).atStartOfDay();
            startDate = startDateTime.atZone(ZoneId.systemDefault());
            endDateTime = LocalDateTime.of(LocalDate.parse(filterEndDate, DATE_TIME_FORMATTER), LocalTime.MAX);
            endDate = endDateTime.atZone(ZoneId.systemDefault());
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

}

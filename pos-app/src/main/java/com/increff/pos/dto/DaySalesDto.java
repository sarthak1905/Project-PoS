package com.increff.pos.dto;

import com.increff.pos.model.DaySalesData;
import com.increff.pos.pojo.InvoicePojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.DaySalesPojo;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
public class DaySalesDto {

    @Autowired
    InvoiceService invoiceService;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    DaySalesService daySalesService;


    public void createDailyReport() throws ApiException {
        DaySalesPojo daySalesPojo = new DaySalesPojo();
        LocalDate localDate = java.time.LocalDate.now().minus(1, ChronoUnit.DAYS);
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);
        daySalesPojo.setDate(localDate);

        List<InvoicePojo> invoicePojoList = invoiceService.getInvoicedOrdersBetweenDates(startOfDay, endOfDay);
        daySalesPojo.setInvoicedOrdersCount(invoicePojoList.size());

        int totalItems = 0;
        double totalRevenue = 0.0;
        for(InvoicePojo invoicePojo: invoicePojoList){
            List<OrderItemPojo> orderItemPojoList = orderItemService.getByOrderId(invoicePojo.getOrderId());
            OrderPojo orderPojo = orderService.get(invoicePojo.getOrderId());
            totalItems += orderItemPojoList.size();
            totalRevenue += orderPojo.getOrderTotal();
        }
        daySalesPojo.setInvoicedItemsCount(totalItems);
        daySalesPojo.setTotalRevenue(totalRevenue);
        daySalesService.addOrUpdate(daySalesPojo);
    }

    public List<DaySalesData> getAll() {
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
}

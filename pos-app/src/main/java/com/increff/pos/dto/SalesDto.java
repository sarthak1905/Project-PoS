package com.increff.pos.dto;

import com.increff.pos.pojo.InvoicePojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.SchedulerPojo;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class SalesDto {

    @Autowired
    InvoiceService invoiceService;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    SchedulerService schedulerService;


    public void createScheduler() throws ApiException {
        SchedulerPojo schedulerPojo = new SchedulerPojo();
        LocalDateTime currentDateTime = java.time.LocalDateTime.now();
        LocalDateTime lastDayDateTime = currentDateTime.minus(1, ChronoUnit.DAYS);
        LocalDate lastDayDate = java.time.LocalDate.now().minus(1, ChronoUnit.DAYS);
        schedulerPojo.setDate(lastDayDate);

        List<InvoicePojo> invoicePojoList = invoiceService.getInvoicedOrdersBetweenDates(lastDayDateTime, currentDateTime);
        schedulerPojo.setInvoicedOrdersCount(invoicePojoList.size());

        int totalItems = 0;
        double totalRevenue = 0.0;
        for(InvoicePojo invoicePojo: invoicePojoList){
            List<OrderItemPojo> orderItemPojoList = orderItemService.getByOrderId(invoicePojo.getOrderId());
            OrderPojo orderPojo = orderService.get(invoicePojo.getOrderId());
            totalItems += orderItemPojoList.size();
            totalRevenue += orderPojo.getOrderTotal();
        }
        schedulerPojo.setInvoicedItemsCount(totalItems);
        schedulerPojo.setTotalRevenue(totalRevenue);

        schedulerService.addOrUpdate(schedulerPojo);
    }

}

package com.increff.pos.service;

import com.increff.pos.dao.DaySalesDao;
import com.increff.pos.pojo.DaySalesPojo;
import com.increff.pos.pojo.InvoicePojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.*;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class DaySalesService {

    @Autowired
    DaySalesDao daySalesDao;
    @Autowired
    InvoiceService invoiceService;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderItemService orderItemService;

    public void addOrUpdate(DaySalesPojo daySalesPojo) {
        DaySalesPojo existingDaySalesPojo = daySalesDao.selectByDate(daySalesPojo.getDate());
        if (existingDaySalesPojo == null) {
            daySalesDao.insert(daySalesPojo);
        }
        else{
            existingDaySalesPojo.setInvoicedOrdersCount(daySalesPojo.getInvoicedOrdersCount());
            existingDaySalesPojo.setInvoicedItemsCount(daySalesPojo.getInvoicedItemsCount());
            existingDaySalesPojo.setTotalRevenue(daySalesPojo.getTotalRevenue());
        }
    }

    public List<DaySalesPojo> getAll() {
        return daySalesDao.selectAll();
    }

    public LocalDate getLastDate() {
        return daySalesDao.selectLatestDate();
    }

    public void createDaySalesEntry(LocalDate date) throws ApiException {
        LocalDateTime startOfDayTime = date.atStartOfDay();
        LocalDateTime endOfDayTime = date.atTime(LocalTime.MAX);
        ZonedDateTime startOfDay = startOfDayTime.atZone(ZoneId.systemDefault());
        ZonedDateTime endOfDay = endOfDayTime.atZone(ZoneId.systemDefault());
        DaySalesPojo daySalesPojo = new DaySalesPojo();
        daySalesPojo.setDate(date);

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
        addOrUpdate(daySalesPojo);
    }

    public List<DaySalesPojo> getBetweenDates(LocalDate startDate, LocalDate endDate) {
        return daySalesDao.selectBetweenDates(startDate, endDate);
    }
}

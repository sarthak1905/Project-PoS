package com.increff.pos.flow;

import com.increff.pos.pojo.DaySalesPojo;
import com.increff.pos.pojo.InvoicePojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.*;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class DaySalesFlow {

    @Autowired
    private DaySalesService daySalesService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderService orderService;

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
        daySalesService.addOrUpdate(daySalesPojo);
    }

    public List<DaySalesPojo> getBetweenDates(LocalDate startDate, LocalDate endDate) {
        return daySalesService.getBetweenDates(startDate, endDate);
    }

    public LocalDate getLastDate() {
        return daySalesService.getLastDate();
    }
}

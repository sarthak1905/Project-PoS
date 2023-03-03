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

    public LocalDate getLastDate() {
        return daySalesDao.selectLatestDate();
    }

    public List<DaySalesPojo> getBetweenDates(LocalDate startDate, LocalDate endDate) {
        return daySalesDao.selectBetweenDates(startDate, endDate);
    }
}

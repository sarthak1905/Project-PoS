package com.increff.pos.service;

import com.increff.pos.dao.DaySalesDao;
import com.increff.pos.pojo.DaySalesPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    public List<DaySalesPojo> getAll() {
        return daySalesDao.selectAll();
    }
}

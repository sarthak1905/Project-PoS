package com.increff.pos.service;

import com.increff.pos.dao.SchedulerDao;
import com.increff.pos.pojo.SchedulerPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class SchedulerService {

    @Autowired
    SchedulerDao schedulerDao;

    public void addOrUpdate(SchedulerPojo schedulerPojo) {
        SchedulerPojo existingSchedulerPojo = schedulerDao.selectByDate(schedulerPojo.getDate());
        if (existingSchedulerPojo == null) {
            schedulerDao.insert(schedulerPojo);
        }
        else{
            existingSchedulerPojo.setInvoicedOrdersCount(schedulerPojo.getInvoicedOrdersCount());
            existingSchedulerPojo.setInvoicedItemsCount(schedulerPojo.getInvoicedItemsCount());
            existingSchedulerPojo.setTotalRevenue(schedulerPojo.getTotalRevenue());
        }
    }
}

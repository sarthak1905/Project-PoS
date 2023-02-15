package com.increff.pos.service;

import com.increff.pos.dao.InvoiceDao;
import com.increff.pos.pojo.InvoicePojo;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class InvoiceService {

    @Autowired
    private InvoiceDao invoiceDao;
    
    public void add(InvoicePojo invoicePojo) throws ApiException {
        InvoicePojo existingPojo = invoiceDao.select_id(invoicePojo.getOrderId());
        if(existingPojo == null) {
            invoiceDao.insert(invoicePojo);
        }
    }
    
    public List<InvoicePojo> getAll(){
        return invoiceDao.selectAll();
    }

    public List<InvoicePojo> getInvoicedOrdersBetweenDates(LocalDateTime startDate, LocalDateTime endDate) throws ApiException {
        if(startDate.isAfter(endDate)){
            throw new ApiException("Start date cannot be after end date for scheduler!");
        }
        return invoiceDao.selectInvoicedOrdersBetweenDates(startDate, endDate);
    }

    public InvoicePojo getOrNull(Integer orderId) {
        return invoiceDao.select_id(orderId);
    }
    public LocalDateTime getFirstOrderDateTime() {
        return invoiceDao.selectFirstOrderDateTime();
    }
}

package com.increff.pos.flow;

import com.increff.pos.pojo.InvoicePojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InvoiceService;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;

@Service
@Transactional(rollbackOn = ApiException.class)
public class InvoiceFlow {

    @Autowired
    private InvoiceService invoiceService;


    public InvoicePojo getOrNull(Integer orderId) throws ApiException {
        ValidationUtil.checkId(orderId);
        return invoiceService.getOrNull(orderId);
    }

    public void add(InvoicePojo invoicePojo) throws ApiException {
        ValidationUtil.checkPojo(invoicePojo);
        invoiceService.add(invoicePojo);
    }

    public ZonedDateTime getFirstOrderDateTime() {
        return invoiceService.getFirstOrderDateTime();
    }

    public InvoicePojo getInvoiceDetails(Integer orderId) throws ApiException {
        InvoicePojo invoicePojo = new InvoicePojo();
        InvoicePojo existingInvoicePojo = invoiceService.getOrNull(orderId);
        invoicePojo.setOrderId(orderId);
        if(existingInvoicePojo == null) {
            invoicePojo.setInvoiceDate(java.time.ZonedDateTime.now());
        }
        else{
            invoicePojo.setInvoiceDate(existingInvoicePojo.getInvoiceDate());
        }
        return invoicePojo;
    }
}

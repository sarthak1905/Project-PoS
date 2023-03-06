package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.InvoicePojo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class InvoiceServiceTest extends AbstractUnitTest {

    private static final ZonedDateTime invoiceDateTime = ZonedDateTime.now();
    private static final int orderId = 999;
    private static final String invoicePath = "invoicePath";
    @Autowired
    private InvoiceService invoiceService;

    @Test()
    public void testExistingAdd() throws ApiException {
        InvoicePojo invoicePojo = new InvoicePojo();
        invoicePojo.setInvoiceDate(invoiceDateTime);
        invoicePojo.setOrderId(orderId);
        invoicePojo.setPath(invoicePath);
        invoiceService.add(invoicePojo);
    }

    @Test
    public void testAdd() throws ApiException {
        InvoicePojo invoicePojo = new InvoicePojo();
        invoicePojo.setInvoiceDate(invoiceDateTime);
        invoicePojo.setOrderId(orderId + 1);
        invoicePojo.setPath(invoicePath);
        invoiceService.add(invoicePojo);
        List<InvoicePojo> invoicePojoList = invoiceService.getAll();
        if(invoicePojoList.size()!= 2) {
            fail();
        }
        InvoicePojo invoicePojo1 = invoicePojoList.get(1);
        assertEquals(invoicePojo1.getInvoiceDate(), invoiceDateTime);
        assertEquals(invoicePojo1.getOrderId(), (Integer)(orderId + 1));
        assertEquals(invoicePojo1.getPath(), invoicePath);
    }

    @Test
    public void testGetAll() throws ApiException {
        InvoicePojo invoicePojo = new InvoicePojo();
        invoicePojo.setInvoiceDate(invoiceDateTime);
        invoicePojo.setOrderId(orderId + 1);
        invoicePojo.setPath(invoicePath);
        invoiceService.add(invoicePojo);

        List<InvoicePojo> invoicePojoList = invoiceService.getAll();
        if(invoicePojoList.size()!= 2){
            fail();
        }
        InvoicePojo invoicePojo1 = invoicePojoList.get(0);
        assertEquals(invoicePojo1.getInvoiceDate(), invoiceDateTime);
        assertEquals(invoicePojo1.getOrderId(), (Integer)orderId);
        assertEquals(invoicePojo1.getPath(), invoicePath);
        InvoicePojo invoicePojo2 = invoicePojoList.get(1);
        assertEquals(invoicePojo2.getInvoiceDate(), invoiceDateTime);
        assertEquals(invoicePojo2.getOrderId(), (Integer)(orderId+1));
        assertEquals(invoicePojo2.getPath(), invoicePath);
    }

    @Test(expected = ApiException.class)
    public void testInvalidGetInvoicedOrdersBetweenDates() throws ApiException {
        ZonedDateTime startDate = ZonedDateTime.now().plus(1, ChronoUnit.DAYS);
        ZonedDateTime endDate = ZonedDateTime.now();
        invoiceService.getOrdersBetweenDates(startDate, endDate);
    }

    @Before
    public void initInvoiceService() throws ApiException {
        InvoicePojo invoicePojo = new InvoicePojo();
        invoicePojo.setInvoiceDate(invoiceDateTime);
        invoicePojo.setOrderId(orderId);
        invoicePojo.setPath(invoicePath);
        invoiceService.add(invoicePojo);
    }

}

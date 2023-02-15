package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.helper.TestHelper;
import com.increff.pos.pojo.DaySalesPojo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class DaySalesServiceTest extends AbstractUnitTest {

    private static final LocalDate date = LocalDate.now();
    private static final Integer invoicedOrdersCount = 1;
    private static final Integer invoicedItemsCount = 3;
    private static final Double totalRevenue = 199.99;
    @Autowired
    private DaySalesService daySalesService;

    @Test
    public void testUpdateAddOrUpdate(){
        List<DaySalesPojo> daySalesPojoList = daySalesService.getAll();
        if(daySalesPojoList.size() != 1){
            fail();
        }
        DaySalesPojo daySalesPojo = daySalesPojoList.get(0);
        daySalesPojo.setTotalRevenue(totalRevenue + 0.1);
        daySalesPojo.setInvoicedOrdersCount(invoicedOrdersCount + 1);
        daySalesPojo.setInvoicedItemsCount(invoicedItemsCount + 1);
        daySalesService.addOrUpdate(daySalesPojo);
        daySalesPojoList = daySalesService.getAll();
        daySalesPojo = daySalesPojoList.get(0);
        assertEquals(totalRevenue + 0.1, daySalesPojo.getTotalRevenue());
        assertEquals((Integer)(invoicedOrdersCount + 1), daySalesPojo.getInvoicedOrdersCount());
        assertEquals((Integer)(invoicedItemsCount + 1), daySalesPojo.getInvoicedItemsCount());
    }

    @Before
    public void initDaySales(){
        DaySalesPojo daySalesPojo = TestHelper.createDaySalesPojo(date, invoicedOrdersCount, invoicedItemsCount, totalRevenue);
        daySalesService.addOrUpdate(daySalesPojo);
    }

}

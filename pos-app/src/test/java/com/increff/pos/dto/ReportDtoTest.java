package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.model.*;
import com.increff.pos.dao.*;
import com.increff.pos.helper.TestHelper;
import com.increff.pos.pojo.*;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class ReportDtoTest extends AbstractUnitTest {

    @Autowired
    private ReportDto reportDto;
    @Autowired
    private DaySalesDao daySalesDao;
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private InvoiceDao invoiceDao;
    private static final String productName = "name";
    private static final String productBarcode = "barcode";
    private static final String productBrand = "brand";
    private static final String productCategory = "category";
    private static final Double productMrp = 99.99;
    private static final Double sellingPrice = 98.99;
    private static final Integer quantity = 100;
    private static final Integer orderId = 999;
    private static final Integer orderItemsCount = 3;
    private static final LocalDateTime orderTime = LocalDateTime.now();
    private static final Double orderTotal = 199.99;
    private static final boolean invoiced = false;

    @Test
    public void testCreateDailyReport() throws ApiException {
        reportDto.createDailyReport();
        List<DaySalesPojo> daySalesPojoList = daySalesDao.selectAll();
        if(daySalesPojoList.size() != 1){
            fail();
        }
        DaySalesPojo daySalesPojo = daySalesPojoList.get(0);
        LocalDate localDate = LocalDate.now();
        assertEquals(localDate.minus(1, ChronoUnit.DAYS), daySalesPojo.getDate());
        assertEquals(0.0, daySalesPojo.getTotalRevenue());
        assertEquals((Integer)0, daySalesPojo.getInvoicedItemsCount());
        assertEquals((Integer)0, daySalesPojo.getInvoicedOrdersCount());
    }

/*    @Test
    public void testGetDaySalesReport() throws ApiException {
        reportDto.createDailyReport();
        List<com.increff.pos.model.DaySalesData> daySalesDataList = reportDto.getDaySalesReport();
        if(daySalesDataList.size() != 1){
            fail();
        }
        DaySalesData daySalesData = daySalesDataList.get(0);
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        assertEquals(localDate.minus(1, ChronoUnit.DAYS).format(dateTimeFormatter), daySalesData.getDate());
        assertEquals(0.0, daySalesData.getTotalRevenue());
        assertEquals((Integer)0, daySalesData.getInvoicedItemsCount());
        assertEquals((Integer)0, daySalesData.getInvoicedOrdersCount());
    }*/

    @Test
    public void getSalesReport() throws ApiException {
        SalesReportFilterForm salesReportFilterForm = createTemplateSalesReportFilterForm();
        List<SalesReportData> salesReportDataList = reportDto.getSalesReport(salesReportFilterForm);
        if(salesReportDataList.size() != 3){
            fail();
        }
        Double totalRevenue = 0.0;
        Integer quantity = 0;
        for(SalesReportData salesReportData: salesReportDataList){
            totalRevenue += salesReportData.getRevenue();
            quantity += salesReportData.getQuantity();
        }
        assertEquals(sellingPrice*3, totalRevenue);
        assertEquals(orderItemsCount, quantity);
    }

    @Test
    public void getSalesReportWithNullDates() throws ApiException {
        SalesReportFilterForm salesReportFilterForm = createTemplateSalesReportFilterFormWithNoDates();
        List<SalesReportData> salesReportDataList = reportDto.getSalesReport(salesReportFilterForm);
        if(salesReportDataList.size() != 3){
            fail();
        }
        Double totalRevenue = 0.0;
        Integer quantity = 0;
        for(SalesReportData salesReportData: salesReportDataList){
            totalRevenue += salesReportData.getRevenue();
            quantity += salesReportData.getQuantity();
        }
        assertEquals(sellingPrice*3, totalRevenue);
        assertEquals(orderItemsCount, quantity);
    }

    @Test
    public void getSalesReportWithStartDate() throws ApiException {
        SalesReportFilterForm salesReportFilterForm = createTemplateSalesReportFilterFormWithStartDate();
        List<SalesReportData> salesReportDataList = reportDto.getSalesReport(salesReportFilterForm);
        if(salesReportDataList.size() != 3){
            fail();
        }
        Double totalRevenue = 0.0;
        Integer quantity = 0;
        for(SalesReportData salesReportData: salesReportDataList){
            totalRevenue += salesReportData.getRevenue();
            quantity += salesReportData.getQuantity();
        }
        assertEquals(sellingPrice*3, totalRevenue);
        assertEquals(orderItemsCount, quantity);
    }

    @Test
    public void getSalesReportWithEndDate() throws ApiException {
        SalesReportFilterForm salesReportFilterForm = createTemplateSalesReportFilterFormWithEndDate();
        List<SalesReportData> salesReportDataList = reportDto.getSalesReport(salesReportFilterForm);
        if(salesReportDataList.size() != 3){
            fail();
        }
        Double totalRevenue = 0.0;
        Integer quantity = 0;
        for(SalesReportData salesReportData: salesReportDataList){
            totalRevenue += salesReportData.getRevenue();
            quantity += salesReportData.getQuantity();
        }
        assertEquals(sellingPrice*3, totalRevenue);
        assertEquals(orderItemsCount, quantity);
    }


/*    @Test
    public void testGetBrandReport(){
        List<BrandData> brandDataList = reportDto.getBrandReport();
        if(brandDataList.size() != orderItemsCount){
            fail();
        }
        int i = 0;
        for(BrandData brandData: brandDataList){
            assertEquals(brandData.getBrand(), productBrand + i);
            assertEquals(brandData.getCategory(), productCategory + i);
            i++;
        }
    }

    @Test
    public void testGetInventoryReport() throws ApiException {
        List<InventoryReportData> inventoryReportDataList = reportDto.getInventoryReport();
        if(inventoryReportDataList.size() != orderItemsCount){
            fail();
        }
        int i = 0;
        for(InventoryReportData inventoryReportData: inventoryReportDataList){
            assertEquals((Integer)(quantity + i - 1), inventoryReportData.getQuantity());
            assertEquals(productBrand + i, inventoryReportData.getBrand());
            assertEquals(productCategory + i, inventoryReportData.getCategory());
            i++;
        }
    }*/

    @Before
    public void initReportTests() throws ApiException {
        OrderPojo orderPojo = createTemplateOrder();
        InvoicePojo invoicePojo = createTemplateInvoice(orderPojo.getId());
    }

    private SalesReportFilterForm createTemplateSalesReportFilterForm() {
        SalesReportFilterForm salesReportFilterForm = new SalesReportFilterForm();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        salesReportFilterForm.setStartDate(LocalDate.now().minus(1, ChronoUnit.DAYS).format(dateTimeFormatter));
        salesReportFilterForm.setEndDate(LocalDate.now().format(dateTimeFormatter));
        salesReportFilterForm.setBrand(null);
        salesReportFilterForm.setCategory(null);
        return salesReportFilterForm;
    }

    private SalesReportFilterForm createTemplateSalesReportFilterFormWithNoDates() {
        SalesReportFilterForm salesReportFilterForm = new SalesReportFilterForm();
        salesReportFilterForm.setStartDate(null);
        salesReportFilterForm.setEndDate(null);
        salesReportFilterForm.setBrand(null);
        salesReportFilterForm.setCategory(null);
        return salesReportFilterForm;
    }

    private SalesReportFilterForm createTemplateSalesReportFilterFormWithStartDate() {
        SalesReportFilterForm salesReportFilterForm = new SalesReportFilterForm();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        salesReportFilterForm.setStartDate(LocalDate.now().minus(1, ChronoUnit.DAYS).format(dateTimeFormatter));
        salesReportFilterForm.setEndDate(null);
        salesReportFilterForm.setBrand(null);
        salesReportFilterForm.setCategory(null);
        return salesReportFilterForm;
    }

    private SalesReportFilterForm createTemplateSalesReportFilterFormWithEndDate() {
        SalesReportFilterForm salesReportFilterForm = new SalesReportFilterForm();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        salesReportFilterForm.setStartDate(null);
        salesReportFilterForm.setEndDate(LocalDate.now().format(dateTimeFormatter));
        salesReportFilterForm.setBrand(null);
        salesReportFilterForm.setCategory(null);
        return salesReportFilterForm;
    }

    private InvoicePojo createTemplateInvoice(Integer orderId) {
        InvoicePojo invoicePojo = TestHelper.createInvoicePojo(orderId, orderTime, "tempPath");
        invoiceDao.insert(invoicePojo);
        invoicePojo = invoiceDao.selectAll().get(0);
        return invoicePojo;
    }

    private OrderPojo createTemplateOrder() throws ApiException {
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setOrderDate(orderTime);
        orderPojo.setInvoiced(true);
        orderPojo.setId(orderId);
        orderPojo.setOrderTotal(orderTotal);
        orderDao.insert(orderPojo);
        orderPojo = orderDao.selectAll().get(0);
        List<OrderItemPojo> orderItemPojoList = createOrderItemPojoList(0);
        insertOrderItems(orderItemPojoList, orderPojo.getId());
        return orderPojo;
    }

    private void insertOrderItems(List<OrderItemPojo> orderItemPojoList, Integer orderId) throws ApiException {
        for(OrderItemPojo orderItemPojo: orderItemPojoList){
            orderItemPojo.setOrderId(orderId);
            orderItemDao.insert(orderItemPojo);
            InventoryPojo inventoryPojo = inventoryDao.selectId(orderItemPojo.getProductId());
            inventoryPojo.setQuantity(inventoryPojo.getQuantity() - orderItemPojo.getQuantity());
            inventoryService.update(inventoryPojo.getId(), inventoryPojo);
        }
    }

    private List<OrderItemPojo> createOrderItemPojoList(int indexVal) {
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for(int i = indexVal; i < indexVal + orderItemsCount; ++i){
            ProductPojo productPojo = createTemplateProductPojo(productName + i, productCategory + i,
                        productBrand + i, productBarcode + i, productMrp + i, quantity + i);
            int productId = productPojo.getId();
            OrderItemForm orderItemForm = TestHelper.createOrderItemForm(productBarcode + i, 1, sellingPrice);
            OrderItemPojo orderItemPojo = TestHelper.convertOrderItemFormToPojo(orderItemForm, productId);
            orderItemPojoList.add(orderItemPojo);
        }
        return orderItemPojoList;
    }

    private ProductPojo createTemplateProductPojo(String name, String category, String brand, String barcode, Double mrp, Integer quantity){
        ProductPojo productPojo = createNewProductPojo(name, barcode, brand, category, mrp);
        productDao.insert(productPojo);
        productPojo = productDao.selectBarcode(barcode);
        int productId = productPojo.getId();
        InventoryPojo inventoryPojo = createTemplateInventoryPojo(productId, quantity);
        return productPojo;
    }

    private InventoryPojo createTemplateInventoryPojo(int productId, Integer quantity) {
        InventoryPojo inventoryPojo = TestHelper.createInventoryPojo(productId, quantity);
        inventoryDao.insert(inventoryPojo);
        return inventoryPojo;
    }

    private ProductPojo createNewProductPojo(String name, String barcode, String brand, String category, Double mrp) {
        BrandPojo brandPojo = TestHelper.createBrand(brand, category);
        brandDao.insert(brandPojo);
        brandPojo = brandDao.selectBrandCategory(brand, category);
        int brandId = brandPojo.getId();
        return TestHelper.createProduct(name, barcode, brandId, mrp);
    }

}

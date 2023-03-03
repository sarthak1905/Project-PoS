package com.increff.pos.dto;

import com.increff.pos.clients.InvoiceClient;
import com.increff.pos.flow.InventoryFlow;
import com.increff.pos.flow.InvoiceFlow;
import com.increff.pos.flow.OrderFlow;
import com.increff.pos.flow.ProductFlow;
import com.increff.pos.model.*;
import com.increff.pos.pojo.InvoicePojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.*;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service
public class OrderDto {

    @Autowired
    private OrderFlow orderFlow;
    @Autowired
    private ProductFlow productFlow;
    @Autowired
    private InventoryFlow inventoryFlow;
    @Autowired
    private InvoiceFlow invoiceFlow;
    @Autowired
    private InvoiceClient invoiceClient;
    @Value("${invoice.uri}")
    private String invoiceUrl;

    public void add(List<OrderItemForm> orderItemForms) throws ApiException {
        validateOrderItemInputForms(orderItemForms);
        OrderPojo orderPojo = new OrderPojo();
        List<OrderItemPojo> orderItemPojos = new ArrayList<>();
        for(OrderItemForm orderItemForm: orderItemForms){
            int productId = productFlow.getProductIdFromBarcode(orderItemForm.getBarcode());
            orderItemPojos.add(ConvertUtil.convertOrderItemFormToPojo(orderItemForm, productId));
        }
        orderFlow.add(orderPojo, orderItemPojos);
    }

    public OrderData get(Integer id) throws ApiException{
        ValidationUtil.checkId(id);
        OrderPojo orderPojo = orderFlow.get(id);
        return ConvertUtil.convertOrderPojoToData(orderPojo);
    }

    public List<OrderData> getFilteredOrders(OrderFilterForm orderFilterForm) throws ApiException {
        ValidationUtil.validateForms(orderFilterForm);
        String filteredStartDate = orderFilterForm.getStartDate();
        String filteredEndDate = orderFilterForm.getEndDate();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime startDateTime = LocalDate.parse(filteredStartDate, dateTimeFormatter).atStartOfDay();
        LocalDateTime endDateTime = LocalDate.parse(filteredEndDate, dateTimeFormatter).atTime(LocalTime.MAX);
        ZonedDateTime startDate = startDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime endDate = endDateTime.atZone(ZoneId.systemDefault());
        ValidationUtil.validateDates(startDate, endDate);

        List<OrderPojo> orderList = orderFlow.getBetweenDates(startDate, endDate);
        List<OrderData> orderDataList = new ArrayList<>();
        for(OrderPojo orderPojo: orderList){
            orderDataList.add(ConvertUtil.convertOrderPojoToData(orderPojo));
        }
        return orderDataList;
    }

    public List<OrderItemData> getOrderItems(Integer orderId) throws ApiException {
        ValidationUtil.checkId(orderId);
        List<OrderItemPojo> orderItemPojos = orderFlow.getByOrderId(orderId);
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        for(OrderItemPojo orderItemPojo: orderItemPojos){
            String barcode = productFlow.getBarcodeFromProductId(orderItemPojo.getProductId());
            String productName = productFlow.getProductNameFromProductId(orderItemPojo.getProductId());
            OrderItemData orderItemData  = ConvertUtil.convertOrderItemPojoToData(orderItemPojo, barcode, productName);
            orderItemDataList.add(orderItemData);
        }
        return orderItemDataList;
    }

    public ResponseEntity<byte[]> getOrderInvoice(Integer orderId) throws ApiException, IOException {
        ValidationUtil.checkId(orderId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        String filename = "invoice" + orderId +".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        String invoiceDirectoryString = "./generated/invoice"+ orderId +".pdf";
        File file = new File(invoiceDirectoryString);
        OrderPojo orderPojo = orderFlow.get(orderId);

        if(file.exists() && orderPojo.getOrderStatus().equals("invoiced")){
            Path invoicePath = Paths.get(invoiceDirectoryString);
            byte[] contents = Files.readAllBytes(invoicePath);
            return new ResponseEntity<>(contents, headers, HttpStatus.OK);
        }
        InvoicePojo invoicePojo = invoiceFlow.getInvoiceDetails(orderId);
        InvoiceForm invoiceForm = generateInvoiceForm(orderId, invoicePojo);
        try(FileOutputStream fos = new FileOutputStream(file)) {
            byte[] contents = invoiceClient.generateInvoice(invoiceForm);
            fos.write(contents);
            String path = "pos-app/generated/invoice" + orderId + ".pdf";
            invoicePojo.setPath(path);
            invoiceFlow.add(invoicePojo);
            orderFlow.setInvoicedTrue(orderPojo.getId());
            return new ResponseEntity<>(contents, headers, HttpStatus.OK);
        }
        catch(FileNotFoundException e){
            throw new ApiException("Error creating file in system!");
        }
    }

    public void update(Integer orderId, List<OrderItemForm> orderItemForms) throws ApiException{
        orderFlow.validateOrderInvoiceStatus(orderId);
        validateOrderItemInputForms(orderItemForms);
        ValidationUtil.checkId(orderId);
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for(OrderItemForm orderItemForm: orderItemForms){
            int productId = productFlow.getProductIdFromBarcode(orderItemForm.getBarcode());
            orderItemPojoList.add(ConvertUtil.convertOrderItemFormToPojo(orderItemForm, productId));
        }
        orderFlow.update(orderId, orderItemPojoList);
    }

    public void cancel(Integer id) throws ApiException {
        ValidationUtil.checkId(id);
        orderFlow.cancel(id);
    }


    // -----------------------PRIVATE METHODS-------------------------------

    private InvoiceForm generateInvoiceForm(Integer orderId, InvoicePojo invoicePojo) throws ApiException {
        List<OrderItemData> orderItemDataList = generateOrderItemListByOrderId(orderId);
        OrderPojo orderPojo = orderFlow.get(orderId);
        InvoiceForm invoiceForm = new InvoiceForm();
        invoiceForm.setOrderItemData(orderItemDataList);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        invoiceForm.setInvoiceDate(invoicePojo.getInvoiceDate().format(dateTimeFormatter));
        invoiceForm.setOrderDate(orderPojo.getOrderDate().format(dateTimeFormatter));
        invoiceForm.setOrderTotal(orderPojo.getOrderTotal());
        invoiceForm.setOrderId(orderPojo.getId());
        return invoiceForm;
    }

    private List<OrderItemData> generateOrderItemListByOrderId(Integer orderId) throws ApiException {
        List<OrderItemPojo> orderItemPojos = orderFlow.getByOrderId(orderId);
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        for(OrderItemPojo orderItemPojo: orderItemPojos){
            String barcode = productFlow.getBarcodeFromProductId(orderItemPojo.getProductId());
            String productName = productFlow.getProductNameFromProductId(orderItemPojo.getProductId());
            orderItemDataList.add(ConvertUtil.convertOrderItemPojoToData(orderItemPojo, barcode, productName));
        }
        return orderItemDataList;
    }

    private void validateOrderItemInputForms(List<OrderItemForm> orderItemForms) throws ApiException {
        if(orderItemForms.size() == 0){
            throw new ApiException("Minimum 1 order item must exist to place order!");
        }
        for(OrderItemForm orderItemForm: orderItemForms){
            ValidationUtil.validateForms(orderItemForm);
        }
    }

}

package com.increff.pos.dto;

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
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@Component
public class OrderDto {

    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private InvoiceService invoiceService;
    @Value("${invoice.uri}")
    private String invoiceUrl;

    public void add(List<OrderItemForm> orderItemForms) throws ApiException {
        validateOrderItemInputForms(orderItemForms);
        OrderPojo orderPojo = new OrderPojo();
        List<OrderItemPojo> orderItemPojos = new ArrayList<>();
        for(OrderItemForm orderItemForm: orderItemForms){
            int productId = productService.getProductIdFromBarcode(orderItemForm.getBarcode());
            orderItemPojos.add(ConvertUtil.convertOrderItemFormToPojo(orderItemForm, productId));
        }
        double orderTotal = calculateOrderTotal(orderItemForms);
        orderPojo.setOrderTotal(orderTotal);
        orderService.add(orderPojo, orderItemPojos);
    }

    public OrderData get(Integer id) throws ApiException{
        OrderPojo orderPojo = orderService.get(id);
        return ConvertUtil.convertOrderPojoToData(orderPojo);
    }

    public List<OrderData> getFilteredOrders(OrderFilterForm orderFilterForm) throws ApiException {
        ValidationUtil.validateForms(orderFilterForm);
        String filteredStartDate = orderFilterForm.getStartDate();
        String filteredEndDate = orderFilterForm.getEndDate();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime startDate = LocalDate.parse(filteredStartDate, dateTimeFormatter).atStartOfDay();
        LocalDateTime endDate = LocalDate.parse(filteredEndDate, dateTimeFormatter).atTime(LocalTime.MAX);
        if(startDate.isAfter(endDate)){
            throw new ApiException("Start date cannot be after end date!");
        }
        if(Duration.between(startDate, endDate).compareTo(Duration.ofDays(90)) > 0){
            throw new ApiException("Max duration between start date and end date is 90 days");
        }

        List<OrderPojo> orderList = orderService.getBetweenDates(startDate, endDate);
        List<OrderData> orderDataList = new ArrayList<>();
        for(OrderPojo orderPojo: orderList){
            orderDataList.add(ConvertUtil.convertOrderPojoToData(orderPojo));
        }
        return orderDataList;
    }

    public List<OrderItemData> getOrderItems(Integer orderId) throws ApiException {
        List<OrderItemPojo> orderItemPojos = orderItemService.getByOrderId(orderId);
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        for(OrderItemPojo orderItemPojo: orderItemPojos){
            String barcode = productService.getBarcodeFromProductId(orderItemPojo.getProductId());
            String productName = productService.getProductNameFromProductId(orderItemPojo.getProductId());
            OrderItemData orderItemData  = ConvertUtil.convertOrderItemPojoToData(orderItemPojo, barcode, productName);
            orderItemDataList.add(orderItemData);
        }
        return orderItemDataList;
    }

    public ResponseEntity<byte[]> getOrderInvoice(Integer orderId) throws ApiException, IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        String filename = "invoice" + orderId +".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        String invoiceDirectoryString = "./generated/invoice"+ orderId +".pdf";
        File file = new File(invoiceDirectoryString);
        OrderPojo orderPojo = orderService.get(orderId);

        if(file.exists() && orderPojo.isInvoiced()){
            Path invoicePath = Paths.get(invoiceDirectoryString);
            byte[] contents = Files.readAllBytes(invoicePath);
            return new ResponseEntity<>(contents, headers, HttpStatus.OK);
        }

        List<OrderItemPojo> orderItemPojos = orderItemService.getByOrderId(orderId);

        List<OrderItemData> orderItemDataList = new ArrayList<>();
        for(OrderItemPojo orderItemPojo: orderItemPojos){
            String barcode = productService.getBarcodeFromProductId(orderItemPojo.getProductId());
            String productName = productService.getProductNameFromProductId(orderItemPojo.getProductId());
            orderItemDataList.add(ConvertUtil.convertOrderItemPojoToData(orderItemPojo, barcode, productName));
        }

        InvoicePojo invoicePojo = new InvoicePojo();
        InvoicePojo existingInvoicePojo = invoiceService.getOrNull(orderId);
        invoicePojo.setOrderId(orderId);
        if(existingInvoicePojo == null) {
            invoicePojo.setInvoiceDate(java.time.LocalDateTime.now());
        }
        else{
            invoicePojo.setInvoiceDate(existingInvoicePojo.getInvoiceDate());
        }

        InvoiceForm invoiceForm = generateInvoiceForm(orderItemDataList, orderPojo, invoicePojo);
        RestTemplate restTemplate = new RestTemplate();

        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] contents = Base64.getDecoder().decode(restTemplate.postForEntity(invoiceUrl, invoiceForm, byte[].class).getBody());
            fos.write(contents);
            String path = "pos-app/generated/invoice" + orderId + ".pdf";
            invoicePojo.setPath(path);
            invoiceService.add(invoicePojo);
            orderService.setInvoicedTrue(orderPojo.getId());

            return new ResponseEntity<>(contents, headers, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void update(Integer orderId, List<OrderItemForm> orderItemForms) throws ApiException{
        orderService.validateOrderInvoiceStatus(orderId);
        validateOrderItemInputForms(orderItemForms);
        double orderTotal = calculateOrderTotal(orderItemForms);

        List<OrderItemPojo> existingOrderItems = orderItemService.getByOrderId(orderId);
        HashMap<Integer,OrderItemPojo> existingOrderItemMapByID = new HashMap<>();
        HashMap<String,OrderItemPojo> existingOrderItemMapByBarcode = new HashMap<>();

        for(OrderItemPojo orderItemPojo: existingOrderItems){
            String barcode = productService.getBarcodeFromProductId(orderItemPojo.getProductId());
            existingOrderItemMapByID.put(orderItemPojo.getProductId(), orderItemPojo);
            existingOrderItemMapByBarcode.put(barcode, orderItemPojo);
        }
        List<OrderItemPojo> newOrderItemPojos = new ArrayList<>();
        for(OrderItemForm form: orderItemForms){
            OrderItemPojo orderItemPojo = getExistingPojoOrAddNew(form, orderId, existingOrderItemMapByBarcode);
            newOrderItemPojos.add(orderItemPojo);
        }
        orderService.update(newOrderItemPojos, existingOrderItemMapByID, orderId, orderTotal);
    }


    // -----------------------PRIVATE METHODS-------------------------------

    private double calculateOrderTotal(List<OrderItemForm> orderItemForms) {
        double orderTotal = 0.0;
        for(OrderItemForm orderItemForm: orderItemForms){
            orderTotal += orderItemForm.getQuantity() * orderItemForm.getSellingPrice();
        }
        return orderTotal;
    }

    private InvoiceForm generateInvoiceForm(List<OrderItemData> orderItemDataList, OrderPojo orderPojo, InvoicePojo invoicePojo) {
        InvoiceForm invoiceForm = new InvoiceForm();
        invoiceForm.setOrderItemData(orderItemDataList);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        invoiceForm.setInvoiceDate(invoicePojo.getInvoiceDate().format(dateTimeFormatter));
        invoiceForm.setOrderDate(orderPojo.getOrderDate().format(dateTimeFormatter));
        invoiceForm.setOrderTotal(orderPojo.getOrderTotal());
        invoiceForm.setOrderId(orderPojo.getId());
        return invoiceForm;
    }

    private OrderItemPojo getExistingPojoOrAddNew(OrderItemForm orderItemForm, Integer orderId,
                                                  HashMap<String, OrderItemPojo> existingOrderItems)
            throws ApiException {
        int productId = productService.getProductIdFromBarcode(orderItemForm.getBarcode());
        OrderItemPojo newOrderItemPojo = ConvertUtil.convertOrderItemFormToPojo(orderItemForm, productId);
        newOrderItemPojo.setOrderId(orderId);
        if(existingOrderItems.containsKey(orderItemForm.getBarcode())){
            OrderItemPojo existingOrderItemPojo = existingOrderItems.get(orderItemForm.getBarcode());
            newOrderItemPojo.setId(existingOrderItemPojo.getId());
        }
        else{
            orderItemService.add(newOrderItemPojo);
        }
        return newOrderItemPojo;
    }

    private void validateOrderItemInputForms(List<OrderItemForm> orderItemForms) throws ApiException {
        for(OrderItemForm orderItemForm: orderItemForms){
            ValidationUtil.validateForms(orderItemForm);
            int productId = productService.getProductIdFromBarcode(orderItemForm.getBarcode());
            inventoryService.checkInventory(productId, orderItemForm.getQuantity());
            orderService.validateSellingPrice(productId, orderItemForm.getSellingPrice());
        }
    }

}

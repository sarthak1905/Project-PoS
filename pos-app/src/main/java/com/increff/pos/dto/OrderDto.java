package com.increff.pos.dto;

import com.increff.pos.model.*;
import com.increff.pos.pojo.InvoicePojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.*;
import com.increff.pos.util.OrderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
            orderItemPojos.add(convertOrderItemFormToPojo(orderItemForm));
        }
        orderService.add(orderPojo, orderItemPojos);
    }

    public OrderData get(Integer id) throws ApiException{
        OrderPojo b = orderService.get(id);
        return convertOrderPojoToData(b);
    }

    public List<OrderData> getAll(){
        List<OrderPojo> orderList = orderService.getAll();
        List<OrderData> orderDataList = new ArrayList<>();
        for(OrderPojo b: orderList){
            orderDataList.add(convertOrderPojoToData(b));
        }
        return orderDataList;
    }

    public List<OrderItemData> getOrderItems(Integer orderId) throws ApiException {
        List<OrderItemPojo> orderItemPojos = orderItemService.getByOrderId(orderId);
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        for(OrderItemPojo orderItemPojo: orderItemPojos){
            OrderItemData orderItemData  = convertOrderItemPojoToData(orderItemPojo);
            orderItemDataList.add(orderItemData);
        }
        return orderItemDataList;
    }

    public ResponseEntity<byte[]> getOrderInvoice(Integer orderId) throws ApiException {
        List<OrderItemPojo> orderItemPojos = orderItemService.getByOrderId(orderId);
        OrderPojo orderPojo = orderService.get(orderId);

        InvoicePojo invoicePojo = new InvoicePojo();
        invoicePojo.setOrderId(orderId);
        invoicePojo.setInvoiceDate(OrderUtil.getCurrentTime());
        invoiceService.add(invoicePojo);

        List<OrderItemData> orderItemDataList = new ArrayList<>();
        for(OrderItemPojo obj:orderItemPojos){
            orderItemDataList.add(convertOrderItemPojoToData(obj));
        }

        orderService.setInvoicedTrue(orderPojo);

        InvoiceForm invoiceForm = generateInvoiceForm(orderItemDataList, orderPojo, invoicePojo);

        RestTemplate restTemplate = new RestTemplate();
        String url = invoiceUrl;

        byte[] contents = Base64.getDecoder().decode(restTemplate.postForEntity(url, invoiceForm, byte[].class).getBody());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        String filename = "invoice.pdf";
        headers.setContentDispositionFormData(filename, filename);

        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    private InvoiceForm generateInvoiceForm(List<OrderItemData> orderItemDataList, OrderPojo orderPojo, InvoicePojo invoicePojo) {
        InvoiceForm invoiceForm = new InvoiceForm();
        invoiceForm.setOrderItemData(orderItemDataList);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        invoiceForm.setInvoiceDate(invoicePojo.getInvoiceDate().format(dateTimeFormatter));
        invoiceForm.setOrderDate(orderPojo.getDateTime().format(dateTimeFormatter));
        invoiceForm.setOrderTotal(orderPojo.getOrderTotal());
        invoiceForm.setOrderId(orderPojo.getId());
        return invoiceForm;
    }

    public void update(Integer orderId, List<OrderItemForm> orderItemForms) throws ApiException{
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

    private double calculateOrderTotal(List<OrderItemForm> orderItemForms) {
        double orderTotal = 0.0;
        for(OrderItemForm orderItemForm: orderItemForms){
            orderTotal += orderItemForm.getQuantity() * orderItemForm.getSellingPrice();
        }
        return orderTotal;
    }

    public void delete(Integer id) throws ApiException{
        orderService.delete(id);
    }

    private OrderItemPojo getExistingPojoOrAddNew(OrderItemForm orderItemForm, Integer orderId,
                                                  HashMap<String, OrderItemPojo> existingOrderItems)
            throws ApiException {
        OrderItemPojo newOrderItemPojo = convertOrderItemFormToPojo(orderItemForm);
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
            if(orderItemForm.getQuantity() <= 0){
                throw new ApiException("Quantity cannot be below 1 for product with barcode:"
                        + orderItemForm.getBarcode());
            }
            int productId = productService.getProductIdFromBarcode(orderItemForm.getBarcode());
            inventoryService.checkInventory(productId, orderItemForm.getQuantity());
            orderService.validateSellingPrice(productId, orderItemForm.getSellingPrice());
        }
    }

    private OrderData convertOrderPojoToData(OrderPojo orderPojo) {
        OrderData orderData = new OrderData();
        orderData.setId(orderPojo.getId());
        orderData.setDateTime(orderPojo.getDateTime());
        orderData.setOrderTotal(orderPojo.getOrderTotal());
        return orderData;
    }

    public OrderItemData convertOrderItemPojoToData(OrderItemPojo orderItemPojo) throws ApiException {
        OrderItemData orderItemData = new OrderItemData();
        orderItemData.setQuantity(orderItemPojo.getQuantity());
        orderItemData.setId(orderItemPojo.getId());
        orderItemData.setProductId(orderItemPojo.getProductId());
        orderItemData.setOrderId(orderItemPojo.getOrderId());
        orderItemData.setSellingPrice(orderItemPojo.getSellingPrice());
        orderItemData.setBarcode(productService.getBarcodeFromProductId(orderItemPojo.getProductId()));
        orderItemData.setProductName(productService.getProductNameFromProductId(orderItemPojo.getProductId()));
        return orderItemData;
    }

    public OrderItemPojo convertOrderItemFormToPojo(OrderItemForm orderItemForm) throws ApiException {
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setQuantity(orderItemForm.getQuantity());
        orderItemPojo.setSellingPrice(orderItemForm.getSellingPrice());
        orderItemPojo.setProductId(productService.getProductIdFromBarcode(orderItemForm.getBarcode()));
        return orderItemPojo;
    }

}

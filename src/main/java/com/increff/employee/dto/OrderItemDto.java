package com.increff.employee.dto;

import com.increff.employee.model.OrderItemData;
import com.increff.employee.model.OrderItemForm;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.OrderItemService;
import com.increff.employee.service.OrderService;
import com.increff.employee.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderItemDto {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    public void add(OrderItemForm form) throws ApiException {
        OrderPojo orderPojo = new OrderPojo();
        orderService.add(orderPojo);
        OrderItemPojo b = convert(orderPojo.getId(), form);
        orderItemService.add(b);
    }

    public OrderItemData get(int id) throws ApiException{
        OrderItemPojo b = orderItemService.get(id);
        return convert(b);
    }

    public List<OrderItemData> getAll(){
        List<OrderItemPojo> orderItemList = orderItemService.getAll();
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        for(OrderItemPojo b: orderItemList){
            orderItemDataList.add(convert(b));
        }
        return orderItemDataList;
    }

    public void update(int id, OrderItemForm form) throws ApiException{
        OrderItemPojo b = convert(id, form);
        orderItemService.update(id, b);
    }

    public void delete(int id) throws ApiException{
        orderItemService.delete(id);
    }

    public OrderItemData convert(OrderItemPojo b) {
        OrderItemData d = new OrderItemData();
        d.setQuantity(b.getQuantity());
        d.setId(b.getId());
        d.setProductId(b.getProductId());
        d.setOrderId(b.getOrderId());
        d.setSellingPrice(b.getSellingPrice());
        d.setBarcode(productService.getBarcodeFromProductId(b.getProductId()));
        return d;
    }

    public OrderItemPojo convert(int orderId, OrderItemForm f) throws ApiException {
        OrderItemPojo b = new OrderItemPojo();
        b.setQuantity(f.getQuantity());
        b.setSellingPrice(f.getSellingPrice());
        b.setOrderId(orderId);
        b.setProductId(productService.getProductIdFromBarcode(f.getBarcode()));
        return b;
    }

    public OrderItemPojo convert(OrderItemData data) throws ApiException {
        OrderItemPojo b = new OrderItemPojo();
        b.setQuantity(data.getQuantity());
        b.setSellingPrice(data.getSellingPrice());
        b.setOrderId(data.getOrderId());
        b.setProductId(productService.getProductIdFromBarcode(data.getBarcode()));
        return b;
    }

}

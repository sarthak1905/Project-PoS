package com.increff.employee.controller;

import com.increff.employee.model.OrderItemData;
import com.increff.employee.model.OrderItemForm;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.OrderItemService;
import com.increff.employee.service.OrderService;
import com.increff.employee.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api
@RestController
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @ApiOperation(value = "Adds an orderitem")
    @RequestMapping(path = "/api/orderitem", method = RequestMethod.POST)
    public void add(@RequestBody OrderItemForm form) throws ApiException {
        OrderPojo orderPojo = new OrderPojo();
        orderService.add(orderPojo);
        OrderItemPojo b = convert(form, orderPojo.getId());
        orderItemService.add(b);
    }

    @ApiOperation(value = "Gets an orderitem")
    @RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.GET)
    public OrderItemData get(@PathVariable int id) throws ApiException{
        OrderItemPojo b = orderItemService.get(id);
        return convert(b);
    }

    @ApiOperation(value = "Gets all orderitem")
    @RequestMapping(path = "/api/orderitem", method = RequestMethod.GET)
    public List<OrderItemData> getAll(){
        List<OrderItemPojo> orderItemList = orderItemService.getAll();
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        for(OrderItemPojo b: orderItemList){
            orderItemDataList.add(convert(b));
        }
        return orderItemDataList;
    }

/*    @ApiOperation(value = "Updates an orderitem")
    @RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody OrderItemForm f) throws ApiException {
        OrderItemPojo b = convert(f);
        orderItemService.update(id, b);
    }*/

    @ApiOperation(value = "Deletes an orderitem")
    @RequestMapping(path = "/api/orderitem/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) throws ApiException{
        orderItemService.delete(id);
    }

    private OrderItemData convert(OrderItemPojo b) {
        OrderItemData d = new OrderItemData();
        d.setQuantity(b.getQuantity());
        d.setId(b.getId());
        d.setProductId(b.getProductId());
        d.setOrderId(b.getOrderId());
        d.setSellingPrice(b.getSellingPrice());
        d.setBarcode(productService.getBarcode(b.getId()));
        return d;
    }

    private OrderItemPojo convert(OrderItemForm f, int orderId) throws ApiException {
        OrderItemPojo b = new OrderItemPojo();
        b.setQuantity(f.getQuantity());
        b.setSellingPrice(f.getSellingPrice());
        b.setOrderId(orderId);
        b.setProductId(productService.getProductIdFromBarcode(f.getBarcode()));
        return b;
    }

}

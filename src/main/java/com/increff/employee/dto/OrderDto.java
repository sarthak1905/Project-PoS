package com.increff.employee.dto;

import com.increff.employee.model.OrderData;
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
import java.util.HashMap;
import java.util.List;

@Component
public class OrderDto {

    @Autowired
    private OrderItemDto orderItemDto;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private InventoryDto inventoryDto;
    @Autowired
    private ProductService productService;

    public void add(List<OrderItemForm> forms) throws ApiException {
        // TODO Shift business logic to orderService
        OrderPojo orderPojo = new OrderPojo();
        orderService.add(orderPojo);
        for (OrderItemForm orderItemForm: forms){
            if(inventoryDto.isValidInventory(productService.getProductIdFromBarcode(orderItemForm.getBarcode()),
                    orderItemForm.getQuantity())){
                OrderItemPojo b = orderItemDto.convert(orderPojo.getId(), orderItemForm);
                orderItemService.add(b);
            }
            else {
                throw new ApiException("Insufficient quantity available for product with barcode: "
                        + orderItemForm.getBarcode());
            }
        }
    }

    public OrderData get(int id) throws ApiException{
        OrderPojo b = orderService.get(id);
        return convert(b);
    }

    public List<OrderData> getAll(){
        List<OrderPojo> orderList = orderService.getAll();
        List<OrderData> orderDataList = new ArrayList<>();
        for(OrderPojo b: orderList){
            orderDataList.add(convert(b));
        }
        return orderDataList;
    }

    public void update(int id, List<OrderItemData> orderItemData) throws ApiException{
        HashMap<Integer,OrderItemData> map= new HashMap<>();
        for(OrderItemData data: orderItemData){
            map.put(data.getId(),data);
        }
        List<OrderItemPojo> orderItems = orderItemService.getByOrderId(id);
        for(OrderItemPojo orderItem: orderItems){
            if(map.containsKey(orderItem.getId())){
                orderItemService.update(orderItem.getId(), orderItemDto.convert(map.get(orderItem.getId())));
            }
            else{
                orderItemService.delete(orderItem.getId());
            }
        }
    }

    public void delete(int id) throws ApiException{
        orderItemService.delete(id);
    }

    private OrderData convert(OrderPojo b) {
        OrderData d = new OrderData();
        d.setId(b.getId());
        d.setDateTime(b.getDateTime());
        return d;
    }

    private OrderPojo convert(int orderId) {
        OrderPojo b = new OrderPojo();
        b.setId(orderId);
        return b;
    }

}

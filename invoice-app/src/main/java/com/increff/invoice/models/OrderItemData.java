package com.increff.invoice.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemData extends OrderItemForm {

    private Integer id;
    private Integer orderId;
    private Integer productId;
    private String productName;

}

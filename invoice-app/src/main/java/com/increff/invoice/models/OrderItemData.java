package com.increff.invoice.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemData extends OrderItemForm {

    private int id;
    private int orderId;
    private int productId;

}

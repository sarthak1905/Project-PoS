package com.increff.invoice.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InvoiceForm {

    private OrderData orderData;
    private List<OrderItemData> orderItemData;
}

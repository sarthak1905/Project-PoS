package com.increff.pos.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class InvoiceForm {

    private Integer orderId;
    private Double orderTotal;
    private String orderDate;
    private List<OrderItemData> orderItemData;
    private String invoiceDate;
}

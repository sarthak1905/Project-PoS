package com.increff.invoice.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderData {

    private Integer id;
    private LocalDateTime dateTime;
    private boolean invoiceStatus;
    private LocalDateTime invoiceDate;
    private Double orderTotal;

}

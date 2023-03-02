package com.increff.invoice.models;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class OrderData {

    private Integer id;
    private ZonedDateTime dateTime;
    private boolean invoiceStatus;
    private ZonedDateTime invoiceDate;
    private Double orderTotal;

}

package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class OrderData {

    private Integer id;
    private String dateTime;
    private boolean isInvoiced;
    private Double orderTotal;

}

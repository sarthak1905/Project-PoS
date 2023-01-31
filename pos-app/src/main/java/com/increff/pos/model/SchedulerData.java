package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchedulerData {

    private String date;
    private Integer invoicedOrdersCount;
    private Integer invoicedItemsCount;
    private Double totalRevenue;

}

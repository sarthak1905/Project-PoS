package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "pos_day_sales")
public class DaySalesPojo {

    @Id
    private LocalDate date;

    @Column(nullable = false, name = "invoiced_order_count")
    @Min(value = 0)
    private Integer invoicedOrdersCount;

    @Column(nullable = false, name = "invoiced_items_count")
    @Min(value = 0)
    private Integer invoicedItemsCount;

    @Column(nullable = false, name = "total_revenue")
    @Min(value = 0)
    @NotBlank
    private Double totalRevenue;

}

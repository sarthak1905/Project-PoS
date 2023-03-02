package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "pos_day_sales")
public class DaySalesPojo extends AbstractVersionPojo {

    @Id
    private LocalDate date;

    @Column(nullable = false)
    @Min(value = 0)
    private Integer invoicedOrdersCount;

    @Column(nullable = false)
    @Min(value = 0)
    private Integer invoicedItemsCount;

    @Column(nullable = false)
    @Min(value = 0)
    private Double totalRevenue;

}

package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.ZonedDateTime;

// TODO unless specific use case, use IDENTITY as generation strategy
// TODO use non-primitive data types everywhere possible
@Entity
@Getter
@Setter
@Table(name = "orders", indexes = {@Index(name = "order_date_index", columnList = "orderDate"),
                                   @Index(name = "is_invoiced_index", columnList = "isInvoiced")})
public class OrderPojo extends AbstractVersionPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private ZonedDateTime orderDate;

    @Column(nullable = false)
    private Boolean isInvoiced;

    @Column(nullable = false)
    private Double orderTotal;

}

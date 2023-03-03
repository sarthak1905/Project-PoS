package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@Table(name = "orders", indexes = {@Index(name = "order_date_index", columnList = "orderDate"),
                                   @Index(name = "order_status_index", columnList = "orderStatus")})
public class OrderPojo extends AbstractVersionPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private ZonedDateTime orderDate;

    @Column(nullable = false)
    private String orderStatus;

    @Column(nullable = false)
    private Double orderTotal;

}

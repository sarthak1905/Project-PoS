package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class OrderPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false, name = "is_invoiced")
    private boolean isInvoiced;

    @Column(name = "invoice_date")
    private LocalDateTime invoiceDate;

    @Column(name = "order_total")
    private Double orderTotal;

}

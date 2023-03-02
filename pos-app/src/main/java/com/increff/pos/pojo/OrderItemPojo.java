package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Getter
@Setter
@Table(name = "order_items", indexes ={@Index(name="order_id_index", columnList = "orderId")},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"productId", "orderId"})})
public class OrderItemPojo extends AbstractVersionPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer orderId;

    @Column(nullable = false)
    private Integer productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    @Min(value=0)
    private Double sellingPrice;

}

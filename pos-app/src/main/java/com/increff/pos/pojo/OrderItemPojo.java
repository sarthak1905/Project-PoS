package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;

import static com.increff.pos.pojo.GeneratorTable.POS_ORDER_ITEMS_SEQ;
import static com.increff.pos.pojo.GeneratorTable.POS_TABLE_NAME;

@Entity
@Getter
@Setter
@Table(name = "order_items")
public class OrderItemPojo extends AbstractVersionPojo {

    @Id
    @TableGenerator(name = POS_ORDER_ITEMS_SEQ, pkColumnValue = POS_ORDER_ITEMS_SEQ, table = POS_TABLE_NAME)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = POS_ORDER_ITEMS_SEQ)
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

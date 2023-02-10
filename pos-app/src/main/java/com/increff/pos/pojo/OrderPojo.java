package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.increff.pos.pojo.GeneratorTable.POS_ORDER_SEQ;
import static com.increff.pos.pojo.GeneratorTable.POS_TABLE_NAME;

@Entity
@Getter
@Setter
@Table(name = "orders", indexes = {@Index(name = "order_date_index", columnList = "order_date"),
                                   @Index(name = "is_invoiced_index", columnList = "is_invoiced")})
public class OrderPojo extends AbstractVersionPojo {

    @Id
    @TableGenerator(name = POS_ORDER_SEQ, pkColumnValue = POS_ORDER_SEQ, table = POS_TABLE_NAME)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = POS_ORDER_SEQ)
    private Integer id;

    @Column(nullable = false, name = "order_date")
    private LocalDateTime orderDate;

    @Column(nullable = false, name = "is_invoiced")
    private boolean isInvoiced;

    @Column(name = "order_total")
    private Double orderTotal;

}

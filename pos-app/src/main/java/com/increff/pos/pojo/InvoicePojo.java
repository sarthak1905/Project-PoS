package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@Table(name = "invoice")
public class InvoicePojo extends AbstractVersionPojo {

    @Id
    private Integer orderId;

    @Column(nullable = false)
    private ZonedDateTime invoiceDate;

    @Column(nullable = false, length = 100)
    @NotBlank
    private String path;
}

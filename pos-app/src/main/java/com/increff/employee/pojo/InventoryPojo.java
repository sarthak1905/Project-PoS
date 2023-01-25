package com.increff.employee.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;

@Entity
@Getter
@Setter
@Table(name = "inventory")
public class InventoryPojo {

    @Id
    private Integer id;

    @Column(nullable = false)
    @Min(value=0)
    private Integer quantity;

}

package com.increff.employee.pojo;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(indexes ={@Index(columnList = "brand,category")},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"brand", "category"})})
@Getter
@Setter
public class BrandPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String category;

}

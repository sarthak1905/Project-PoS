package com.increff.employee.pojo;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(indexes ={@Index(columnList = "brand,category")},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"brand", "category"})})
public class BrandPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    @Column(nullable = false)
    private String brand;

    @Getter
    @Setter
    @Column(nullable = false)
    private String category;

}

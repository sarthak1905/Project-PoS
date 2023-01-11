package com.increff.employee.dao;

import com.increff.employee.pojo.BrandPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class BrandDao extends AbstractDao{

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void insert(BrandPojo b){
        em.persist(b);
    }
}

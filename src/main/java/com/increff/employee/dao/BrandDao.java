package com.increff.employee.dao;

import com.increff.employee.pojo.BrandPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class BrandDao extends AbstractDao{

    @PersistenceContext
    private EntityManager em;
    private static String select_id = "select b from BrandPojo b where id=:id";
    private static String select_all = "select b from BrandPojo b";

    @Transactional
    public void insert(BrandPojo b){
        em.persist(b);
    }

    public BrandPojo select(int id){
        TypedQuery<BrandPojo> query = getQuery(select_id, BrandPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<BrandPojo> selectAll(){
        TypedQuery<BrandPojo> query = getQuery(select_all, BrandPojo.class);
        return query.getResultList();
    }


}

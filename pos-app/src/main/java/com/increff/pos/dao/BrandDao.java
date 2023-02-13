package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class BrandDao extends AbstractDao{

    private static final String SELECT_ID = "select b from BrandPojo b where id=:id";
    private static final String SELECT_BRAND = "select b from BrandPojo b where brand=:brand";
    private static final String SELECT_CATEGORY = "select b from BrandPojo b where category=:category";
    private static final String SELECT_ALL = "select b from BrandPojo b";
    private static final String SELECT_BRAND_CATEGORY = "select b from BrandPojo b where brand=:brand and category=:category";
    private static final String DELETE_ID = "delete from BrandPojo b where id=:id";


    public void insert(BrandPojo brandPojo){
        em().persist(brandPojo);
    }

    public BrandPojo selectId(int id){
        TypedQuery<BrandPojo> query = getQuery(SELECT_ID, BrandPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public BrandPojo selectBrand(String brand){
        TypedQuery<BrandPojo> query = getQuery(SELECT_BRAND, BrandPojo.class);
        query.setParameter("brand", brand);
        return getSingle(query);
    }

    public BrandPojo selectCategory(String category){
        TypedQuery<BrandPojo> query = getQuery(SELECT_CATEGORY, BrandPojo.class);
        query.setParameter("category", category);
        return getSingle(query);
    }

    public List<BrandPojo> selectAll(){
        TypedQuery<BrandPojo> query = getQuery(SELECT_ALL, BrandPojo.class);
        return query.getResultList();
    }

    public BrandPojo selectBrandCategory(String brand, String category){
        TypedQuery<BrandPojo> query = getQuery(SELECT_BRAND_CATEGORY, BrandPojo.class);
        query.setParameter("brand", brand).setParameter("category", category);
        return getSingle(query);
    }

}

package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class BrandDao extends AbstractDao{

    private static String select_id = "select b from BrandPojo b where id=:id";
    private static String select_brand = "select b from BrandPojo b where brand=:brand";
    private static String select_category = "select b from BrandPojo b where category=:category";
    private static String select_all = "select b from BrandPojo b";
    private static String select_brand_category = "select b from BrandPojo b where brand=:brand and category=:category";
    private static String delete_id = "delete from BrandPojo b where id=:id";


    @Transactional
    public void insert(BrandPojo b){
        em().persist(b);
    }

    public BrandPojo select_id(int id){
        TypedQuery<BrandPojo> query = getQuery(select_id, BrandPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public BrandPojo select_brand(String brand){
        TypedQuery<BrandPojo> query = getQuery(select_brand, BrandPojo.class);
        query.setParameter("brand", brand);
        return getSingle(query);
    }

    public BrandPojo select_category(String category){
        TypedQuery<BrandPojo> query = getQuery(select_category, BrandPojo.class);
        query.setParameter("category", category);
        return getSingle(query);
    }

    public List<BrandPojo> selectAll(){
        TypedQuery<BrandPojo> query = getQuery(select_all, BrandPojo.class);
        return query.getResultList();
    }

    public BrandPojo selectBrandCategory(String brand, String category){
        TypedQuery<BrandPojo> query = getQuery(select_brand_category, BrandPojo.class);
        query.setParameter("brand", brand).setParameter("category", category);
        return getSingle(query);
    }

    public int delete(int id){
        Query query = em().createQuery(delete_id);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

}

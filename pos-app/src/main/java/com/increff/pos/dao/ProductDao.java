package com.increff.pos.dao;

import com.increff.pos.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class ProductDao extends AbstractDao{

    private static final String SELECT_ID = "select p from ProductPojo p where id=:id";
    private static final String SELECT_BRAND_CATEGORY = "select p from ProductPojo p where brand_category=:brand_category";
    private static final String SELECT_BARCODE = "select p from ProductPojo p where barcode=:barcode";
    private static final String SELECT_NAME = "select p from ProductPojo p where name=:name";
    private static final String SELECT_ALL = "select p from ProductPojo p";
    private static final String DELETE_ID = "delete from ProductPojo p where id=:id";


    public void insert(ProductPojo productPojo){
        em().persist(productPojo);
    }

    public ProductPojo selectId(int id){
        TypedQuery<ProductPojo> query = getQuery(SELECT_ID, ProductPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public ProductPojo selectBarcode(String barcode){
        TypedQuery<ProductPojo> query = getQuery(SELECT_BARCODE, ProductPojo.class);
        query.setParameter("barcode", barcode);
        return getSingle(query);
    }

    public ProductPojo selectName(String name){
        TypedQuery<ProductPojo> query = getQuery(SELECT_NAME, ProductPojo.class);
        query.setParameter("name", name);
        return getSingle(query);
    }

    public List<ProductPojo> selectAll(){
        TypedQuery<ProductPojo> query = getQuery(SELECT_ALL, ProductPojo.class);
        return query.getResultList();
    }

    public int delete(int id){
        Query query = em().createQuery(DELETE_ID);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

}

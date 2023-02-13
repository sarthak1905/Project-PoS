package com.increff.pos.dao;

import com.increff.pos.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class InventoryDao extends AbstractDao{

    private static final String SELECT_ID = "select p from InventoryPojo p where id=:id";
    private static final String SELECT_ALL = "select p from InventoryPojo p";
    private static final String DELETE_ID = "delete from InventoryPojo p where id=:id";


    public void insert(InventoryPojo inventoryPojo){
        em().persist(inventoryPojo);
    }

    public InventoryPojo selectId(Integer id){
        TypedQuery<InventoryPojo> query = getQuery(SELECT_ID, InventoryPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<InventoryPojo> selectAll(){
        TypedQuery<InventoryPojo> query = getQuery(SELECT_ALL, InventoryPojo.class);
        return query.getResultList();
    }

}

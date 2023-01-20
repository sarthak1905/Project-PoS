package com.increff.employee.dao;

import com.increff.employee.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class OrderDao extends AbstractDao{

    private static String select_id = "select p from OrderPojo p where id=:id";
    private static String select_all = "select p from OrderPojo p";
    private static String delete_id = "delete from OrderPojo p where id=:id";


    public void insert(OrderPojo p){
        em().persist(p);
    }

    public OrderPojo select_id(int id){
        System.out.println("Here xyz");
        TypedQuery<OrderPojo> query = getQuery(select_id, OrderPojo.class);
        System.out.println("Here abc");
        query.setParameter("id", id);
        System.out.println("Here 123");
        return getSingle(query);
    }

    public List<OrderPojo> selectAll(){
        TypedQuery<OrderPojo> query = getQuery(select_all, OrderPojo.class);
        return query.getResultList();
    }

    public int delete(int id){
        Query query = em().createQuery(delete_id);
        query.setParameter("id", id);
        return query.executeUpdate();
    }
}

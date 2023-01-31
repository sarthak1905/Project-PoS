package com.increff.pos.dao;

import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public class OrderDao extends AbstractDao{

    private static String select_id = "select p from OrderPojo p where id=:id";
    private static String select_all = "select p from OrderPojo p";
    private static String delete_id = "delete from OrderPojo p where id=:id";
    private static String select_between_dates = "select p from OrderPojo p where " +
            "datetime between :startDate and :endDate and is_invoiced=true";


    public void insert(OrderPojo p){
        em().persist(p);
    }

    public OrderPojo select_id(int id){
        TypedQuery<OrderPojo> query = getQuery(select_id, OrderPojo.class);
        query.setParameter("id", id);
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

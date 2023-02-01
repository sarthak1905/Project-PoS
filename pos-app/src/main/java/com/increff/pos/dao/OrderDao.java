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

    private static final String select_id = "select p from OrderPojo p where id=:id";
    private static final String select_all = "select p from OrderPojo p";
    private static final String delete_id = "delete from OrderPojo p where id=:id";
    private static final String select_between_dates = "select p from OrderPojo p where " +
            "datetime between :startDate and :endDate";
    private static final String select_after_date = "select p from OrderPojo p where " +
            "datetime >= :startDate";
    private static final String select_before_date = "select p from OrderPojo p where " +
            "datetime <= :endDate";


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

    public List<OrderPojo> selectBeforeEndDate(LocalDateTime endDate) {
        TypedQuery<OrderPojo> query = getQuery(select_before_date, OrderPojo.class);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    public List<OrderPojo> selectBetweenDates(LocalDateTime startDate,LocalDateTime endDate) {
        TypedQuery<OrderPojo> query = getQuery(select_between_dates, OrderPojo.class);
        query.setParameter("startDate", startDate).setParameter("endDate", endDate);
        return query.getResultList();
    }

    public List<OrderPojo> selectAfterStartDate(LocalDateTime startDate) {
        TypedQuery<OrderPojo> query = getQuery(select_after_date, OrderPojo.class);
        query.setParameter("startDate", startDate);
        return query.getResultList();
    }

}

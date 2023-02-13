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

    private static final String SELECT_ID = "select p from OrderPojo p where id=:id";
    private static final String SELECT_ALL = "select p from OrderPojo p";
    private static final String DELETE_ID = "delete from OrderPojo p where id=:id";
    private static final String SELECT_BETWEEN_DATES = "select p from OrderPojo p where " +
            "datetime between :startDate and :endDate";
    private static final String SELECT_AFTER_DATE = "select p from OrderPojo p where " +
            "datetime >= :startDate";
    private static final String SELECT_BEFORE_DATE = "select p from OrderPojo p where " +
            "datetime <= :endDate";


    public void insert(OrderPojo orderPojo){
        em().persist(orderPojo);
    }

    public OrderPojo selectId(int id){
        TypedQuery<OrderPojo> query = getQuery(SELECT_ID, OrderPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<OrderPojo> selectAll(){
        TypedQuery<OrderPojo> query = getQuery(SELECT_ALL, OrderPojo.class);
        return query.getResultList();
    }

    public List<OrderPojo> selectBeforeEndDate(LocalDateTime endDate) {
        TypedQuery<OrderPojo> query = getQuery(SELECT_BEFORE_DATE, OrderPojo.class);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    public List<OrderPojo> selectBetweenDates(LocalDateTime startDate,LocalDateTime endDate) {
        TypedQuery<OrderPojo> query = getQuery(SELECT_BETWEEN_DATES, OrderPojo.class);
        query.setParameter("startDate", startDate).setParameter("endDate", endDate);
        return query.getResultList();
    }

    public List<OrderPojo> selectAfterStartDate(LocalDateTime startDate) {
        TypedQuery<OrderPojo> query = getQuery(SELECT_AFTER_DATE, OrderPojo.class);
        query.setParameter("startDate", startDate);
        return query.getResultList();
    }

}

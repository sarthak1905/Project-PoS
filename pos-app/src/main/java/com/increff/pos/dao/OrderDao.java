package com.increff.pos.dao;

import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;


@Repository
@Transactional
public class OrderDao extends AbstractDao{

    private static final String SELECT_ID = "select p from OrderPojo p where id=:id";
    private static final String SELECT_ALL = "select p from OrderPojo p";
    private static final String SELECT_ALL_BETWEEN_DATES = "select p from OrderPojo p where " +
            "orderDate between :startDate and :endDate";
    private static final String SELECT_ALL_BY_STATUS = "select p from OrderPojo p where orderStatus=:orderStatus";
    private static final String SELECT_BETWEEN_DATES = "select p from OrderPojo p where " +
            "orderDate between :startDate and :endDate and orderStatus!=:orderStatus";
    private static final String SELECT_AFTER_DATE = "select p from OrderPojo p where " +
            "orderDate >= :startDate and orderStatus!=:orderStatus";
    private static final String SELECT_BEFORE_DATE = "select p from OrderPojo p where " +
            "orderDate <= :endDate and orderStatus!=:orderStatus";


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

    public List<OrderPojo> selectAllByStatus(String orderStatus){
        TypedQuery<OrderPojo> query = getQuery(SELECT_ALL_BY_STATUS, OrderPojo.class);
        query.setParameter("orderStatus", orderStatus);
        return query.getResultList();
    }

    public List<OrderPojo> selectBeforeEndDate(ZonedDateTime endDate) {
        TypedQuery<OrderPojo> query = getQuery(SELECT_BEFORE_DATE, OrderPojo.class);
        query.setParameter("endDate", endDate);
        query.setParameter("orderStatus", "cancelled");
        return query.getResultList();
    }

    public List<OrderPojo> selectBetweenDates(ZonedDateTime startDate,ZonedDateTime endDate) {
        TypedQuery<OrderPojo> query = getQuery(SELECT_BETWEEN_DATES, OrderPojo.class);
        query.setParameter("startDate", startDate).setParameter("endDate", endDate);
        query.setParameter("orderStatus", "cancelled");
        return query.getResultList();
    }

    public List<OrderPojo> selectAfterStartDate(ZonedDateTime startDate) {
        TypedQuery<OrderPojo> query = getQuery(SELECT_AFTER_DATE, OrderPojo.class);
        query.setParameter("startDate", startDate);
        query.setParameter("orderStatus", "cancelled");
        return query.getResultList();
    }

    public List<OrderPojo> selectAllBetweenDates(ZonedDateTime startDate, ZonedDateTime endDate) {
        TypedQuery<OrderPojo> query = getQuery(SELECT_ALL_BETWEEN_DATES, OrderPojo.class);
        query.setParameter("startDate", startDate).setParameter("endDate", endDate);
        return query.getResultList();
    }
}

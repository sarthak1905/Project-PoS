package com.increff.pos.dao;

import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class OrderItemDao extends AbstractDao{

    private static final String SELECT_ID = "select p from OrderItemPojo p where id=:id";
    private static final String SELECT_ALL = "select p from OrderItemPojo p";
    private static final String DELETE_ID = "delete from OrderItemPojo p where id=:id";
    private static final String SELECT_PRODUCT_ID_ORDER_ID = "select p from OrderItemPojo p where " +
                                                        "productId=:productId " +
                                                        "and orderId=:orderId";
    private static final String SELECT_BY_ORDER_ID = "select p from OrderItemPojo p where orderId=:orderId";

    public void insert(OrderItemPojo orderItemPojo){
        em().persist(orderItemPojo);
    }

    public OrderItemPojo select_id(int id){
        TypedQuery<OrderItemPojo> query = getQuery(SELECT_ID, OrderItemPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<OrderItemPojo> selectByOrderId(int orderId) {
        TypedQuery<OrderItemPojo> query = getQuery(SELECT_BY_ORDER_ID, OrderItemPojo.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }

    public List<OrderItemPojo> selectAll(){
        TypedQuery<OrderItemPojo> query = getQuery(SELECT_ALL, OrderItemPojo.class);
        return query.getResultList();
    }

    public OrderItemPojo selectProductIdOrderId(Integer productId, Integer orderId){
        TypedQuery<OrderItemPojo> query = getQuery(SELECT_PRODUCT_ID_ORDER_ID, OrderItemPojo.class);
        query.setParameter("productId", productId).setParameter("orderId", orderId);
        return getSingle(query);
    }

    public int delete(int id){
        Query query = em().createQuery(DELETE_ID);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

}

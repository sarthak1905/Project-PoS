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

    private static String select_id = "select p from OrderItemPojo p where id=:id";
    private static String select_all = "select p from OrderItemPojo p";
    private static String delete_id = "delete from OrderItemPojo p where id=:id";
    private static String select_productId_orderId = "select p from OrderItemPojo p where " +
                                                        "productId=:productId " +
                                                        "and orderId=:orderId";
    private static String select_by_orderId = "select p from OrderItemPojo p where orderId=:orderId";

    public void insert(OrderItemPojo p){
        em().persist(p);
    }

    public OrderItemPojo select_id(int id){
        TypedQuery<OrderItemPojo> query = getQuery(select_id, OrderItemPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<OrderItemPojo> selectByOrderId(int orderId) {
        TypedQuery<OrderItemPojo> query = getQuery(select_by_orderId, OrderItemPojo.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }

    public List<OrderItemPojo> selectAll(){
        TypedQuery<OrderItemPojo> query = getQuery(select_all, OrderItemPojo.class);
        return query.getResultList();
    }

    public OrderItemPojo selectProductIdOrderId(Integer productId, Integer orderId){
        TypedQuery<OrderItemPojo> query = getQuery(select_productId_orderId, OrderItemPojo.class);
        query.setParameter("productId", productId).setParameter("orderId", orderId);
        return getSingle(query);
    }

    public int delete(int id){
        Query query = em().createQuery(delete_id);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

}

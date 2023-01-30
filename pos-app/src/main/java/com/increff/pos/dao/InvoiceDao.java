package com.increff.pos.dao;

import com.increff.pos.pojo.InvoicePojo;
import com.increff.pos.pojo.InvoicePojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class InvoiceDao extends AbstractDao{

    private static String select_id = "select p from InvoicePojo p where order_id=:order_id";
    private static String select_all = "select p from InvoicePojo p";

    public void insert(InvoicePojo invoicePojo){
        em().persist(invoicePojo);
    }

    public InvoicePojo select_id(Integer orderId){
        TypedQuery<InvoicePojo> query = getQuery(select_id, InvoicePojo.class);
        query.setParameter("order_id", orderId);
        return getSingle(query);
    }

    public List<InvoicePojo> selectAll(){
        TypedQuery<InvoicePojo> query = getQuery(select_all, InvoicePojo.class);
        return query.getResultList();
    }

}

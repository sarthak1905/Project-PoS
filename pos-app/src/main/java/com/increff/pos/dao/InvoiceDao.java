package com.increff.pos.dao;

import com.increff.pos.pojo.InvoicePojo;
import com.increff.pos.pojo.InvoicePojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class InvoiceDao extends AbstractDao{

    private static String select_id = "select p from invoice p where id=:id";
    private static String select_all = "select b from InvoicePojo b";

    @Transactional
    public void insert(InvoicePojo invoicePojo){
        em().persist(invoicePojo);
    }

    public InvoicePojo select_id(int id){
        TypedQuery<InvoicePojo> query = getQuery(select_id, InvoicePojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<InvoicePojo> selectAll(){
        TypedQuery<InvoicePojo> query = getQuery(select_all, InvoicePojo.class);
        return query.getResultList();
    }

}

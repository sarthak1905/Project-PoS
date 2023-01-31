package com.increff.pos.dao;

import com.increff.pos.pojo.InvoicePojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public class InvoiceDao extends AbstractDao{

    private static String select_id = "select p from InvoicePojo p where order_id=:order_id";
    private static String select_all = "select p from InvoicePojo p";
    private static String select_between_dates = "select p from InvoicePojo p where invoice_date " +
            "between :start_date and :end_date";

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

    public List<InvoicePojo> selectInvoicedOrdersBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        TypedQuery<InvoicePojo> query = getQuery(select_between_dates, InvoicePojo.class);
        query.setParameter("start_date", startDate).setParameter("end_date", endDate);
        return query.getResultList();
    }
}

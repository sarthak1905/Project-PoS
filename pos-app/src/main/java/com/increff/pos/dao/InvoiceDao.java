package com.increff.pos.dao;

import com.increff.pos.pojo.InvoicePojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public class InvoiceDao extends AbstractDao{

    private static final String SELECT_ID = "select p from InvoicePojo p where order_id=:order_id";
    private static final String SELECT_ALL = "select p from InvoicePojo p";
    private static final String SELECT_BETWEEN_DATES = "select p from InvoicePojo p where invoice_date " +
            "between :start_date and :end_date";
    private static final String SELECT_FIRST_ORDER_DATE_TIME = "select MIN(p.invoiceDate) " +
            "from InvoicePojo p";

    public void insert(InvoicePojo invoicePojo){
        em().persist(invoicePojo);
    }

    public InvoicePojo select_id(Integer orderId){
        TypedQuery<InvoicePojo> query = getQuery(SELECT_ID, InvoicePojo.class);
        query.setParameter("order_id", orderId);
        return getSingle(query);
    }

    public List<InvoicePojo> selectAll(){
        TypedQuery<InvoicePojo> query = getQuery(SELECT_ALL, InvoicePojo.class);
        return query.getResultList();
    }

    public List<InvoicePojo> selectInvoicedOrdersBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        TypedQuery<InvoicePojo> query = getQuery(SELECT_BETWEEN_DATES, InvoicePojo.class);
        query.setParameter("start_date", startDate).setParameter("end_date", endDate);
        return query.getResultList();
    }

    public LocalDateTime selectFirstOrderDateTime() {
        Query query = em().createQuery(SELECT_FIRST_ORDER_DATE_TIME);
        return (LocalDateTime)query.getSingleResult();
    }
}

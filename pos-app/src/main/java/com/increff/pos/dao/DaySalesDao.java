package com.increff.pos.dao;

import com.increff.pos.pojo.DaySalesPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public class DaySalesDao extends AbstractDao {

    private static final String SELECT_ALL = "select p from DaySalesPojo p";
    private static final String SELECT_BY_DATE = "select p from DaySalesPojo p where date=:date";
    private static final String SELECT_BETWEEN_DATES = "select p from DaySalesPojo p where " +
            "date between :startDate and :endDate";
    private static final String SELECT_LATEST_DATE = "select max(p.date) from DaySalesPojo p";

    public void insert(DaySalesPojo daySalesPojo){
        em().persist(daySalesPojo);
    }

    public DaySalesPojo selectByDate(LocalDate date){
        TypedQuery<DaySalesPojo> query = getQuery(SELECT_BY_DATE, DaySalesPojo.class);
        query.setParameter("date", date);
        return getSingle(query);
    }

    public List<DaySalesPojo> selectAll() {
        TypedQuery<DaySalesPojo> query = getQuery(SELECT_ALL, DaySalesPojo.class);
        return query.getResultList();
    }

    public LocalDate selectLatestDate() {
        Query query = em().createQuery(SELECT_LATEST_DATE);
        return (LocalDate)query.getSingleResult();
    }

    public List<DaySalesPojo> selectBetweenDates(LocalDate startDate, LocalDate endDate) {
        TypedQuery<DaySalesPojo> query = getQuery(SELECT_BETWEEN_DATES, DaySalesPojo.class);
        query.setParameter("startDate", startDate).setParameter("endDate", endDate);
        return query.getResultList();
    }
}

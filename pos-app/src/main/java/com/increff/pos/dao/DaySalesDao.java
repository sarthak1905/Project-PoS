package com.increff.pos.dao;

import com.increff.pos.pojo.DaySalesPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public class DaySalesDao extends AbstractDao {

    private static String select_all = "select p from SchedulerPojo p";
    private static String select_by_date = "select p from SchedulerPojo p where date=:date";

    public void insert(DaySalesPojo daySalesPojo){
        em().persist(daySalesPojo);
    }

    public DaySalesPojo selectByDate(LocalDate date){
        TypedQuery<DaySalesPojo> query = getQuery(select_by_date, DaySalesPojo.class);
        query.setParameter("date", date);
        return getSingle(query);
    }

    public List<DaySalesPojo> selectAll() {
        TypedQuery<DaySalesPojo> query = getQuery(select_all, DaySalesPojo.class);
        return query.getResultList();
    }
}
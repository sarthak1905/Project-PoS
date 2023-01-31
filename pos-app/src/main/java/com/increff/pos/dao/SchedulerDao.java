package com.increff.pos.dao;

import com.increff.pos.model.SchedulerData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.SchedulerPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public class SchedulerDao extends AbstractDao {

    private static String select_all = "select p from SchedulerPojo p";
    private static String select_by_date = "select p from SchedulerPojo p where date=:date";

    public void insert(SchedulerPojo schedulerPojo){
        em().persist(schedulerPojo);
    }

    public SchedulerPojo selectByDate(LocalDate date){
        TypedQuery<SchedulerPojo> query = getQuery(select_by_date, SchedulerPojo.class);
        query.setParameter("date", date);
        return getSingle(query);
    }

    public List<SchedulerPojo> selectAll() {
        TypedQuery<SchedulerPojo> query = getQuery(select_all, SchedulerPojo.class);
        return query.getResultList();
    }
}

package com.increff.pos.flow;

import com.increff.pos.pojo.DaySalesPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.DaySalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class DaySalesFlow {

    @Autowired
    private DaySalesService daySalesService;

    public void createDaySalesEntry(LocalDate localDate) throws ApiException {
        daySalesService.createDaySalesEntry(localDate);
    }

    public List<DaySalesPojo> getBetweenDates(LocalDate startDate, LocalDate endDate) {
        return daySalesService.getBetweenDates(startDate, endDate);
    }

    public LocalDate getLastDate() {
        return daySalesService.getLastDate();
    }
}

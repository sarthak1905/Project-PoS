package com.increff.pos.scheduler;

import com.increff.pos.dto.DaySalesDto;
import com.increff.pos.service.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;

@EnableAsync
public class Scheduler {

    @Autowired
    DaySalesDto daySalesDto;

    @Async
    @Scheduled(cron = "1 0 0 * * *")
    public void createDaySalesReport() throws ApiException {
        daySalesDto.createDailyReport();
    }
}
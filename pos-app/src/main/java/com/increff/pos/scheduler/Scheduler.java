package com.increff.pos.scheduler;

import com.increff.pos.dto.SchedulerDto;
import com.increff.pos.service.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;

@EnableAsync
public class Scheduler {

    @Autowired
    SchedulerDto salesDto;

    @Async
    @Scheduled(cron = "0 1 12 * * *")
    public void createReport() throws ApiException {
        salesDto.createScheduler();
    }
}
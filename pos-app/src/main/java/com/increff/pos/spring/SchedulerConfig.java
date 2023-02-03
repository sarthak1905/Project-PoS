package com.increff.pos.spring;

import com.increff.pos.dto.ReportDto;
import com.increff.pos.service.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Autowired
    ReportDto reportDto;

    @Async
    @Scheduled(cron = "1 0 0 * * *")
    public void createDaySalesReport() throws ApiException {
        reportDto.createDailyReport();
    }

    @EventListener(ContextRefreshedEvent.class)
    public void refreshDaySalesEntity() throws ApiException {
        reportDto.refreshDaySalesEntity();
    }

}
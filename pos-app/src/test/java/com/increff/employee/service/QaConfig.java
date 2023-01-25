package com.increff.employee.service;

import com.increff.employee.spring.SpringConfig;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;

@Configuration
@ComponentScan(//
		basePackages = { "com.increff.employee" }, //
		excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, value = { SpringConfig.class })//
)
@PropertySources({ //
		@PropertySource(value = "classpath:./com/increff/employee/test.properties", ignoreResourceNotFound = true) //
})
public class QaConfig {


}

package com.increff.pos.util;

import com.increff.pos.service.ApiException;

import javax.validation.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

public class ValidationUtil {

    public static <T> void validateForms(T form){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(form);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public static <T> void checkPojo(T pojo) throws ApiException {
        if(pojo == null){
            throw new ApiException("Pojo is null!");
        }
    }

    public static void checkId(Integer id) throws ApiException {
        if(id == null) {
            throw new ApiException("ID provided is null!");
        }
    }

    public static void validateDaySalesDates(LocalDate startDate, LocalDate endDate) throws ApiException {
        if(startDate.isAfter(endDate)){
            throw new ApiException("Start date cannot be after end date!");
        }
        if(ChronoUnit.DAYS.between(startDate, endDate) > 90){
            throw new ApiException("Max duration between start date and end date is 90 days");
        }
    }

    public static boolean checkIfNullOrEmpty(String inputString) {
        return inputString == null || inputString.equals("");
    }

}

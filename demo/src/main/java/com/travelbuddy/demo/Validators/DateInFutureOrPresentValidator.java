package com.travelbuddy.demo.Validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateInFutureOrPresentValidator implements ConstraintValidator<DateInFutureOrPresent, String> {

    @Override
    public void initialize(DateInFutureOrPresent constraintAnnotation) {
    }

    @Override
    public boolean isValid(String dateStr, ConstraintValidatorContext constraintValidatorContext) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            return !date.isBefore(LocalDate.now());
        } catch (Exception e) {
            return false;
        }
    }
}
package com.travelbuddy.demo.Validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Slf4j
public class DateInFutureOrPresentValidator implements ConstraintValidator<DateInFutureOrPresent, String> {

    @Override
    public void initialize(DateInFutureOrPresent constraintAnnotation) {
    }

    @Override
    public boolean isValid(String dateStr, ConstraintValidatorContext constraintValidatorContext) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            boolean isValid = date.isAfter(LocalDate.now()) || date.isEqual(LocalDate.now());
            log.info("Is Date in Future or Present: {}", isValid);
            return isValid;
        } catch (DateTimeParseException e) {
            log.error("Failed to parse date string: " + dateStr, e);
            return false;
        } catch (Exception e) {
            log.error("Unknown error occurred while validating date: " + dateStr, e);
            return false;
        }
    }
}


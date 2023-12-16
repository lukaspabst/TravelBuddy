package com.travelbuddy.demo.Validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateInFutureOrPresentValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DateInFutureOrPresent {
    String message() default "Date should be present or future date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

package com.dattran.ecommerceapp.custom;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    String message() default "Password must contain character, number, and special symbol";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

package com.example.mutants.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValidDnaSequenceValidator.class)
public @interface ValidDnaSequence {

    String message() default "Secuencia de ADN inválida";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

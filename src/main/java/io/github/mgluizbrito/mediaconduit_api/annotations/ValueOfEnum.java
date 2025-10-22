package io.github.mgluizbrito.mediaconduit_api.annotations;

import io.github.mgluizbrito.mediaconduit_api.validator.ValueOfEnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValueOfEnumValidator.class)
public @interface ValueOfEnum {

    Class<? extends Enum<?>> enumClass();

    String message() default "The value must be one of: {allowedValues}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

package io.github.mgluizbrito.mediaconduit_api.validator;

import io.github.mgluizbrito.mediaconduit_api.annotations.ValueOfEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, String> {

    private Class<? extends Enum<?>> enumClass;
    private String allowedValues;

    @Override
    public void initialize(ValueOfEnum constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();

        this.enumClass = constraintAnnotation.enumClass();

        this.allowedValues = Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;

        boolean isValid = Arrays.stream(enumClass.getEnumConstants())
                .anyMatch(e -> e.name().equalsIgnoreCase(value));

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    context.getDefaultConstraintMessageTemplate().replace("{allowedValues}", this.allowedValues)
            ).addConstraintViolation();
        }

        return isValid;
    }
}

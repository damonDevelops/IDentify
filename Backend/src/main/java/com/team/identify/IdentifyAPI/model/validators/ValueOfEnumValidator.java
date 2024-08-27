package com.team.identify.IdentifyAPI.model.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, Object> {
    private List<String> acceptedValues;

    @Override
    public void initialize(ValueOfEnum annotation) {
        acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (null == value) {
            return true;
        }

        if (value instanceof Collection<?>) {
            boolean result = false;
            for (Object entry : (Collection<?>) value) {
                if (entry instanceof CharSequence) {
                    result = acceptedValues.contains(entry.toString());
                } else {
                    return false;
                }
                return result;
            }
        } else if (value instanceof String) {
            return acceptedValues.contains(value.toString());
        }
        return false;
    }
}
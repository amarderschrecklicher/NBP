package ba.unsa.etf.employeemanagement.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, String> {

    private Set<String> allowedValues;

    @Override
    public void initialize(ValueOfEnum annotation) {
        allowedValues = Arrays.stream(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null is valid; use @NotNull/@NotBlank separately
        }

        boolean valid = allowedValues.contains(value.toUpperCase());

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "must be one of the allowed values: " + allowedValues
            ).addConstraintViolation();
        }

        return valid;
    }
}


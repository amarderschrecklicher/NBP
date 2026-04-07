package ba.unsa.etf.employeemanagement.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Class-level constraint that validates a date range between two Date fields.
 * Ensures that the "before" date field is not after the "after" date field
 * (e.g. hireDate must be before or equal to terminationDate).
 */
@Documented
@Constraint(validatedBy = DateRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ValidDateRange.List.class)
public @interface ValidDateRange {

    String message() default "Start date must be before or equal to end date";

    String startDateField();

    String endDateField();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        ValidDateRange[] value();
    }
}


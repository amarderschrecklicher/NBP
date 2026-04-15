package ba.unsa.etf.employeemanagement.util.validation;

import ba.unsa.etf.employeemanagement.dto.request.DepartmentRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class DepartmentValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return DepartmentRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DepartmentRequest request = (DepartmentRequest) target;

        // Validate name
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "name", "name.empty", "Department name is required"
        );
        if (request.getName() != null) {
            if (request.getName().length() < 2) {
                errors.rejectValue("name", "name.tooShort",
                        "Department name must be at least 2 characters");
            }
            if (request.getName().length() > 100) {
                errors.rejectValue("name", "name.tooLong",
                        "Department name must not exceed 100 characters");
            }
        }

        // Validate description
        if (request.getDescription() != null && request.getDescription().length() > 500) {
            errors.rejectValue("description", "description.tooLong",
                    "Description must not exceed 500 characters");
        }
    }
}


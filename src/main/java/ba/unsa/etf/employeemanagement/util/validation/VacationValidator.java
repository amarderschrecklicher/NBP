package ba.unsa.etf.employeemanagement.util.validation;

import ba.unsa.etf.employeemanagement.dto.request.VacationRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class VacationValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return VacationRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        VacationRequest request = (VacationRequest) target;

        if (request.getEmployeeId() == null) {
            errors.rejectValue("employeeId", "employeeId.empty", "Employee ID is required");
        }

        if (request.getStartDate() == null) {
            errors.rejectValue("startDate", "startDate.empty", "Start date is required");
        }

        if (request.getEndDate() == null) {
            errors.rejectValue("endDate", "endDate.empty", "End date is required");
        }

        if (request.getStartDate() != null && request.getEndDate() != null) {
            if (request.getEndDate().before(request.getStartDate())) {
                errors.rejectValue("endDate", "endDate.invalid", "End date cannot be before start date");
            }
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "vacationType", "vacationType.empty", "Vacation type is required"
        );

        if (request.getReason() != null && request.getReason().length() > 500) {
            errors.rejectValue("reason", "reason.tooLong", "Reason must not exceed 500 characters");
        }
    }
}

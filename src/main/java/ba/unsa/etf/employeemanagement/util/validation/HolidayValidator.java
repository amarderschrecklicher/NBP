package ba.unsa.etf.employeemanagement.util.validation;

import ba.unsa.etf.employeemanagement.dto.request.HolidayRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class HolidayValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return HolidayRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HolidayRequest request = (HolidayRequest) target;

        // Validate holidayName
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "holidayName", "holidayName.empty", "Holiday name is required"
        );
        if (request.getHolidayName() != null) {
            if (request.getHolidayName().length() < 2) {
                errors.rejectValue("holidayName", "holidayName.tooShort",
                        "Holiday name must be at least 2 characters");
            }
            if (request.getHolidayName().length() > 100) {
                errors.rejectValue("holidayName", "holidayName.tooLong",
                        "Holiday name must not exceed 100 characters");
            }
        }

        // Validate holidayDate
        if (request.getHolidayDate() == null) {
            errors.rejectValue("holidayDate", "holidayDate.empty", "Holiday date is required");
        }

        // Validate country
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "country", "country.empty", "Country is required"
        );
        if (request.getCountry() != null) {
            if (request.getCountry().length() < 2) {
                errors.rejectValue("country", "country.tooShort",
                        "Country must be at least 2 characters");
            }
            if (request.getCountry().length() > 100) {
                errors.rejectValue("country", "country.tooLong",
                        "Country must not exceed 100 characters");
            }
        }

        // Validate description
        if (request.getDescription() != null && request.getDescription().length() > 500) {
            errors.rejectValue("description", "description.tooLong",
                    "Description must not exceed 500 characters");
        }
    }
}


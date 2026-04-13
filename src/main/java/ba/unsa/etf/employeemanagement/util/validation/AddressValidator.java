package ba.unsa.etf.employeemanagement.util.validation;

import ba.unsa.etf.employeemanagement.dto.address.AddressDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AddressValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return AddressDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AddressDto address = (AddressDto) target;

        // Validate street
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "street", "street.empty", "Street is required"
        );
        if (address.getStreet() != null) {
            if (address.getStreet().length() < 2) {
                errors.rejectValue("street", "street.tooShort",
                        "Street must be at least 2 characters");
            }
            if (address.getStreet().length() > 255) {
                errors.rejectValue("street", "street.tooLong",
                        "Street must not exceed 255 characters");
            }
        }

        // Validate city
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "city", "city.empty", "City is required"
        );
        if (address.getCity() != null) {
            if (address.getCity().length() < 2) {
                errors.rejectValue("city", "city.tooShort",
                        "City must be at least 2 characters");
            }
            if (address.getCity().length() > 255) {
                errors.rejectValue("city", "city.tooLong",
                        "City must not exceed 255 characters");
            }
            if (!address.getCity().matches("^[a-zA-Z\\s\\-]+$")) {
                errors.rejectValue("city", "city.invalid",
                        "City must contain only letters, spaces or hyphens");
            }
        }

        // Validate postal code
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "postalCode", "postalCode.empty", "Postal code is required"
        );
        if (address.getPostalCode() != null) {
            if (!address.getPostalCode().matches("^[0-9]{5}$")) {
                errors.rejectValue("postalCode", "postalCode.invalid",
                        "Postal code must be exactly 5 digits");
            }
        }

        // Validate country
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "country", "country.empty", "Country is required"
        );
        if (address.getCountry() != null) {
            if (address.getCountry().length() < 2) {
                errors.rejectValue("country", "country.tooShort",
                        "Country must be at least 2 characters");
            }
            if (address.getCountry().length() > 100) {
                errors.rejectValue("country", "country.tooLong",
                        "Country must not exceed 100 characters");
            }
            if (!address.getCountry().matches("^[a-zA-Z\\s\\-]+$")) {
                errors.rejectValue("country", "country.invalid",
                        "Country must contain only letters, spaces or hyphens");
            }
        }
    }
}


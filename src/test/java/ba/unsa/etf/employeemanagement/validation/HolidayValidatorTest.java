package ba.unsa.etf.employeemanagement.validation;

import ba.unsa.etf.employeemanagement.dto.request.HolidayRequest;
import ba.unsa.etf.employeemanagement.util.validation.HolidayValidator;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class HolidayValidatorTest {

    private final HolidayValidator validator = new HolidayValidator();

    @Test
    void supports_holidayRequestClass() {
        assertTrue(validator.supports(HolidayRequest.class));
        assertFalse(validator.supports(Integer.class));
    }

    @Test
    void validate_validRequest_hasNoErrors() {
        HolidayRequest request = new HolidayRequest("Statehood Day", new Date(1732752000000L), "BA", "Public holiday", null);
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(request, "holidayRequest");

        validator.validate(request, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    void validate_missingDate_rejectsHolidayDateField() {
        HolidayRequest request = new HolidayRequest("Statehood Day", null, "BA", "Public holiday", null);
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(request, "holidayRequest");

        validator.validate(request, errors);

        assertTrue(errors.hasFieldErrors("holidayDate"));
        assertEquals("holidayDate.empty", errors.getFieldError("holidayDate").getCode());
    }

    @Test
    void validate_blankCountry_rejectsCountryField() {
        HolidayRequest request = new HolidayRequest("Statehood Day", new Date(1732752000000L), " ", "Public holiday", null);
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(request, "holidayRequest");

        validator.validate(request, errors);

        assertTrue(errors.hasFieldErrors("country"));
        assertEquals("country.empty", errors.getFieldError("country").getCode());
    }

    @Test
    void validate_tooLongHolidayName_rejectsHolidayNameField() {
        HolidayRequest request = new HolidayRequest("x".repeat(101), new Date(1732752000000L), "BA", "Public holiday", null);
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(request, "holidayRequest");

        validator.validate(request, errors);

        assertTrue(errors.hasFieldErrors("holidayName"));
        assertEquals("holidayName.tooLong", errors.getFieldError("holidayName").getCode());
    }
}


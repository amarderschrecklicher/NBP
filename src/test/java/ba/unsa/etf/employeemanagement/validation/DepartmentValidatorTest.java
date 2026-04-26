package ba.unsa.etf.employeemanagement.validation;

import ba.unsa.etf.employeemanagement.dto.request.DepartmentRequest;
import ba.unsa.etf.employeemanagement.util.validation.DepartmentValidator;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentValidatorTest {

    private final DepartmentValidator validator = new DepartmentValidator();

    @Test
    void supports_departmentRequestClass() {
        assertTrue(validator.supports(DepartmentRequest.class));
        assertFalse(validator.supports(String.class));
    }

    @Test
    void validate_validRequest_hasNoErrors() {
        DepartmentRequest request = new DepartmentRequest("Finance", "Budget and planning");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(request, "departmentRequest");

        validator.validate(request, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    void validate_emptyName_rejectsNameField() {
        DepartmentRequest request = new DepartmentRequest("   ", "Any");
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(request, "departmentRequest");

        validator.validate(request, errors);

        assertTrue(errors.hasFieldErrors("name"));
        assertEquals("name.empty", errors.getFieldError("name").getCode());
    }

    @Test
    void validate_tooLongDescription_rejectsDescriptionField() {
        String tooLongDescription = "x".repeat(501);
        DepartmentRequest request = new DepartmentRequest("Finance", tooLongDescription);
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(request, "departmentRequest");

        validator.validate(request, errors);

        assertTrue(errors.hasFieldErrors("description"));
        assertEquals("description.tooLong", errors.getFieldError("description").getCode());
    }
}


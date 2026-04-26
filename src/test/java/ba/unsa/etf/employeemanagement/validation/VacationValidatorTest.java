package ba.unsa.etf.employeemanagement.validation;

import ba.unsa.etf.employeemanagement.dto.request.VacationRequest;
import ba.unsa.etf.employeemanagement.util.validation.VacationValidator;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VacationValidatorTest {

	private final VacationValidator validator = new VacationValidator();

	@Test
	void supports_vacationRequestClass() {
		assertTrue(validator.supports(VacationRequest.class));
		assertFalse(validator.supports(Long.class));
	}

	@Test
	void validate_validRequest_hasNoErrors() {
		VacationRequest request = new VacationRequest(1L, new Date(1704067200000L), new Date(1704153600000L), "ANNUAL", "Family travel");
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(request, "vacationRequest");

		validator.validate(request, errors);

		assertFalse(errors.hasErrors());
	}

	@Test
	void validate_missingEmployeeId_rejectsEmployeeIdField() {
		VacationRequest request = new VacationRequest(null, new Date(1704067200000L), new Date(1704153600000L), "ANNUAL", "Family travel");
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(request, "vacationRequest");

		validator.validate(request, errors);

		assertTrue(errors.hasFieldErrors("employeeId"));
		assertEquals("employeeId.empty", errors.getFieldError("employeeId").getCode());
	}

	@Test
	void validate_endDateBeforeStartDate_rejectsEndDateField() {
		VacationRequest request = new VacationRequest(1L, new Date(1704153600000L), new Date(1704067200000L), "ANNUAL", "Family travel");
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(request, "vacationRequest");

		validator.validate(request, errors);

		assertTrue(errors.hasFieldErrors("endDate"));
		assertEquals("endDate.invalid", errors.getFieldError("endDate").getCode());
	}

	@Test
	void validate_blankVacationType_rejectsVacationTypeField() {
		VacationRequest request = new VacationRequest(1L, new Date(1704067200000L), new Date(1704153600000L), " ", "Family travel");
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(request, "vacationRequest");

		validator.validate(request, errors);

		assertTrue(errors.hasFieldErrors("vacationType"));
		assertEquals("vacationType.empty", errors.getFieldError("vacationType").getCode());
	}
}


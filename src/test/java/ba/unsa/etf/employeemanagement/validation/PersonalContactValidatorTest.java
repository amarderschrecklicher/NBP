package ba.unsa.etf.employeemanagement.validation;

import ba.unsa.etf.employeemanagement.model.PersonalContact;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonalContactValidatorTest {

    @Test
    void validate_WithValidData_DoesNotThrow() {
        PersonalContact contact = new PersonalContact(1L, 1L, "+387 61 123 456", "test@example.com");
        assertDoesNotThrow(() -> PersonalContactValidator.validate(contact));
    }

    @Test
    void validate_WithNull_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> PersonalContactValidator.validate(null));
    }

    @Test
    void validate_WithNullEmployeeId_ThrowsIllegalArgumentException() {
        PersonalContact contact = new PersonalContact(1L, null, "+387 61 123 456", "test@example.com");
        assertThrows(IllegalArgumentException.class, () -> PersonalContactValidator.validate(contact));
    }

    @Test
    void validate_WithEmptyPhoneNumber_ThrowsIllegalArgumentException() {
        PersonalContact contact = new PersonalContact(1L, 1L, "", "test@example.com");
        assertThrows(IllegalArgumentException.class, () -> PersonalContactValidator.validate(contact));
    }

    @Test
    void validate_WithInvalidPhoneNumber_ThrowsIllegalArgumentException() {
        PersonalContact contact = new PersonalContact(1L, 1L, "invalid-phone", "test@example.com");
        assertThrows(IllegalArgumentException.class, () -> PersonalContactValidator.validate(contact));
    }

    @Test
    void validate_WithEmptyEmail_ThrowsIllegalArgumentException() {
        PersonalContact contact = new PersonalContact(1L, 1L, "+387 61 123 456", "");
        assertThrows(IllegalArgumentException.class, () -> PersonalContactValidator.validate(contact));
    }

    @Test
    void validate_WithInvalidEmail_ThrowsIllegalArgumentException() {
        PersonalContact contact = new PersonalContact(1L, 1L, "+387 61 123 456", "invalid-email");
        assertThrows(IllegalArgumentException.class, () -> PersonalContactValidator.validate(contact));
    }

    @Test
    void validate_WithLongEmail_ThrowsIllegalArgumentException() {
        String longEmail = "a".repeat(92) + "@test.com"; // 101 chars
        PersonalContact contact = new PersonalContact(1L, 1L, "+387 61 123 456", longEmail);
        assertThrows(IllegalArgumentException.class, () -> PersonalContactValidator.validate(contact));
    }
}

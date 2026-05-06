package ba.unsa.etf.employeemanagement.validation;

import ba.unsa.etf.employeemanagement.model.EmergencyContact;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmergencyContactValidatorTest {

    @Test
    void validate_WithValidContact_DoesNotThrowException() {
        EmergencyContact contact = new EmergencyContact(1L, 1L, "John", "Doe", "Brother", "123456789", "john.doe@example.com", "Main St");
        assertDoesNotThrow(() -> EmergencyContactValidator.validate(contact));
    }

    @Test
    void validate_WithNullContact_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> EmergencyContactValidator.validate(null));
    }

    @Test
    void validate_WithMissingEmployeeId_ThrowsException() {
        EmergencyContact contact = new EmergencyContact(1L, null, "John", "Doe", "Brother", "123456789", "john.doe@example.com", "Main St");
        assertThrows(IllegalArgumentException.class, () -> EmergencyContactValidator.validate(contact));
    }

    @Test
    void validate_WithEmptyFirstName_ThrowsException() {
        EmergencyContact contact = new EmergencyContact(1L, 1L, "", "Doe", "Brother", "123456789", "john.doe@example.com", "Main St");
        assertThrows(IllegalArgumentException.class, () -> EmergencyContactValidator.validate(contact));
    }

    @Test
    void validate_WithInvalidEmail_ThrowsException() {
        EmergencyContact contact = new EmergencyContact(1L, 1L, "John", "Doe", "Brother", "123456789", "invalid-email", "Main St");
        assertThrows(IllegalArgumentException.class, () -> EmergencyContactValidator.validate(contact));
    }
}

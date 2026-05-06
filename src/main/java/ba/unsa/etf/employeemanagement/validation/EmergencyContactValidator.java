package ba.unsa.etf.employeemanagement.validation;

import ba.unsa.etf.employeemanagement.model.EmergencyContact;

public class EmergencyContactValidator {

    public static void validate(EmergencyContact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Emergency contact cannot be null");
        }
        if (contact.getEmployeeId() == null) {
            throw new IllegalArgumentException("Employee ID is required");
        }
        if (contact.getFirstName() == null || contact.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (contact.getLastName() == null || contact.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (contact.getRelationship() == null || contact.getRelationship().trim().isEmpty()) {
            throw new IllegalArgumentException("Relationship is required");
        }
        if (contact.getPhoneNumber() == null || contact.getPhoneNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required");
        }
        if (contact.getEmail() != null && !contact.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
}

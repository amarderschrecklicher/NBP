package ba.unsa.etf.employeemanagement.validation;

import ba.unsa.etf.employeemanagement.model.Employee;
import java.util.Arrays;
import java.util.List;

public class EmployeeValidator {
    private static final List<String> VALID_GENDERS = Arrays.asList("Male", "Female", "Other");
    private static final List<String> VALID_MARITAL_STATUSES = Arrays.asList("Single", "Married", "Divorced", "Widowed");

    public static void validate(Employee employee) {
        if (employee == null) throw new IllegalArgumentException("Employee cannot be null");
        if (employee.getUserId() == null) throw new IllegalArgumentException("User ID is required");
        if (employee.getGender() == null || !VALID_GENDERS.contains(employee.getGender())) {
            throw new IllegalArgumentException("Invalid gender");
        }
        if (employee.getNationality() == null || employee.getNationality().trim().isEmpty()) {
            throw new IllegalArgumentException("Nationality is required");
        }
        if (employee.getMaritalStatus() == null || !VALID_MARITAL_STATUSES.contains(employee.getMaritalStatus())) {
            throw new IllegalArgumentException("Invalid marital status");
        }
    }
}


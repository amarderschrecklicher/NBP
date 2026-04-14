package ba.unsa.etf.employeemanagement.validation;

import ba.unsa.etf.employeemanagement.model.Employment;
import java.util.Date;

public class EmploymentValidator {
    public static void validate(Employment employment) {
        if (employment == null) throw new IllegalArgumentException("Employment cannot be null");
        if (employment.getEmployeeId() == null) throw new IllegalArgumentException("Employee ID is required");
        if (employment.getNumber() == null || employment.getNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Employment number is required");
        }
        if (employment.getHireDate() == null) throw new IllegalArgumentException("Hire date is required");
        if (employment.getTerminationDate() != null && employment.getHireDate().after(employment.getTerminationDate())) {
            throw new IllegalArgumentException("Hire date must be before termination date");
        }
        if (employment.getJobTitle() == null || employment.getJobTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Job title is required");
        }
        if (employment.getEmploymentType() == null || employment.getEmploymentType().trim().isEmpty()) {
            throw new IllegalArgumentException("Employment type is required");
        }
        if (employment.getStatus() == null || employment.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Status is required");
        }
        if (employment.getDepartmentId() == null) throw new IllegalArgumentException("Department ID is required");
    }
}


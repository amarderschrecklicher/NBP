package ba.unsa.etf.employeemanagement.validation;

import ba.unsa.etf.employeemanagement.model.Disability;
import ba.unsa.etf.employeemanagement.util.enums.DisabilityLevel;
import ba.unsa.etf.employeemanagement.util.enums.DisabilityType;

import java.util.Arrays;
import java.util.stream.Collectors;

public class DisabilityValidator {

    public static void validate(Disability disability) {
        if (disability == null) {
            throw new IllegalArgumentException("Disability cannot be null");
        }
        if (disability.getEmployeeId() == null) {
            throw new IllegalArgumentException("Employee ID is required");
        }
        if (disability.getDisabilityType() == null || !isValidDisabilityType(disability.getDisabilityType())) {
            throw new IllegalArgumentException("Invalid disability type. Must be one of: " + 
                Arrays.stream(DisabilityType.values()).map(Enum::name).collect(Collectors.joining(", ")));
        }
        if (disability.getDisabilityLevel() == null || !isValidDisabilityLevel(disability.getDisabilityLevel())) {
            throw new IllegalArgumentException("Invalid disability level. Must be one of: " + 
                Arrays.stream(DisabilityLevel.values()).map(Enum::name).collect(Collectors.joining(", ")));
        }
        if (disability.getRegisteredDate() == null) {
            throw new IllegalArgumentException("Registered date is required");
        }
        if (disability.getRegisteredDate().after(new java.util.Date())) {
            throw new IllegalArgumentException("Registered date cannot be in the future");
        }
    }

    private static boolean isValidDisabilityType(String type) {
        try {
            DisabilityType.valueOf(type);
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }

    private static boolean isValidDisabilityLevel(String level) {
        try {
            DisabilityLevel.valueOf(level);
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }
}

package ba.unsa.etf.employeemanagement.validation;

import ba.unsa.etf.employeemanagement.model.WorkPermit;
import ba.unsa.etf.employeemanagement.util.enums.WorkPermitStatus;
import ba.unsa.etf.employeemanagement.util.enums.WorkPermitType;

import java.util.Arrays;
import java.util.stream.Collectors;

public class WorkPermitValidator {

    public static void validate(WorkPermit workPermit) {
        if (workPermit == null) {
            throw new IllegalArgumentException("Work permit cannot be null");
        }
        if (workPermit.getEmployeeId() == null) {
            throw new IllegalArgumentException("Employee ID is required");
        }
        if (workPermit.getPermitNumber() == null || workPermit.getPermitNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Permit number is required");
        }
        if (workPermit.getPermitType() == null || !isValidPermitType(workPermit.getPermitType())) {
            throw new IllegalArgumentException("Invalid permit type. Must be one of: " + 
                Arrays.stream(WorkPermitType.values()).map(Enum::name).collect(Collectors.joining(", ")));
        }
        if (workPermit.getIssuingCountry() == null || workPermit.getIssuingCountry().trim().isEmpty()) {
            throw new IllegalArgumentException("Issuing country is required");
        }
        if (workPermit.getIssueDate() == null) {
            throw new IllegalArgumentException("Issue date is required");
        }
        if (workPermit.getExpiryDate() == null) {
            throw new IllegalArgumentException("Expiry date is required");
        }
        if (workPermit.getExpiryDate().before(workPermit.getIssueDate())) {
            throw new IllegalArgumentException("Expiry date must be after issue date");
        }
        if (workPermit.getStatus() == null || !isValidStatus(workPermit.getStatus())) {
            throw new IllegalArgumentException("Invalid status. Must be one of: " + 
                Arrays.stream(WorkPermitStatus.values()).map(Enum::name).collect(Collectors.joining(", ")));
        }
    }

    private static boolean isValidPermitType(String type) {
        try {
            WorkPermitType.valueOf(type);
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }

    private static boolean isValidStatus(String status) {
        try {
            WorkPermitStatus.valueOf(status);
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }
}

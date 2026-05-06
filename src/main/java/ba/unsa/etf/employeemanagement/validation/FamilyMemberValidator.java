package ba.unsa.etf.employeemanagement.validation;

import ba.unsa.etf.employeemanagement.model.FamilyMember;
import ba.unsa.etf.employeemanagement.util.enums.FamilyRelation;

import java.util.Arrays;
import java.util.Date;

public class FamilyMemberValidator {

    public static void validate(FamilyMember familyMember) {
        if (familyMember == null) {
            throw new IllegalArgumentException("Family member cannot be null");
        }
        if (familyMember.getEmployeeId() == null || familyMember.getEmployeeId() <= 0) {
            throw new IllegalArgumentException("Employee ID must be a positive number");
        }
        if (familyMember.getFirstName() == null || familyMember.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (familyMember.getLastName() == null || familyMember.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (familyMember.getRelation() == null || !isValidRelation(familyMember.getRelation())) {
            throw new IllegalArgumentException("Invalid family relation");
        }
        if (familyMember.getDateOfBirth() == null || familyMember.getDateOfBirth().after(new Date())) {
            throw new IllegalArgumentException("Date of birth is required and must be in the past");
        }
    }

    private static boolean isValidRelation(String relation) {
        return Arrays.stream(FamilyRelation.values())
                .anyMatch(r -> r.name().equals(relation));
    }
}

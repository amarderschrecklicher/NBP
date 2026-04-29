package ba.unsa.etf.employeemanagement.validation;

import ba.unsa.etf.employeemanagement.model.FamilyMember;
import ba.unsa.etf.employeemanagement.util.enums.FamilyRelation;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FamilyMemberValidatorTest {

    @Test
    void validate_WithValidData_ShouldNotThrowException() {
        FamilyMember familyMember = new FamilyMember(1L, 1L, "John", "Doe", FamilyRelation.CHILD.name(), new Date(System.currentTimeMillis() - 1000000), 1, "Student");
        assertDoesNotThrow(() -> FamilyMemberValidator.validate(familyMember));
    }

    @Test
    void validate_WithNullFamilyMember_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> FamilyMemberValidator.validate(null));
    }

    @Test
    void validate_WithInvalidEmployeeId_ShouldThrowException() {
        FamilyMember familyMember = new FamilyMember(1L, null, "John", "Doe", FamilyRelation.CHILD.name(), new Date(), 1, "Student");
        assertThrows(IllegalArgumentException.class, () -> FamilyMemberValidator.validate(familyMember));

        familyMember.setEmployeeId(-1L);
        assertThrows(IllegalArgumentException.class, () -> FamilyMemberValidator.validate(familyMember));
    }

    @Test
    void validate_WithMissingFirstName_ShouldThrowException() {
        FamilyMember familyMember = new FamilyMember(1L, 1L, "", "Doe", FamilyRelation.CHILD.name(), new Date(), 1, "Student");
        assertThrows(IllegalArgumentException.class, () -> FamilyMemberValidator.validate(familyMember));
    }

    @Test
    void validate_WithInvalidRelation_ShouldThrowException() {
        FamilyMember familyMember = new FamilyMember(1L, 1L, "John", "Doe", "INVALID", new Date(), 1, "Student");
        assertThrows(IllegalArgumentException.class, () -> FamilyMemberValidator.validate(familyMember));
    }

    @Test
    void validate_WithFutureBirthDate_ShouldThrowException() {
        FamilyMember familyMember = new FamilyMember(1L, 1L, "John", "Doe", FamilyRelation.CHILD.name(), new Date(System.currentTimeMillis() + 1000000), 1, "Student");
        assertThrows(IllegalArgumentException.class, () -> FamilyMemberValidator.validate(familyMember));
    }
}

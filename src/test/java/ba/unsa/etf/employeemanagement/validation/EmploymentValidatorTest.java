package ba.unsa.etf.employeemanagement.validation;

import ba.unsa.etf.employeemanagement.model.Employment;
import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class EmploymentValidatorTest {
    @Test
    void validEmployment_passesValidation() {
        Employment e = new Employment(1L, 2L, "EMP123", new Date(100000), null, "Engineer", "Full-time", "Active", 3L);
        assertDoesNotThrow(() -> EmploymentValidator.validate(e));
    }

    @Test
    void nullEmployment_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> EmploymentValidator.validate(null));
    }

    @Test
    void missingEmployeeId_throwsException() {
        Employment e = new Employment(1L, null, "EMP123", new Date(), null, "Engineer", "Full-time", "Active", 3L);
        assertThrows(IllegalArgumentException.class, () -> EmploymentValidator.validate(e));
    }

    @Test
    void emptyNumber_throwsException() {
        Employment e = new Employment(1L, 2L, "", new Date(), null, "Engineer", "Full-time", "Active", 3L);
        assertThrows(IllegalArgumentException.class, () -> EmploymentValidator.validate(e));
    }

    @Test
    void nullHireDate_throwsException() {
        Employment e = new Employment(1L, 2L, "EMP123", null, null, "Engineer", "Full-time", "Active", 3L);
        assertThrows(IllegalArgumentException.class, () -> EmploymentValidator.validate(e));
    }

    @Test
    void hireDateAfterTerminationDate_throwsException() {
        Employment e = new Employment(1L, 2L, "EMP123", new Date(200000), new Date(100000), "Engineer", "Full-time", "Active", 3L);
        assertThrows(IllegalArgumentException.class, () -> EmploymentValidator.validate(e));
    }

    @Test
    void emptyJobTitle_throwsException() {
        Employment e = new Employment(1L, 2L, "EMP123", new Date(), null, "", "Full-time", "Active", 3L);
        assertThrows(IllegalArgumentException.class, () -> EmploymentValidator.validate(e));
    }

    @Test
    void emptyEmploymentType_throwsException() {
        Employment e = new Employment(1L, 2L, "EMP123", new Date(), null, "Engineer", "", "Active", 3L);
        assertThrows(IllegalArgumentException.class, () -> EmploymentValidator.validate(e));
    }

    @Test
    void emptyStatus_throwsException() {
        Employment e = new Employment(1L, 2L, "EMP123", new Date(), null, "Engineer", "Full-time", "", 3L);
        assertThrows(IllegalArgumentException.class, () -> EmploymentValidator.validate(e));
    }

    @Test
    void missingDepartmentId_throwsException() {
        Employment e = new Employment(1L, 2L, "EMP123", new Date(), null, "Engineer", "Full-time", "Active", null);
        assertThrows(IllegalArgumentException.class, () -> EmploymentValidator.validate(e));
    }
}


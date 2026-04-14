package ba.unsa.etf.employeemanagement.validation;

import ba.unsa.etf.employeemanagement.model.Employee;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeeValidatorTest {
    @Test
    void validEmployee_passesValidation() {
        Employee e = new Employee(1L, 2L, "Male", "Bosnian", "Single");
        assertDoesNotThrow(() -> EmployeeValidator.validate(e));
    }

    @Test
    void nullEmployee_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> EmployeeValidator.validate(null));
    }

    @Test
    void missingUserId_throwsException() {
        Employee e = new Employee(1L, null, "Male", "Bosnian", "Single");
        assertThrows(IllegalArgumentException.class, () -> EmployeeValidator.validate(e));
    }

    @Test
    void invalidGender_throwsException() {
        Employee e = new Employee(1L, 2L, "Unknown", "Bosnian", "Single");
        assertThrows(IllegalArgumentException.class, () -> EmployeeValidator.validate(e));
    }

    @Test
    void emptyNationality_throwsException() {
        Employee e = new Employee(1L, 2L, "Male", "", "Single");
        assertThrows(IllegalArgumentException.class, () -> EmployeeValidator.validate(e));
    }

    @Test
    void invalidMaritalStatus_throwsException() {
        Employee e = new Employee(1L, 2L, "Male", "Bosnian", "Complicated");
        assertThrows(IllegalArgumentException.class, () -> EmployeeValidator.validate(e));
    }
}


package ba.unsa.etf.employeemanagement.validation;

import ba.unsa.etf.employeemanagement.model.Disability;
import ba.unsa.etf.employeemanagement.util.enums.DisabilityLevel;
import ba.unsa.etf.employeemanagement.util.enums.DisabilityType;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DisabilityValidatorTest {

    @Test
    void whenValidDisability_thenNoExceptionThrown() {
        Disability disability = new Disability();
        disability.setEmployeeId(1L);
        disability.setDisabilityType(DisabilityType.PHYSICAL.name());
        disability.setDisabilityLevel(DisabilityLevel.MILD.name());
        disability.setRegisteredDate(new Date());

        assertDoesNotThrow(() -> DisabilityValidator.validate(disability));
    }

    @Test
    void whenDisabilityIsNull_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> DisabilityValidator.validate(null));
    }

    @Test
    void whenEmployeeIdIsNull_thenThrowException() {
        Disability disability = new Disability();
        disability.setDisabilityType(DisabilityType.PHYSICAL.name());
        disability.setDisabilityLevel(DisabilityLevel.MILD.name());
        disability.setRegisteredDate(new Date());

        assertThrows(IllegalArgumentException.class, () -> DisabilityValidator.validate(disability));
    }

    @Test
    void whenInvalidDisabilityType_thenThrowException() {
        Disability disability = new Disability();
        disability.setEmployeeId(1L);
        disability.setDisabilityType("INVALID_TYPE");
        disability.setDisabilityLevel(DisabilityLevel.MILD.name());
        disability.setRegisteredDate(new Date());

        assertThrows(IllegalArgumentException.class, () -> DisabilityValidator.validate(disability));
    }

    @Test
    void whenInvalidDisabilityLevel_thenThrowException() {
        Disability disability = new Disability();
        disability.setEmployeeId(1L);
        disability.setDisabilityType(DisabilityType.PHYSICAL.name());
        disability.setDisabilityLevel("INVALID_LEVEL");
        disability.setRegisteredDate(new Date());

        assertThrows(IllegalArgumentException.class, () -> DisabilityValidator.validate(disability));
    }

    @Test
    void whenRegisteredDateIsInFuture_thenThrowException() {
        Disability disability = new Disability();
        disability.setEmployeeId(1L);
        disability.setDisabilityType(DisabilityType.PHYSICAL.name());
        disability.setDisabilityLevel(DisabilityLevel.MILD.name());
        disability.setRegisteredDate(new Date(System.currentTimeMillis() + 86400000)); // Tomorrow

        assertThrows(IllegalArgumentException.class, () -> DisabilityValidator.validate(disability));
    }
}

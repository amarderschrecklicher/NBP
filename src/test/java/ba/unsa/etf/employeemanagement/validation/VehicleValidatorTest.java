package ba.unsa.etf.employeemanagement.validation;

import ba.unsa.etf.employeemanagement.model.Vehicle;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleValidatorTest {

    @Test
    void validVehicle_passesValidation() {
        Vehicle v = new Vehicle(1L, 1L, "Toyota", "Corolla", "ABC-123", "12345678901234567", "PETROL", "SEDAN", new Date(), null);
        assertDoesNotThrow(() -> VehicleValidator.validate(v));
    }

    @Test
    void nullVehicle_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> VehicleValidator.validate(null));
    }

    @Test
    void missingEmployeeId_throwsException() {
        Vehicle v = new Vehicle(1L, null, "Toyota", "Corolla", "ABC-123", "12345678901234567", "PETROL", "SEDAN", new Date(), null);
        assertThrows(IllegalArgumentException.class, () -> VehicleValidator.validate(v));
    }

    @Test
    void invalidFuelType_throwsException() {
        Vehicle v = new Vehicle(1L, 1L, "Toyota", "Corolla", "ABC-123", "12345678901234567", "WATER", "SEDAN", new Date(), null);
        assertThrows(IllegalArgumentException.class, () -> VehicleValidator.validate(v));
    }

    @Test
    void invalidVehicleType_throwsException() {
        Vehicle v = new Vehicle(1L, 1L, "Toyota", "Corolla", "ABC-123", "12345678901234567", "PETROL", "SPACESHIP", new Date(), null);
        assertThrows(IllegalArgumentException.class, () -> VehicleValidator.validate(v));
    }

    @Test
    void returnDateBeforeAssignedDate_throwsException() {
        Calendar cal = Calendar.getInstance();
        Date assigned = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date returned = cal.getTime();

        Vehicle v = new Vehicle(1L, 1L, "Toyota", "Corolla", "ABC-123", "12345678901234567", "PETROL", "SEDAN", assigned, returned);
        assertThrows(IllegalArgumentException.class, () -> VehicleValidator.validate(v));
    }
}

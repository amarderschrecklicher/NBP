package ba.unsa.etf.employeemanagement.validation;

import ba.unsa.etf.employeemanagement.model.Finance;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FinanceValidatorTest {

    @Test
    void validFinance_passesValidation() {
        Finance f = new Finance(1L, 1L, "Test Bank", "123456", "BA123456", "TAX123", 2000.0, "BAM", "MONTHLY", 1);
        assertDoesNotThrow(() -> FinanceValidator.validate(f));
    }

    @Test
    void nullFinance_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> FinanceValidator.validate(null));
    }

    @Test
    void missingEmployeeId_throwsException() {
        Finance f = new Finance(1L, null, "Test Bank", "123456", "BA123456", "TAX123", 2000.0, "BAM", "MONTHLY", 1);
        assertThrows(IllegalArgumentException.class, () -> FinanceValidator.validate(f));
    }

    @Test
    void negativeSalary_throwsException() {
        Finance f = new Finance(1L, 1L, "Test Bank", "123456", "BA123456", "TAX123", -100.0, "BAM", "MONTHLY", 1);
        assertThrows(IllegalArgumentException.class, () -> FinanceValidator.validate(f));
    }

    @Test
    void invalidCurrency_throwsException() {
        Finance f = new Finance(1L, 1L, "Test Bank", "123456", "BA123456", "TAX123", 2000.0, "INVALID", "MONTHLY", 1);
        assertThrows(IllegalArgumentException.class, () -> FinanceValidator.validate(f));
    }

    @Test
    void invalidPaymentFrequency_throwsException() {
        Finance f = new Finance(1L, 1L, "Test Bank", "123456", "BA123456", "TAX123", 2000.0, "BAM", "DAILY", 1);
        assertThrows(IllegalArgumentException.class, () -> FinanceValidator.validate(f));
    }

    @Test
    void invalidBonusEligible_throwsException() {
        Finance f = new Finance(1L, 1L, "Test Bank", "123456", "BA123456", "TAX123", 2000.0, "BAM", "MONTHLY", 2);
        assertThrows(IllegalArgumentException.class, () -> FinanceValidator.validate(f));
    }
}

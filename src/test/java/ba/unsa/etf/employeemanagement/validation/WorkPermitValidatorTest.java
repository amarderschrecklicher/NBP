package ba.unsa.etf.employeemanagement.validation;

import ba.unsa.etf.employeemanagement.model.WorkPermit;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class WorkPermitValidatorTest {

    @Test
    void validWorkPermit_passesValidation() {
        Calendar cal = Calendar.getInstance();
        Date issueDate = cal.getTime();
        cal.add(Calendar.YEAR, 1);
        Date expiryDate = cal.getTime();

        WorkPermit wp = new WorkPermit(1L, 1L, "WP123456", "OPEN", "Canada", issueDate, expiryDate, "ACTIVE");
        assertDoesNotThrow(() -> WorkPermitValidator.validate(wp));
    }

    @Test
    void nullWorkPermit_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> WorkPermitValidator.validate(null));
    }

    @Test
    void missingEmployeeId_throwsException() {
        WorkPermit wp = new WorkPermit(1L, null, "WP123456", "OPEN", "Canada", new Date(), new Date(), "ACTIVE");
        assertThrows(IllegalArgumentException.class, () -> WorkPermitValidator.validate(wp));
    }

    @Test
    void invalidPermitType_throwsException() {
        WorkPermit wp = new WorkPermit(1L, 1L, "WP123456", "INVALID_TYPE", "Canada", new Date(), new Date(), "ACTIVE");
        assertThrows(IllegalArgumentException.class, () -> WorkPermitValidator.validate(wp));
    }

    @Test
    void expiryBeforeIssue_throwsException() {
        Calendar cal = Calendar.getInstance();
        Date issueDate = cal.getTime();
        cal.add(Calendar.YEAR, -1);
        Date expiryDate = cal.getTime();

        WorkPermit wp = new WorkPermit(1L, 1L, "WP123456", "OPEN", "Canada", issueDate, expiryDate, "ACTIVE");
        assertThrows(IllegalArgumentException.class, () -> WorkPermitValidator.validate(wp));
    }

    @Test
    void invalidStatus_throwsException() {
        WorkPermit wp = new WorkPermit(1L, 1L, "WP123456", "OPEN", "Canada", new Date(), new Date(), "UNKNOWN");
        assertThrows(IllegalArgumentException.class, () -> WorkPermitValidator.validate(wp));
    }
}

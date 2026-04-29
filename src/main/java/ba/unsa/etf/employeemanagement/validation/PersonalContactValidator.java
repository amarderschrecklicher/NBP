package ba.unsa.etf.employeemanagement.validation;

import ba.unsa.etf.employeemanagement.model.PersonalContact;
import java.util.regex.Pattern;

public class PersonalContactValidator {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9\\s-]{7,20}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    public static void validate(PersonalContact personalContact) {
        if (personalContact == null) {
            throw new IllegalArgumentException("Personal contact cannot be null");
        }
        if (personalContact.getEmployeeId() == null) {
            throw new IllegalArgumentException("Employee ID is required");
        }
        if (personalContact.getPhoneNumber() == null || personalContact.getPhoneNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required");
        }
        if (!PHONE_PATTERN.matcher(personalContact.getPhoneNumber()).matches()) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        if (personalContact.getPersonalEmail() == null || personalContact.getPersonalEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Personal email is required");
        }
        if (personalContact.getPersonalEmail().length() > 100) {
            throw new IllegalArgumentException("Personal email must not exceed 100 characters");
        }
        if (!EMAIL_PATTERN.matcher(personalContact.getPersonalEmail()).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
}

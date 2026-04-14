package ba.unsa.etf.employeemanagement.dto.request;

import ba.unsa.etf.employeemanagement.util.enums.*;
import ba.unsa.etf.employeemanagement.util.validation.ValidDateRange;
import ba.unsa.etf.employeemanagement.util.validation.ValueOfEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidDateRange(
        startDateField = "hireDate",
        endDateField = "terminationDate",
        message = "Termination date must be after or equal to hire date"
)
public class EmployeeRequest {

    // ── Employee fields ──
    @ValueOfEnum(enumClass = Gender.class, message = "Gender must be one of: MALE, FEMALE")
    @Size(max = 50, message = "Gender must not exceed 50 characters")
    private String gender;

    @Size(max = 100, message = "Nationality must not exceed 100 characters")
    private String nationality;

    @ValueOfEnum(enumClass = MaritalStatus.class, message = "Marital status must be one of: SINGLE, MARRIED, DIVORCED, WIDOWED, SEPARATED")
    @Size(max = 50, message = "Marital status must not exceed 50 characters")
    private String maritalStatus;

    // ── Address fields ──
    @Size(max = 255, message = "Street must not exceed 255 characters")
    private String street;

    @Size(max = 255, message = "City must not exceed 255 characters")
    private String city;

    @Pattern(regexp = "^[A-Za-z0-9 -]{3,10}$", message = "Postal code must be 3-10 alphanumeric characters, spaces, or dashes")
    private String postalCode;

    @Size(max = 255, message = "Country must not exceed 255 characters")
    private String country;

    // ── NbpUser fields ──
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 255, message = "First name must be between 2 and 255 characters")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿčćžšđČĆŽŠĐ' -]+$", message = "First name can only contain letters, spaces, hyphens, and apostrophes")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 255, message = "Last name must be between 2 and 255 characters")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿčćžšđČĆŽŠĐ' -]+$", message = "Last name can only contain letters, spaces, hyphens, and apostrophes")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    private String password;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[A-Za-z0-9._-]+$", message = "Username can only contain letters, digits, dots, underscores, and hyphens")
    private String username;

    @Pattern(regexp = "^\\+?[0-9\\s-]{7,20}$", message = "Phone number must be a valid format (7-20 digits, optional + prefix)")
    private String phoneNumber;

    @Past(message = "Birth date must be in the past")
    private Date birthDate;

    // Use roleName instead of numeric roleId
    // @ValueOfEnum(enumClass = EmsRole.class, message = "Role must be one of: EMS_ADMINISTRATOR, EMS_MANAGER, EMS_EMPLOYEE")
    @NotBlank(message = "Role name is required")
    private String roleName;

    // ── Employment fields ──
    @Size(max = 100, message = "Employment number must not exceed 100 characters")
    private String employmentNumber;

    @PastOrPresent(message = "Hire date cannot be in the future")
    private Date hireDate;

    private Date terminationDate;

    @Size(max = 255, message = "Job title must not exceed 255 characters")
    private String jobTitle;

    @ValueOfEnum(enumClass = EmploymentType.class, message = "Employment type must be one of: FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP, TEMPORARY, FREELANCE")
    @Size(max = 100, message = "Employment type must not exceed 100 characters")
    private String employmentType;

    @ValueOfEnum(enumClass = EmploymentStatus.class, message = "Employment status must be one of: ACTIVE, INACTIVE, ON_LEAVE, TERMINATED, SUSPENDED, PROBATION")
    @Size(max = 100, message = "Employment status must not exceed 100 characters")
    private String employmentStatus;

    // ── Department ──  (use existing ID or create new)
    @Positive(message = "Department ID must be a positive number")
    private Long departmentId;

    @Size(max = 255, message = "Department name must not exceed 255 characters")
    private String departmentName;

    @Size(max = 500, message = "Department description must not exceed 500 characters")
    private String departmentDescription;
}

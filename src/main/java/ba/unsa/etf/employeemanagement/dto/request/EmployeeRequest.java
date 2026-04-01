package ba.unsa.etf.employeemanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {

    // ── Employee fields ──
    private String gender;
    private String nationality;
    private String maritalStatus;

    // ── Address fields ──
    private String street;
    private String city;
    private String postalCode;
    private String country;

    // ── NbpUser fields ──
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String username;
    private String phoneNumber;
    private Date birthDate;
    private Long roleId;

    // ── Employment fields ──
    private String employmentNumber;
    private Date hireDate;
    private Date terminationDate;
    private String jobTitle;
    private String employmentType;
    private String employmentStatus;

    // ── Department ──  (use existing ID or create new)
    private Long departmentId;
    private String departmentName;
    private String departmentDescription;
}

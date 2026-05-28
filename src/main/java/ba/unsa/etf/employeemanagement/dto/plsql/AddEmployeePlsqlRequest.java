package ba.unsa.etf.employeemanagement.dto.plsql;

import ba.unsa.etf.employeemanagement.util.enums.EmploymentType;
import ba.unsa.etf.employeemanagement.util.validation.ValueOfEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddEmployeePlsqlRequest {

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Long userId;

    @Size(max = 50, message = "Gender must not exceed 50 characters")
    private String gender;

    @Size(max = 100, message = "Nationality must not exceed 100 characters")
    private String nationality;

    @Size(max = 50, message = "Marital status must not exceed 50 characters")
    private String maritalStatus;

    @Positive(message = "Manager ID must be positive")
    private Long managerId;

    @NotBlank(message = "Employment number is required")
    @Size(max = 100, message = "Employment number must not exceed 100 characters")
    private String employmentNumber;

    @PastOrPresent(message = "Hire date cannot be in the future")
    private Date hireDate;

    @Size(max = 255, message = "Job title must not exceed 255 characters")
    private String jobTitle;

    @ValueOfEnum(enumClass = EmploymentType.class, message = "Employment type must be one of: FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP, TEMPORARY, FREELANCE")
    @Size(max = 100, message = "Employment type must not exceed 100 characters")
    private String employmentType;

    @Positive(message = "Department ID must be positive")
    private Long departmentId;
}

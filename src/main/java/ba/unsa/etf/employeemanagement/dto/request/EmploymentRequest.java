package ba.unsa.etf.employeemanagement.dto.request;

import ba.unsa.etf.employeemanagement.util.enums.EmploymentStatus;
import ba.unsa.etf.employeemanagement.util.enums.EmploymentType;
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
public class EmploymentRequest {

    @NotNull(message = "Employee ID is required")
    @Positive(message = "Employee ID must be a positive number")
    private Long employeeId;

    @Size(max = 100, message = "Employment number must not exceed 100 characters")
    private String number;

    @NotNull(message = "Hire date is required")
    @PastOrPresent(message = "Hire date cannot be in the future")
    private Date hireDate;

    private Date terminationDate;

    @NotBlank(message = "Job title is required")
    @Size(max = 255, message = "Job title must not exceed 255 characters")
    private String jobTitle;

    @NotBlank(message = "Employment type is required")
    @ValueOfEnum(enumClass = EmploymentType.class, message = "Employment type must be one of: FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP, TEMPORARY, FREELANCE")
    @Size(max = 100, message = "Employment type must not exceed 100 characters")
    private String employmentType;

    @NotBlank(message = "Status is required")
    @ValueOfEnum(enumClass = EmploymentStatus.class, message = "Status must be one of: ACTIVE, INACTIVE, ON_LEAVE, TERMINATED, SUSPENDED, PROBATION")
    @Size(max = 100, message = "Status must not exceed 100 characters")
    private String status;

    @NotNull(message = "Department ID is required")
    @Positive(message = "Department ID must be a positive number")
    private Long departmentId;
}

package ba.unsa.etf.employeemanagement.dto.request;

import ba.unsa.etf.employeemanagement.util.enums.WorkPermitStatus;
import ba.unsa.etf.employeemanagement.util.enums.WorkPermitType;
import ba.unsa.etf.employeemanagement.util.validation.ValueOfEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkPermitRequest {

    @NotNull(message = "Employee ID is required")
    @Positive(message = "Employee ID must be a positive number")
    private Long employeeId;

    @NotBlank(message = "Permit number is required")
    @Size(max = 100, message = "Permit number must not exceed 100 characters")
    @Pattern(regexp = "^[A-Z0-9 -]+$", message = "Permit number must contain only uppercase letters, digits, spaces, or dashes")
    private String permitNumber;

    @NotBlank(message = "Permit type is required")
    @ValueOfEnum(enumClass = WorkPermitType.class, message = "Permit type must be one of: OPEN, EMPLOYER_SPECIFIC, POST_GRADUATE, SPOUSAL, OTHER")
    private String permitType;

    @NotBlank(message = "Issuing country is required")
    @Size(max = 100, message = "Issuing country must not exceed 100 characters")
    private String issuingCountry;

    @NotNull(message = "Issue date is required")
    @PastOrPresent(message = "Issue date cannot be in the future")
    private Date issueDate;

    @NotNull(message = "Expiry date is required")
    @Future(message = "Expiry date must be in the future")
    private Date expiryDate;

    @NotBlank(message = "Status is required")
    @ValueOfEnum(enumClass = WorkPermitStatus.class, message = "Status must be one of: ACTIVE, EXPIRED, PENDING, REVOKED, RENEWAL_IN_PROGRESS")
    private String status;
}

package ba.unsa.etf.employeemanagement.dto.request;

import ba.unsa.etf.employeemanagement.util.enums.DisabilityLevel;
import ba.unsa.etf.employeemanagement.util.enums.DisabilityType;
import ba.unsa.etf.employeemanagement.util.validation.ValueOfEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisabilityRequest {

    @NotNull(message = "Employee ID is required")
    @Positive(message = "Employee ID must be a positive number")
    private Long employeeId;

    @NotBlank(message = "Disability type is required")
    @ValueOfEnum(enumClass = DisabilityType.class, message = "Disability type must be one of: PHYSICAL, SENSORY, INTELLECTUAL, MENTAL_HEALTH, LEARNING, OTHER")
    private String disabilityType;

    @NotBlank(message = "Disability level is required")
    @ValueOfEnum(enumClass = DisabilityLevel.class, message = "Disability level must be one of: MILD, MODERATE, SEVERE, PROFOUND")
    private String disabilityLevel;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @NotNull(message = "Registered date is required")
    @PastOrPresent(message = "Registered date cannot be in the future")
    private Date registeredDate;
}

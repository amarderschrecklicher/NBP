package ba.unsa.etf.employeemanagement.dto.request;

import ba.unsa.etf.employeemanagement.util.enums.FamilyRelation;
import ba.unsa.etf.employeemanagement.util.validation.ValueOfEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FamilyMemberRequest {
    @NotNull(message = "Employee ID is required")
    @Positive(message = "Employee ID must be a positive number")
    private Long employeeId;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
    private String lastName;

    @NotBlank(message = "Relation is required")
    @ValueOfEnum(enumClass = FamilyRelation.class, message = "Relation must be one of: SPOUSE, CHILD, PARENT, SIBLING, OTHER")
    private String relation;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private Date dateOfBirth;

    @Min(value = 0, message = "Dependent must be 0 or 1")
    @Max(value = 1, message = "Dependent must be 0 or 1")
    private Integer dependent;

    @Size(max = 100, message = "Occupation must not exceed 100 characters")
    private String occupation;
}